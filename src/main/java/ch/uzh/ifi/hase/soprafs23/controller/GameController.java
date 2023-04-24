package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnAfterGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.RoomService;
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
    private final RoomService roomService;
    GameController(GameService gameService, UserService userService, RoomService roomService ) {

        this.gameService = gameService;
        this.userService = userService;
        this.roomService = roomService;
    }

    // start a new game
    //@UserLoginToken
    @PostMapping("/games/waitingArea/{roomId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTurnAfterGetDTO startGame(@PathVariable long roomId){
        // start game turn
        Room room = gameService.getRoom(roomId);
        GameTurn gameTurn = gameService.startGame(room);
        // roomService.activateRoom(roomId,gameTurn.getGameId(),gameTurn.getId() );
        // List<Long> playersIds = transferStringToLong(gameTurn.getAllPlayersIds());
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        return changeGetToAfter(gameTurnGetDTO);

//        for(int i=0; i<playersIds.size(); i++){
//            simpMessagingTemplate.convertAndSend("/game/startGame/"+ Long.toString(playersIds.get(i)), gameTurnGetDTO);
//        }
    }

//    @UserLoginToken
//    @PostMapping("/games/leave/{gameId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    public void endGame(@PathVariable long gameId){
//        // start game turn
//        GameTurn gameTurn = gameService.startGameTurn(room);
//        roomService.activateRoom(roomId,gameTurn.getGameId(),gameTurn.getId() );
//        // List<Long> playersIds = transferStringToLong(gameTurn.getAllPlayersIds());
//        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);
//
//        return changeGetToAfter(gameTurnGetDTO);
//
////        for(int i=0; i<playersIds.size(); i++){
////            simpMessagingTemplate.convertAndSend("/game/startGame/"+ Long.toString(playersIds.get(i)), gameTurnGetDTO);
////        }
//    }

    // get rank list from this game
    //if game status is false.
    //@UserLoginToken
    @GetMapping("/games/ranks/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getGameRank(@PathVariable long gameId){

        List<User> rankedUsers = gameService.rankAll(gameId);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : rankedUsers) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }
    ///every second get status, ids
//    @UserLoginToken
//    @GetMapping("/games/{gameId}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public GameGetDTO getGameById(@PathVariable long gameId){
//        Game currentGame = gameService.getGame(gameId);
//        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(currentGame);
//    }

    //@UserLoginToken
    @GetMapping("/games/leave/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void QuitGame(@PathVariable long gameId){
        gameService.endGame(gameId);
    }


    public GameTurnAfterGetDTO changeGetToAfter(GameTurnGetDTO gameTurnGetDTO){

        GameTurnAfterGetDTO gameTurnAfterGetDTO = new GameTurnAfterGetDTO();

        gameTurnAfterGetDTO.setId(gameTurnGetDTO.getId());
        gameTurnAfterGetDTO.setStatus(gameTurnGetDTO.getStatus());
        gameTurnAfterGetDTO.setSubmitNum(gameTurnGetDTO.getSubmitNum());
        gameTurnAfterGetDTO.setDrawingPlayerId(gameTurnGetDTO.getDrawingPlayerId());
        gameTurnAfterGetDTO.setImage(gameTurnGetDTO.getImage());
        gameTurnAfterGetDTO.setTargetWord(gameTurnGetDTO.getTargetWord());
////        gameTurnAfterGetDTO.setDrawingPhase(gameTurnGetDTO.getDrawingPhase());
        gameTurnAfterGetDTO.setRoomId(gameTurnGetDTO.getRoomId());
        gameTurnAfterGetDTO.setStatus(gameTurnGetDTO.getStatus());
//        gameTurnAfterGetDTO.setGameStatus(gameTurnGetDTO.getGameStatus());



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
