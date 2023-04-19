package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.WordsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller This class is responsible for handling all REST request that are related to the
 * game logic. The controller will receive the request and delegate the execution to the GameService and
 * finally return the result.
 */

@RestController
public class GameController {

    private final GameService gameService;
    private final WordsService wordsService;

    GameController(GameService gameService, WordsService wordsService) {

        this.gameService = gameService;
        this.wordsService = wordsService;
    }

//    @Autowired
//    SimpMessagingTemplate simpMessagingTemplate;


    // start a new game
    @UserLoginToken
    @PostMapping("/games/gameRounds/{roomId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTurnGetDTO startGame(@PathVariable("roomId") long roomId){
        // start game turn
        Room room = gameService.getRoom(roomId);
        GameTurn gameTurn = gameService.startGameTurn(room);
        // List<Long> playersIds = transferStringToLong(gameTurn.getAllPlayersIds());
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        return gameTurnGetDTO;

//        for(int i=0; i<playersIds.size(); i++){
//            simpMessagingTemplate.convertAndSend("/game/startGame/"+ Long.toString(playersIds.get(i)), gameTurnGetDTO);
//        }
    }

    // get three words to be chosen by the drawing player
    @UserLoginToken
    @GetMapping("/games/words/{gameTurnId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameTurnGetDTO getThreeWords(@PathVariable("gameTurnId") long gameTurnId){

        GameTurn gameTurn = wordsService.setThreeWords(gameTurnId);

        return DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);
    }

    public List<Long> transferStringToLong(String players){
        String[] strArray = players.split(",");
        List<Long> longList = new ArrayList<>();

        for (String s : strArray) {
            longList.add(Long.valueOf(s));
        }

        return longList;
    }

    // drawing player submits the image
    @UserLoginToken
    @PostMapping("/gameRounds/drawing")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTurnGetDTO submitDrawing(@RequestBody GameTurnPostDTO gameTurnPostDTO) {
        // get current game turn status
        GameTurn gameTurnInput = DTOMapper.INSTANCE.convertGameTurnPostDTOtoEntity(gameTurnPostDTO);

        GameTurn gameTurn = gameService.submitImage(gameTurnInput);
        return DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

    }

    // drawing player chooses the target word
    @UserLoginToken
    @PutMapping("gameRounds/word")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void setTargetWord(@RequestBody GameTurnPostDTO gameTurnPostDTO){
        gameService.setTargetWord(gameTurnPostDTO);

    }


}
