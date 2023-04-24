package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
//import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameGetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    //private final GameRepository gameRepository;
    private final GameTurnRepository gameTurnRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public GameService(
                       @Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("roomRepository") RoomRepository roomRepository) {
        //this.gameRepository = gameRepository;
        this.gameTurnRepository = gameTurnRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    // initialize a new game
    // return the information of first game turn
    public GameTurn startGame(Room room) {
        // start a new game
//        Game game = new Game();
//        // set turn length
//        game.setTurnLength(room.getMode());
//        game.setGameStatus(true);
//        // set current turn index
//        game.setCurrentGameTurn(1);
//        // set all players ids
//        Set<Long> allPlayerIds = new HashSet<>();
//        for(Long id: room.getPlayers()){
//            allPlayerIds.add(id);
//        }
//        game.setAllPlayersIds(allPlayerIds);
//
//        game = gameRepository.save(game);
//        gameRepository.flush();
//
//        return game;

        for(int i =0; i<room.getMode();i++){
            GameTurn gameTurn = new GameTurn();

            gameTurn.setRoomId(room.getId());
            gameTurn.setStatus(TurnStatus.CHOOSE_WORD);
            gameTurn.setCurrentTurn(i+1);
            gameTurn.setDrawingPlayerId(userRepository.findByRoomId(room.getId()).get(i).getId());

            gameTurnRepository.saveAndFlush(gameTurn);
        }

        List<GameTurn> turns = gameTurnRepository.findByRoomId(room.getId());
        GameTurn result =  null;

        for(GameTurn t:turns){
            if(t.getCurrentTurn()==1){
                result = t;
            }
        }

        return result;
    }



    // start a new game turn

//    public GameTurn startGameTurn(Room room) {
//
//        // find the new game information
//        Game game = startGame(room);
//        // initialize a new game turn
//        GameTurn gameTurn = new GameTurn();
//
//        // save all players' Ids
//        Set<Long> allPlayersIds = new HashSet<>();
//        for(Long id: room.getPlayers()){
//            allPlayersIds.add(id);
//        }
//        gameTurn.setAllPlayersIds(allPlayersIds);
//        gameTurn.setGameId(game.getId());
//
//
////        gameTurn.setDrawingPhase(true);
////        gameTurn.setGameTurnStatus(true);
////        gameTurn.setGameStatus(true);
//        //added by runze
//        gameTurn.setStatus(TurnStatus.CHOOSE_WORD);
//
//
//
//        // find all players
//        List<User> allPlayers = new ArrayList<>();
//        List<Long> allPlayerIds = new ArrayList<>(allPlayersIds);
//        for (Long id: allPlayerIds){
//            allPlayers.add(userRepository.findByid(id));
//        }
//
//        // set drawing player and guessing players
//        // select randomly
//        Random random = new Random();
//        int n = random.nextInt(allPlayers.size());
//        Long m = allPlayerIds.get(n);
//
//        for(Long id: allPlayerIds){
//            if(id == m){
//                // set drawing player
//                User drawingPlayer = allPlayers.get(n);
//                if(drawingPlayer != null){
//                    gameTurn.setDrawingPlayerId(id);
//                    game.setDrawingPlayerIds(id);
//                    drawingPlayer.setCurrentScore(0);   // the score in this game turn
//                    drawingPlayer.setCurrentGameScore(0);   // ths score in this game (include all game turns' scores)
//                    drawingPlayer.setStatus(UserStatus.ISPLAYING);
//                }else{
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                }
//            }else{
//                // set guessing players
//                User guessingPlayer = userRepository.findByid(id);
//                if(guessingPlayer != null) {
//                    guessingPlayer.setStatus(UserStatus.ISPLAYING);
//                    guessingPlayer.setCurrentGameScore(0);  // ths score in this game (include all game turns' scores)
//                }else{
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                }
//            }
//        }
//        gameTurnRepository.saveAndFlush(gameTurn);
//
//        // set game turn list <gameTurnId1, gameTurnId2,...>
//        game.setGameTurnList(gameTurn.getId());
//        game.setGameTurnStatus(gameTurn.getStatus());
////        game.setStatus(GameStatus.CHOOSE_WORD);
//        gameRepository.saveAndFlush(game);
//
//        log.debug("Created Information for a new Game Turn: {}", gameTurn);
//        return gameTurn;
//
//    }


//    public GameTurn startNewGameTurn(long gameId) {
//
//        Game game = gameRepository.findByid(gameId);
//        game.setCurrentGameTurn(game.getCurrentGameTurn()+1);
//
////        Room r =roomRepository.findByGameId(gameId);
////        for(Long id: r.getPlayers()){
////            allPlayerIds.add(id);
////        }
//
//        Set<Long> allPlayerIds = new HashSet<>();
//        for(Long id: game.getAllPlayersIds()){
//            allPlayerIds.add(id);
//        }
//        Set<Long> drawingPlayersIds = new HashSet<>();
//        for(Long id: game.getDrawingPlayerIds()){
//            drawingPlayersIds.add(id);
//        }
//        GameTurn gameTurn = new GameTurn();
//        gameTurn.setAllPlayersIds(allPlayerIds);
//        gameTurn.setGameId(game.getId());
//        gameTurn.setStatus(TurnStatus.CHOOSE_WORD);
////        gameTurn.setDrawingPhase(true);
////        gameTurn.setGameTurnStatus(true);
////        gameTurn.setGameStatus(true);
//
//
//        List<Long> allPlayersIds = new ArrayList<>(allPlayerIds);
//        List<Long> drawingPlayerIds = new ArrayList<>(drawingPlayersIds);
//        List<Long> leftDrawingPlayerIds =  allPlayersIds.stream().filter(item -> !drawingPlayerIds.contains(item)).collect(Collectors.toList());
//
//        // find all players
//        List<User> allPlayers = new ArrayList<>();
//        for (Long id: allPlayersIds){
//            allPlayers.add(userRepository.findByid(id));
//        }
//        // set new drawingPlayer
//        if(leftDrawingPlayerIds.size() == 0){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, this game has ended!");
//        }else if (leftDrawingPlayerIds.size() == 1){
//            Long drawingPlayerId = leftDrawingPlayerIds.get(0);
//            for(Long id: allPlayersIds){
//                if(id == drawingPlayerId){
//                    // set drawing player
//                    User drawingPlayer = allPlayers.get(0);
//                    if(drawingPlayer != null){
//                        gameTurn.setDrawingPlayerId(id);
//                        game.setDrawingPlayerIds(id);
//                        drawingPlayer.setCurrentScore(0);
//                    }else{
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                    }
//                }else{
//                    // set guessing players
//                    User guessingPlayer = userRepository.findByid(id);
//                    if(guessingPlayer != null) {
//                        guessingPlayer.setCurrentScore(0);
//                    }else{
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                    }
//                }
//            }
//        }else{
//            // select randomly
//            Random random = new Random();
//            int n = random.nextInt(allPlayers.size());
//            Long m = leftDrawingPlayerIds.get(n);
//            for(Long id: allPlayersIds){
//                if(id == m){
//                    // set drawing player
//                    User drawingPlayer = allPlayers.get(n);
//                    if(drawingPlayer != null){
//                        gameTurn.setDrawingPlayerId(id);
//                        game.setDrawingPlayerIds(id);
//                        drawingPlayer.setCurrentScore(0);
//                    }else{
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                    }
//                }else{
//                    // set guessing players
//                    User guessingPlayer = userRepository.findByid(id);
//                    if(guessingPlayer != null) {
//                        guessingPlayer.setCurrentScore(0);
//                    }else{
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                    }
//                }
//            }
//        }
//
//        gameTurnRepository.saveAndFlush(gameTurn);
//
//        // set game turn list <gameTurnId1, gameTurnId2,...>
//        game.setGameTurnList(gameTurn.getId());
//        game.setGameTurnStatus(gameTurn.getStatus());
//        gameRepository.saveAndFlush(game);
//
//        return gameTurn;
//    }


    public Room getRoom(Long roomId) {
        Room room = roomRepository.findByid(roomId);
        if(room == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the room is not found!");
        }else{
            return room;
        }
    }

//    public Game getGame(Long gameId) {
//        Game game = gameRepository.findByid(gameId);
//        if(game == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the game has not started yet!");
//        }
////        else if(!game.getGameStatus())
////        {
////            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the game has ended!");
////        }
//        else{
//            List<Long> turnIds =  game.getGameTurnList();
//            GameTurn recentTurn = getGameTurn(turnIds.get(turnIds.size() - 1));
//            game.setGameTurnStatus(recentTurn.getStatus());
////            List<TurnStatus> turnSt =  game.getGameTurnStatus();
//      //         check game status
//
//            if (recentTurn.getStatus() == TurnStatus.END)
//            {
//                rank(recentTurn.getId());
//                if(game.getCurrentGameTurn() == game.getTurnLength()){
//                    game.setGameStatus(false);
//                }
////                else{
////               startNewGameTurn(gameId);}
//            }
////            if (recentTurn.getStatus()== TurnStatus.END  )
////            {
////                rank(recentTurn.getId());
////                endTurn(recentTurn.getId());
////                game.setGameTurnSt   game.setGameTurnStatus(recentTurn.getStatus());atus(recentTurn.getStatus());
////            if(game.getCurrentGameTurn() == game.getTurnLength()){
////                game.setGameStatus(false);
////            }
//
////            }
//            return game;
//        }
//    }

    public GameTurn getGameTurn(Long gameTurnId) {
        GameTurn gameTn = gameTurnRepository.findByid(gameTurnId);
        if(gameTn == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the game turn information cannot be found!");
        }else{
            return gameTn;
        }
    }
    public void endGame( long gameId) { //roomId
        Room room = roomRepository.findByid(gameId);
        if(room == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the game is not found!");
        }
        else
        {
            room.setStatus(RoomStatus.END_GAME);
        }
    }

    public User getUser(Long userid){
        User user = userRepository.findByid(userid);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the user is not found!");
        }else{
            return user;
        }
    }

    // rank all players in this game
    public List<User> rankAll(long gameId) { //find room
        Room room = roomRepository.findByid(gameId);
//        Game game = getGame(gameId);
//        Set<Long> allPlayersIds = new HashSet<>();
//        for(Long id: game.getAllPlayersIds()){
//            allPlayersIds.add(id);
//        }
        // find all players
        List<User> allPlayers = userRepository.findByRoomId(room.getId());
//        for (Long id: allPlayersIds){
//            allPlayers.add(getUser(id));
//        }
        Map<User,Integer> playersScores = new HashMap<>();
        for (User user: allPlayers){
            playersScores.put(user, user.getCurrentGameScore());
        }
        // rank the user by scores
        List<User> rankedUsers =  playersScores.entrySet().stream()
                .sorted((Map.Entry<User, Integer> e1, Map.Entry<User, Integer> e2) -> e2.getValue() - e1.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());


        //gameRepository.saveAndFlush(game);

        return rankedUsers;
    }

//    public void endTurn (long gameTurnId)
//    {
//        GameTurn gameTurn = getGameTurn(gameTurnId);
////        gameTurn.setGameTurnStatus(false);
//        gameTurn.setStatus(TurnStatus.END);
//        gameTurnRepository.flush();
//    }

//    public List<User> rank( long gameTurnId) {
//        GameTurn gameTurn = getGameTurn(gameTurnId);
//        Set<Long> allPlayersIds = new HashSet<>();
//
//        for(Long id: gameTurn.getAllPlayersIds()){
//            allPlayersIds.add(id);
//        }
//        // find all players
//        List<User> allPlayers = new ArrayList<>();
//        for (Long id: allPlayersIds){
//            allPlayers.add(getUser(id));
//        }
//        Map<User,Integer> playersScores = new HashMap<>();
//        for (User user: allPlayers){
//            playersScores.put(user, user.getCurrentScore());
//        }
//        // rank the user by scores
//        List<User> rankedUsers =  playersScores.entrySet().stream()
//                .sorted((Map.Entry<User, Integer> e1, Map.Entry<User, Integer> e2) -> e2.getValue() - e1.getValue())
//                .map(entry -> entry.getKey()).collect(Collectors.toList());
//
//        // set users' bestScore and totalScore
//        for(Map.Entry<User,Integer> entry: playersScores.entrySet()){
//            entry.getKey().setTotalScore(entry.getKey().getTotalScore()+entry.getValue());
//            if(entry.getKey().getBestScore() < entry.getValue()){
//                entry.getKey().setBestScore(entry.getValue());
//            }
//        }
//        // set game turn status
////        gameTurn.setGameTurnStatus(false);
////        gameTurn.setStatus(TurnStatus.END);
////        // check game status
////        Game game = getGame(gameId);
////        if(game.getCurrentGameTurn() == game.getTurnLength()){
////            gameTurn.setGameStatus(false);
////        }
//        gameTurnRepository.flush();
//
//        return rankedUsers;
//    }


    public List<Long> getAllPlayersIds(Long id){ //turn id
        Room room  = roomRepository.findByid(gameTurnRepository.findByid(id).getRoomId());

        List<User> users = userRepository.findByRoomId(room.getId());

        List<Long> ids = new ArrayList<>();
        for(User u:users){
            ids.add(u.getId());
        }

        return ids;
    }

}
