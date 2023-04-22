package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


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


    public Game startGame(Room room) {
        // start a new game
        Game game = new Game();
        // set turn length
        if(room.getMode() == RoomMode.P4){
            game.setTurnLength(4);
        }else{game.setTurnLength(2);}
        // set current turn index
        game.setCurrentGameTurn(1);
        // set all players ids
        game.setAllPlayersIds(room.getPlayers());

        game = gameRepository.save(game);
        userRepository.flush();

        return game;
    }
    public GameTurn startGameTurn(Room room) {

        Game game = startGame(room);
        // get all players in this room
        GameTurn gameTurn = new GameTurn();

        // save all players' Ids
        gameTurn.setAllPlayersIds(room.getPlayers());
        gameTurn.setGameId(game.getId());
        gameTurn.setDrawingPhase(true);
        gameTurn.setGameTurnStatus(true);
        List<Long> allPlayerIds = transferStringToLong(room.getPlayers());
        // find all players
        List<Optional<User>> allPlayers = new ArrayList<>();
        for (Long id: allPlayerIds){
            allPlayers.add(userRepository.findById(id));
        }

        // set drawing player and guessing players
        // select randomly
        Random random = new Random();
        int n = random.nextInt(allPlayers.size());
        Long m = allPlayerIds.get(n);

        for(Long id: allPlayerIds){
            if(id == m){
                // set drawing player
                Optional<User> drawingPlayer = allPlayers.get(n);
                if(drawingPlayer.isPresent()){
                    gameTurn.setDrawingPlayer(id);
                    game.setDrawingPlayerIds(Long.toString(id) + ",");
                    game.setPlayersTotalScores(drawingPlayer.get(), 0);
                    drawingPlayer.get().setStatus(UserStatus.ISPLAYING);
                }
            }else{
                // set guessing players
                Optional<User> guessingPlayer = userRepository.findById(id);
                if(guessingPlayer.isPresent()) {
                    game.setPlayersTotalScores(guessingPlayer.get(), 0);
                    guessingPlayer.get().setStatus(UserStatus.ISPLAYING);
                }
            }
        }
        gameTurnRepository.saveAndFlush(gameTurn);

        // set game turn list <gameTurnId1, gameTurnId2,...>
        game.setGameTurnList(Long.toString(gameTurn.getId()) + ",");
        gameRepository.saveAndFlush(game);

        log.debug("Created Information for a new Game Turn: {}", gameTurn);
        return gameTurn;

    }

    public Room getRoom(long roomId) {
        Room room = roomRepository.findById(roomId);
        if(room == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the room is not found!");
        }else{
            return room;
        }
    }

    public List<Long> transferStringToLong(String players){
        String[] strArray = players.split(",");
        List<Long> longList = new ArrayList<>();

        for (String s : strArray) {
            longList.add(Long.valueOf(s));
        }

        return longList;
    }


}

