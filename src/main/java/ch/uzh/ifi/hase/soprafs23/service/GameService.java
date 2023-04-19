package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
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

        gameTurn = gameTurnRepository.save(gameTurn);
        userRepository.flush();

        // set game turn list <gameTurnId1, gameTurnId2,...>
        game.setGameTurnList(Long.toString(gameTurn.getId()) + ",");

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

    public Game getGame(long gameId) {
        Game game = gameRepository.findById(gameId);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, this game has not started yet!");
        }else{
            return game;
        }
    }

    public GameTurn getGameTurn(long gameTurnId){
        GameTurn gameTurn = gameTurnRepository.findById(gameTurnId);
        if(gameTurn == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, this game turn has not started yet!");
        }else{
            return gameTurn;
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

    public void setTargetWord(GameTurnPostDTO gameTurnPostDTO) {
        long id = gameTurnPostDTO.getGameId();
        GameTurn gameTurn = getGameTurn(id);
        GameTurn gameTurnInput = DTOMapper.INSTANCE.convertGameTurnPostDTOtoEntity(gameTurnPostDTO);
        gameTurn.setTargetWord(gameTurnInput.getTargetWord());
        gameTurnRepository.flush();
    }
}

