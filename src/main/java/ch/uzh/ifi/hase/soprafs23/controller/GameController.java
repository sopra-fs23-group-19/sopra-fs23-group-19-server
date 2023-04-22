//package ch.uzh.ifi.hase.soprafs23.controller;
//
//
//import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
//import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
//import ch.uzh.ifi.hase.soprafs23.entity.Room;
//import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
//import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
//import ch.uzh.ifi.hase.soprafs23.service.GameService;
//import ch.uzh.ifi.hase.soprafs23.service.WordsService;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
///**
// * User Controller This class is responsible for handling all REST request that are related to the
// * game logic. The controller will receive the request and delegate the execution to the GameService and
// * finally return the result.
// */
//
//@RestController
//public class GameController {
//
//    private final GameService gameService;
//
//    GameController(GameService gameService, WordsService wordsService) {
//
//        this.gameService = gameService;
//    }
//
////    @Autowired
////    SimpMessagingTemplate simpMessagingTemplate;
//
//
//    // start a new game
//    @UserLoginToken
//    @PostMapping("/games/gameRounds/{roomId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    public GameTurnGetDTO startGame(@PathVariable("roomId") Long roomId){
//        // start game turn
//        Room room = gameService.getRoom(roomId);
//        GameTurn gameTurn = gameService.startGameTurn(room);
//        // List<Long> playersIds = transferStringToLong(gameTurn.getAllPlayersIds());
//        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);
//
//        return gameTurnGetDTO;
//
////        for(int i=0; i<playersIds.size(); i++){
////            simpMessagingTemplate.convertAndSend("/game/startGame/"+ Long.toString(playersIds.get(i)), gameTurnGetDTO);
////        }
//    }
//
//
//}
