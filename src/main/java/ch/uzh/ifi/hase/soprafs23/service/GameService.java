package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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

    private final GameRepository gameRepository;
    private final GameTurnRepository gameTurnRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("roomRepository") RoomRepository roomRepository) {
        this.gameRepository = gameRepository;
        this.gameTurnRepository = gameTurnRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    // initialize a new game
    public Game startGame(Room room) {
        // start a new game
        Game game = new Game();
        // set turn length
        game.setTurnLength(room.getMode());
        // set current turn index
        game.setCurrentGameTurn(1);
        // set all players ids
        Set<Long> allPlayerIds = new HashSet<>();
        for(Long id: room.getPlayers()){
            allPlayerIds.add(id);
        }
        game.setAllPlayersIds(allPlayerIds);

        game = gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    // start a new game turn
    public GameTurn startGameTurn(Room room) {

        // find the new game information
        Game game = startGame(room);
        // initialize a new game turn
        GameTurn gameTurn = new GameTurn();

        // save all players' Ids
        Set<Long> allPlayersIds = new HashSet<>();
        for(Long id: room.getPlayers()){
            allPlayersIds.add(id);
        }
        gameTurn.setAllPlayersIds(allPlayersIds);
        gameTurn.setGameId(game.getId());
        gameTurn.setDrawingPhase(true);  // drawing phase starts
        gameTurn.setGameTurnStatus(true);  // game turn starts
        gameTurn.setGameStatus(true);  // game starts
        // find all players
        List<User> allPlayers = new ArrayList<>();
        List<Long> allPlayerIds = new ArrayList<>(allPlayersIds);
        for (Long id: allPlayerIds){
            allPlayers.add(userRepository.findByid(id));
        }

        // set drawing player and guessing players
        // select randomly
        Random random = new Random();
        int n = random.nextInt(allPlayers.size());
        Long m = allPlayerIds.get(n);

        for(Long id: allPlayerIds){
            if(id == m){
                // set drawing player
                User drawingPlayer = allPlayers.get(n);
                if(drawingPlayer != null){
                    gameTurn.setDrawingPlayer(id);
                    game.setDrawingPlayerIds(id);
                    drawingPlayer.setCurrentScore(0);   // the score in this game turn
                    drawingPlayer.setCurrentGameScore(0);   // ths score in this game (include all game turns' scores)
                    drawingPlayer.setStatus(UserStatus.ISPLAYING);
                }else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
                }
            }else{
                // set guessing players
                User guessingPlayer = userRepository.findByid(id);
                if(guessingPlayer != null) {
                    guessingPlayer.setStatus(UserStatus.ISPLAYING);
                    guessingPlayer.setCurrentGameScore(0);  // ths score in this game (include all game turns' scores)
                }else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
                }
            }
        }
        gameTurnRepository.saveAndFlush(gameTurn);

        // set game turn list <gameTurnId1, gameTurnId2,...>
        game.setGameTurnList(gameTurn.getId());
        gameRepository.saveAndFlush(game);

        log.debug("Created Information for a new Game Turn: {}", gameTurn);
        return gameTurn;

    }

    public Room getRoom(Long roomId) {
        Room room = roomRepository.findByid(roomId);
        if(room == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the room is not found!");
        }else{
            return room;
        }
    }

    public Game getGame(Long gameId) {
        Game game = gameRepository.findByid(gameId);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the game has not started yet!");
        }else{
            return game;
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
    public List<User> rankAll(long gameId) {
        Game game = getGame(gameId);
        Set<Long> allPlayersIds = new HashSet<>();
        for(Long id: game.getAllPlayersIds()){
            allPlayersIds.add(id);
        }
        // find all players
        List<User> allPlayers = new ArrayList<>();
        for (Long id: allPlayersIds){
            allPlayers.add(getUser(id));
        }
        Map<User,Integer> playersScores = new HashMap<>();
        for (User user: allPlayers){
            playersScores.put(user, user.getCurrentGameScore());
        }
        // rank the user by scores
        List<User> rankedUsers =  playersScores.entrySet().stream()
                .sorted((Map.Entry<User, Integer> e1, Map.Entry<User, Integer> e2) -> e2.getValue() - e1.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());


        gameRepository.saveAndFlush(game);

        return rankedUsers;
    }

}
