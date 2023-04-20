package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.GameTurnService;
import ch.uzh.ifi.hase.soprafs23.service.WordsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameTurnController {

    private final GameService gameService;
    private final GameTurnService gameTurnService;
    private final WordsService wordsService;

    GameTurnController(GameService gameService, GameTurnService gameTurnService, WordsService wordsService) {

        this.gameService = gameService;
        this.gameTurnService = gameTurnService;
        this.wordsService = wordsService;
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

    // drawing player chooses the target word
    @UserLoginToken
    @PutMapping("gameRounds/word")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void setTargetWord(@RequestBody GameTurnPutDTO gameTurnPutDTO){
        gameTurnService.setTargetWord(gameTurnPutDTO);

    }

    // drawing player updates the image
    @UserLoginToken
    @PostMapping("/gameRounds/drawing")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void updateDrawing(@RequestBody GameTurnPutDTO gameTurnPutDTO) {

        gameTurnService.updateImage(gameTurnPutDTO);

    }

    // drawing player submits the image
    @UserLoginToken
    @PostMapping("/gameRounds/submitDrawing")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTurnGetDTO submitDrawing(@RequestBody GameTurnPutDTO gameTurnPutDTO) {

        GameTurn gameTurn = gameTurnService.submitImage(gameTurnPutDTO);
        return DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

    }

    // guessing player submits the answer in advance
    @UserLoginToken
    @PutMapping("/gameRounds/answer")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void submitAnswer(@RequestBody UserPutDTO userPutDTO, @RequestAttribute long gameTurnId){

        gameTurnService.calculateScore(userPutDTO, gameTurnId);
    }

    // get rank list from this turn
    @UserLoginToken
    @GetMapping("/gameRounds/rank/{gameTurnId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getRank(@RequestAttribute long gameTurnId){

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
    @GetMapping("/gameRounds/{gameTurnId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameTurnGetDTO getGameTurnInfo(@RequestAttribute long gameTurnId){

        GameTurn gameTurn = gameTurnService.getGameTurn(gameTurnId);

        return DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);
    }

    @UserLoginToken
    @PostMapping("/games/gameRounds/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTurnGetDTO startGameTurn(@PathVariable("roomId") long gameId){

        GameTurn gameTurn = gameTurnService.startGameTurn(gameId);

        return DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);
    }


}
