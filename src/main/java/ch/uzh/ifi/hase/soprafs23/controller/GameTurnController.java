package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnAfterGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.TurnRankGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.GameTurnService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import ch.uzh.ifi.hase.soprafs23.service.WordsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class GameTurnController {

    private final UserService userService;
    private final GameTurnService gameTurnService;
    private final GameService gameService;
    private final WordsService wordsService;

    GameTurnController(UserService userService,
                       GameTurnService gameTurnService,
                       WordsService wordsService,
                       GameService gameService) {

        this.userService = userService;
        this.gameTurnService = gameTurnService;
        this.wordsService = wordsService;
        this.gameService = gameService;
    }

    // get three words to be chosen by the drawing player
    @UserLoginToken
    @GetMapping("/gameRounds/words/{gameTurnId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Set<String> getThreeWords() {

        return wordsService.getThreeWords();
    }

    // drawing player chooses the target word
    @UserLoginToken
    @PutMapping("/gameRounds/words")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void setTargetWord(@RequestBody GameTurnPutDTO gameTurnPutDTO){
        gameTurnService.setTargetWord(gameTurnPutDTO);

    }

    // drawing player updates the image
    @UserLoginToken
    @PutMapping("/gameRounds/drawings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateDrawing(@RequestBody GameTurnPutDTO gameTurnPutDTO) {

        gameTurnService.updateImage(gameTurnPutDTO);

    }

    // drawing player submits the image
    @UserLoginToken
    @PostMapping("/gameRounds/finalDrawings")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTurnAfterGetDTO submitDrawing(@RequestBody GameTurnPutDTO gameTurnPutDTO) {
        //only drawingUser can submit Drawing
        GameTurn gameTurn = gameTurnService.submitImage(gameTurnPutDTO);
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);
        return changeGetToAfter(gameTurnGetDTO);
    }

    // guessing player submits the answer in advance
    @UserLoginToken
    @PostMapping("/gameRounds/answers/{gameTurnId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void submitAnswer(@RequestBody UserPutDTO userPutDTO, @PathVariable Long gameTurnId){

        gameTurnService.calculateScore(userPutDTO, gameTurnId);
    }

    // get rank list from this turn
    @UserLoginToken
    @GetMapping("/gameRounds/ranks/{gameTurnId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TurnRankGetDTO getRank( @PathVariable Long gameTurnId){

//        gameTurnService.calculateDrawerScore(gameTurnId);

        return gameTurnService.setTurnRankInfo(gameTurnId);
    }

    // users request refreshed information from backend every second
    @UserLoginToken
    @GetMapping("/gameRounds/information/{gameTurnId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameTurnAfterGetDTO getGameTurnInfo(@PathVariable("gameTurnId") Long gameTurnId){

        GameTurn gameTurn = gameTurnService.getGameTurn(gameTurnId);
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        return changeGetToAfter(gameTurnGetDTO);
    }

    @UserLoginToken
    @GetMapping("/gameRounds/rankConfirmation/{gameTurnId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameTurnAfterGetDTO confirmRank(@PathVariable("gameTurnId") Long gameTurnId, @PathVariable("userId") Long userId){

        GameTurn gameTurn = gameTurnService.confirmRank(gameTurnId, userId);
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        return changeGetToAfter(gameTurnGetDTO);
    }


    public GameTurnAfterGetDTO changeGetToAfter(GameTurnGetDTO gameTurnGetDTO){

        GameTurnAfterGetDTO gameTurnAfterGetDTO = new GameTurnAfterGetDTO();

        gameTurnAfterGetDTO.setId(gameTurnGetDTO.getId());
        gameTurnAfterGetDTO.setStatus(gameTurnGetDTO.getStatus());
        gameTurnAfterGetDTO.setSubmitNum(gameTurnGetDTO.getSubmitNum());
        gameTurnAfterGetDTO.setDrawingPlayerId(gameTurnGetDTO.getDrawingPlayerId());
        gameTurnAfterGetDTO.setImage(gameTurnGetDTO.getImage());
        gameTurnAfterGetDTO.setTargetWord(gameTurnGetDTO.getTargetWord());
        gameTurnAfterGetDTO.setRoomId(gameTurnGetDTO.getRoomId());
        gameTurnAfterGetDTO.setDrawingPlayerName(userService.findByid(gameTurnGetDTO.getDrawingPlayerId()));


        if(gameService.getAllPlayersIds(gameTurnGetDTO.getId())==null) {
            gameTurnAfterGetDTO.setPlayers(null);
        }else {
            for(Long iid: gameService.getAllPlayersIds(gameTurnGetDTO.getId())){
                gameTurnAfterGetDTO.getPlayers().add(DTOMapper.INSTANCE.convertEntityToUserNameDTO(userService.retrieveUser(iid)));
            }
        }

        return gameTurnAfterGetDTO;
    }

}
