package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnAfterGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
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

    // start a new game
    //@UserLoginToken
    @PostMapping("/games/waitingArea/{roomId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTurnAfterGetDTO startGame(@PathVariable Long roomId){
        // start game turn
        Room room = gameService.getRoom(roomId);
        GameTurn gameTurn = gameService.startGame(room);
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        return changeGetToAfter(gameTurnGetDTO);
    }

    // get rank list from this game
    //if game status is end_game.
    //@UserLoginToken
    @GetMapping("/games/ranks/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getGameRank(@PathVariable Long gameId){

        List<User> rankedUsers = gameService.rankAll(gameId);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : rankedUsers) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    //@UserLoginToken
    @PutMapping("/games/ending/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void QuitGame(@PathVariable Long gameId){
        gameService.endGame(gameId);
    }

    @GetMapping("/leaderboard")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getRank(){
        List<User> rankedUsers = gameService.getLeaderboardRank();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : rankedUsers) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
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
        gameTurnAfterGetDTO.setStatus(gameTurnGetDTO.getStatus());
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

