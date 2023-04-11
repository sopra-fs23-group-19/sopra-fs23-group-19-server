package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    //private final GameTurnRepository gameTurnRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public GameService(//@Qualifier("gameRepository") GameTurnRepository gameTurnRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("roomRepository") RoomRepository roomRepository) {
        //this.gameTurnRepository = gameTurnRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }


    public GameTurn startGameTurn(Room room) {

        // start a new game
        Game game = new Game();
        game.setRoomId(room.getId());
        if(room.getMode() == RoomMode.P4){
            game.setTurnLength(4);
        }else{game.setTurnLength(2);}
        game.setCurrentGameTurn(1);

        // get all players in this room
        GameTurn gameTurn = new GameTurn();

        String gameTurnList = new String();
        gameTurnList = game.getGameTurnList() + Long.toString(gameTurn.getId()) + ",";
        game.setGameTurnList(gameTurnList);

        gameTurn.setAllPlayersIds(room.getPlayers());
        List<Long> allPlayerIds = transferStringToLong(room.getPlayers());
        List<Optional<User>> allPlayers = new ArrayList<>();
        for (Long id: allPlayerIds){
            allPlayers.add(userRepository.findById(id));
        }

        // set drawing player and guessing players
        // select random number
        Random random = new Random();
        int n = random.nextInt(allPlayers.size()-1);
        List<Long> leftDrawingPlayers = new ArrayList<>();

        for(Long id: allPlayerIds){
            if(id == n){
                Optional<User> drawingPlayer = allPlayers.get(n);
                if(drawingPlayer.isPresent()){
                    gameTurn.setDrawingPlayer(id);
                    gameTurn.addPlayersScores(drawingPlayer.get(), 0);
                    game.setPlayersTotalScores(drawingPlayer.get(), 0);
                    drawingPlayer.get().setStatus(UserStatus.ISPLAYING);
                }
            }else{
                Optional<User> guessingPlayer = userRepository.findById(id);
                if(guessingPlayer.isPresent()) {
                    gameTurn.addPlayersScores(guessingPlayer.get(), 0);
                    game.setPlayersTotalScores(guessingPlayer.get(), 0);
                    guessingPlayer.get().setStatus(UserStatus.ISPLAYING);
                }
            }
        }
        return gameTurn;

    }

    public Room getRoom(long roomId) {
        return roomRepository.findById(roomId);
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

