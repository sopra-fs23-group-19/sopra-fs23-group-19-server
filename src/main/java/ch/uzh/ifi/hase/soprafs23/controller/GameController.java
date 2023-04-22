package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnAfterGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
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
    private final UserService userService;

    GameController(GameService gameService, UserService userService) {

        this.gameService = gameService;
        this.userService = userService;
    }

//    @Autowired
//    SimpMessagingTemplate simpMessagingTemplate;


    // start a new game
    //@UserLoginToken
    @PostMapping("/games/{roomId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTurnAfterGetDTO startGame(@PathVariable("roomId") long roomId){
        // start game turn
        Room room = gameService.getRoom(roomId);
        GameTurn gameTurn = gameService.startGameTurn(room);
        // List<Long> playersIds = transferStringToLong(gameTurn.getAllPlayersIds());
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        return changeGetToAfter(gameTurnGetDTO);

//        for(int i=0; i<playersIds.size(); i++){
//            simpMessagingTemplate.convertAndSend("/game/startGame/"+ Long.toString(playersIds.get(i)), gameTurnGetDTO);
//        }
    }

    public GameTurnAfterGetDTO changeGetToAfter(GameTurnGetDTO gameTurnGetDTO){

        GameTurnAfterGetDTO gameTurnAfterGetDTO = new GameTurnAfterGetDTO();
        gameTurnAfterGetDTO.setId(gameTurnGetDTO.getId());
        gameTurnAfterGetDTO.setDrawingPlayerId(gameTurnGetDTO.getDrawingPlayerId());
        gameTurnAfterGetDTO.setImage(gameTurnGetDTO.getImage());
        gameTurnAfterGetDTO.setWordsToBeChosen(gameTurnGetDTO.getWordsToBeChosen());
        gameTurnAfterGetDTO.setDrawingPhase(gameTurnGetDTO.getDrawingPhase());
        gameTurnAfterGetDTO.setGameId(gameTurnGetDTO.getGameId());
        gameTurnAfterGetDTO.setGameTurnStatus(gameTurnGetDTO.getGameTurnStatus());
        gameTurnAfterGetDTO.setGameStatus(gameTurnGetDTO.getGameStatus());


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
