package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnAfterGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameTurnService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import ch.uzh.ifi.hase.soprafs23.service.WordsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameTurnController {

    private final UserService userService;
    private final GameTurnService gameTurnService;
    private final WordsService wordsService;

    GameTurnController(UserService userService, GameTurnService gameTurnService, WordsService wordsService) {

        this.userService = userService;
        this.gameTurnService = gameTurnService;
        this.wordsService = wordsService;
    }

    // get three words to be chosen by the drawing player
    @UserLoginToken
    @GetMapping("/gameRounds/words/{gameTurnId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameTurnAfterGetDTO getThreeWords(@PathVariable("gameTurnId") long gameTurnId){

        GameTurn gameTurn = wordsService.setThreeWords(gameTurnId);
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        return changeGetToAfter(gameTurnGetDTO);
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
    @PutMapping("/gameRounds/answers/{gameTurnId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void submitAnswer(@RequestBody UserPutDTO userPutDTO, @PathVariable("gameTurnId") long gameTurnId){

        gameTurnService.calculateScore(userPutDTO, gameTurnId);
    }

    // get rank list from this turn
    @UserLoginToken
    @GetMapping("/gameRounds/ranks/{gameTurnId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getRank( @PathVariable long gameTurnId){

        List<User> rankedUsers = gameTurnService.rank(gameTurnId);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : rankedUsers) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    // users request refreshed information from backend every second
    @UserLoginToken
    @GetMapping("/gameRounds/information/{gameTurnId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameTurnAfterGetDTO getGameTurnInfo(@PathVariable("gameTurnId") long gameTurnId, @PathVariable("userId") long userId){

        GameTurn gameTurn = gameTurnService.getGameTurn(gameTurnId);
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        return changeGetAccordingToUserId(gameTurnGetDTO, userId);
    }

//    @UserLoginToken
//    @PostMapping("/games/gameRounds/{gameId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    public GameTurnAfterGetDTO startGameTurn(@PathVariable("gameId") long gameId){
//
//        GameTurn gameTurn = gameTurnService.startNewGameTurn(gameId);
//        GameTurnGetDTO gameTurnGetDTO =  DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);
//
//        return changeGetToAfter(gameTurnGetDTO);
//    }


    public GameTurnAfterGetDTO changeGetAccordingToUserId(GameTurnGetDTO gameTurnGetDTO,Long userId){
        GameTurnAfterGetDTO gameTurnAfterGetDTO = new GameTurnAfterGetDTO();
        gameTurnAfterGetDTO.setId(gameTurnGetDTO.getId());
        gameTurnAfterGetDTO.setDrawingPlayerId(gameTurnGetDTO.getDrawingPlayerId());
        gameTurnAfterGetDTO.setImage(gameTurnGetDTO.getImage());
        //only drawing player knows the words information
        if(userId == gameTurnGetDTO.getDrawingPlayerId())
        {
            gameTurnAfterGetDTO.setWordsToBeChosen(gameTurnGetDTO.getWordsToBeChosen());
            gameTurnAfterGetDTO.setTargetWord(gameTurnGetDTO.getTargetWord());
        }
        gameTurnAfterGetDTO.setGameId(gameTurnGetDTO.getGameId());

        //added
        gameTurnAfterGetDTO.setSubmittedAnswerIds(gameTurnGetDTO.getSubmittedAnswerIds());
        gameTurnAfterGetDTO.setStatus(gameTurnGetDTO.getStatus());
        gameTurnAfterGetDTO.setDrawingPlayerName(userService.findById(gameTurnGetDTO.getDrawingPlayerId()));

        if(gameTurnGetDTO.getAllPlayersIds()==null) {
            gameTurnAfterGetDTO.setPlayers(null);
        }else {
            for(Long iid: gameTurnGetDTO.getAllPlayersIds()){
                gameTurnAfterGetDTO.getPlayers().add(DTOMapper.INSTANCE.convertEntityToUserNameDTO(userService.retrieveUser(iid)));
            }
        }

        return gameTurnAfterGetDTO;
    }

    public GameTurnAfterGetDTO changeGetToAfter(GameTurnGetDTO gameTurnGetDTO){
        GameTurnAfterGetDTO gameTurnAfterGetDTO = new GameTurnAfterGetDTO();
        gameTurnAfterGetDTO.setId(gameTurnGetDTO.getId());
        gameTurnAfterGetDTO.setDrawingPlayerId(gameTurnGetDTO.getDrawingPlayerId());
        gameTurnAfterGetDTO.setImage(gameTurnGetDTO.getImage());
        gameTurnAfterGetDTO.setWordsToBeChosen(gameTurnGetDTO.getWordsToBeChosen());

//        gameTurnAfterGetDTO.setDrawingPhase(gameTurnGetDTO.getDrawingPhase());


        gameTurnAfterGetDTO.setGameId(gameTurnGetDTO.getGameId());

        gameTurnAfterGetDTO.setTargetWord(gameTurnGetDTO.getTargetWord());
        //added
        gameTurnAfterGetDTO.setSubmittedAnswerIds(gameTurnGetDTO.getSubmittedAnswerIds());
        gameTurnAfterGetDTO.setStatus(gameTurnGetDTO.getStatus());
       gameTurnAfterGetDTO.setDrawingPlayerName(userService.findById(gameTurnGetDTO.getDrawingPlayerId()));

        if(gameTurnGetDTO.getAllPlayersIds()==null) {
            gameTurnAfterGetDTO.setPlayers(null);
        }else {
            for(Long iid: gameTurnGetDTO.getAllPlayersIds()){
                gameTurnAfterGetDTO.getPlayers().add(DTOMapper.INSTANCE.convertEntityToUserNameDTO(userService.retrieveUser(iid)));
            }
        }

        return gameTurnAfterGetDTO;
    }

}
