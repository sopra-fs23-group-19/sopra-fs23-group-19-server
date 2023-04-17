package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.Words;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.words.WordsGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller This class is responsible for handling all REST request that are related to the
 * game logic. The controller will receive the request and delegate the execution to the GameService and
 * finally return the result.
 */
public class GameController {

    private final GameService gameService;
    private final WordsService wordsService;

    GameController(GameService gameService, WordsService wordsService) {

        this.gameService = gameService;
        this.wordsService = wordsService;
    }

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    // start a new game
    @UserLoginToken
    @PostMapping("/gameRounds/{roomId}")
    @ResponseStatus(HttpStatus.OK)
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

    @UserLoginToken
    @GetMapping("/games/words")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public WordsGetDTO getThreeWords(){

        Words words = new Words();
        String wordsToBeChosen = wordsService.getThreeWords();
        words.setWordsToBeChosen(wordsToBeChosen);
        WordsGetDTO wordsGetDTO = DTOMapper.INSTANCE.convertEntityToWordsGetDTO(words);
        return wordsGetDTO;
    }

    public List<Long> transferStringToLong(String players){
        String[] strArray = players.split(",");
        List<Long> longList = new ArrayList<>();

        for (String s : strArray) {
            longList.add(Long.valueOf(s));
        }

        return longList;
    }

    @UserLoginToken
    @PostMapping("/gameRounds/drawing")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameTurnGetDTO submitDrawing(@RequestBody GameTurnPostDTO gameTurnPostDTO) {
        // get current game turn status
        GameTurn gameTurnInput = DTOMapper.INSTANCE.convertGameTurnPostDTOtoEntity(gameTurnPostDTO);

        GameTurn gameTurn = gameService.submitImage(gameTurnInput);
        return DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

    }

}
