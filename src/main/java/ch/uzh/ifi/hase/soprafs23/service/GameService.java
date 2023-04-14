package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.*;
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


    public GameTurn startGameTurn(Room room) {

        // start a new game
        Game game = new Game();
        // set gameId == roomId
        game.setGameId(room.getId());
        // set turn length
        if(room.getMode() == RoomMode.P4){
            game.setTurnLength(4);
        }else{game.setTurnLength(2);}
        // set current turn index
        game.setCurrentGameTurn(1);

        // get all players in this room
        GameTurn gameTurn = new GameTurn();
        // set game turn list <gameTurnId1, gameTurnId2,...>
        game.setGameTurnList(Long.toString(gameTurn.getId()) + ",");

        // save all players' Ids
        gameTurn.setAllPlayersIds(room.getPlayers());
        gameTurn.setGameId(game.getGameId());
        List<Long> allPlayerIds = transferStringToLong(room.getPlayers());
        // find all players
        List<Optional<User>> allPlayers = new ArrayList<>();
        for (Long id: allPlayerIds){
            allPlayers.add(userRepository.findById(id));
        }

        // set drawing player and guessing players
        // select randomly
        Random random = new Random();
        int n = random.nextInt(allPlayers.size()-1);

        for(Long id: allPlayerIds){
            if(id == n){
                // set drawing player
                Optional<User> drawingPlayer = allPlayers.get(n);
                if(drawingPlayer.isPresent()){
                    gameTurn.setDrawingPlayer(id);
                    gameTurn.addPlayersScores(drawingPlayer.get(), 0);
                    game.setPlayersTotalScores(drawingPlayer.get(), 0);
                    drawingPlayer.get().setStatus(UserStatus.ISPLAYING);
                }
            }else{
                // set guessing players
                Optional<User> guessingPlayer = userRepository.findById(id);
                if(guessingPlayer.isPresent()) {
                    gameTurn.addPlayersScores(guessingPlayer.get(), 0);
                    game.setPlayersTotalScores(guessingPlayer.get(), 0);
                    guessingPlayer.get().setStatus(UserStatus.ISPLAYING);
                }
            }
        }
        log.debug("Created Information for a new Game: {}", game);
        return gameTurn;

    }

    public Room getRoom(long roomId) {
        return roomRepository.findById(roomId);
    }

    public Game getGame(long gameId) {
        return gameRepository.findById(gameId);
    }

    public GameTurn getGameTurn(long gameTurnId){
        return gameTurnRepository.findById(gameTurnId);
    }

    public List<Long> transferStringToLong(String players){
        String[] strArray = players.split(",");
        List<Long> longList = new ArrayList<>();

        for (String s : strArray) {
            longList.add(Long.valueOf(s));
        }

        return longList;
    }

    public GameTurn submitImage(GameTurn gameTurnInput) {

        // get current game turn
        Game game = getGame(gameTurnInput.getGameId());
        List<Long> gameTurnList = transferStringToLong(game.getGameTurnList());
        long currentTurnId;
        if (gameTurnList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the game has not started.");
        }
        else {
            currentTurnId = gameTurnList.get(gameTurnList.size() - 1);
        }
        // refresh current gameTurn status - add new image string
        GameTurn gameTurn = getGameTurn(currentTurnId);
        addImage(gameTurn, gameTurnInput.getImage());

        return gameTurn;
    }

    public void addImage (GameTurn gameTurn, String imageAdd){
        String image = gameTurn.getImage();
        if(image == null) {
            gameTurn.setImage(imageAdd);
        }else{
            gameTurn.setImage(image + imageAdd);
        }
    }
}

