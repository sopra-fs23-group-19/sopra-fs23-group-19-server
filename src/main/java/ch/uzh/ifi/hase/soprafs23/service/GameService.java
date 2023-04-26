package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
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
    private final GameTurnRepository gameTurnRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public GameService(
                       @Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("roomRepository") RoomRepository roomRepository) {
        this.gameTurnRepository = gameTurnRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    // initialize a new game
    // return the information of all turns
    public GameTurn startGame(Room room) {
        room.setStatus(RoomStatus.PLAYING);

        for(int i =0; i<room.getMode();i++){
            GameTurn gameTurn = new GameTurn();

            gameTurn.setRoomId(room.getId());
            gameTurn.setStatus(TurnStatus.CHOOSE_WORD);
            gameTurn.setCurrentTurn(i+1);
            gameTurn.setDrawingPlayerId(userRepository.findByRoomId(room.getId()).get(i).getId());
            userRepository.findByRoomId(room.getId()).get(i).setStatus(UserStatus.ISPLAYING);

            gameTurnRepository.saveAndFlush(gameTurn);
            userRepository.flush();
        }

        List<GameTurn> turns = gameTurnRepository.findByRoomId(room.getId());
        GameTurn result =  null;

        for(GameTurn t:turns){
            if(t.getCurrentTurn()==1){
                result = t;
            }
        }

        roomRepository.flush();

        return result;
    }


    public Room getRoom(Long roomId) {
        Room room = roomRepository.findByid(roomId);
        if(room == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the room is not found!");
        }else{
            return room;
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
            List<User> users = userRepository.findByRoomId(gameId);
            for (User user: users){
                user.setGuessingWord(null);
                user.setCurrentScore(0);
                user.setCurrentGameScore(0);
                user.setRoomId(null);
                user.setStatus(UserStatus.ONLINE);
                userRepository.saveAndFlush(user);
            }
        }
    }

    // rank all players in this game
    public List<User> rankAll(long gameId) { //find room
        Room room = roomRepository.findByid(gameId);
        // find all players
        List<User> allPlayers = userRepository.findByRoomId(room.getId());

        Map<User,Integer> playersScores = new HashMap<>();
        for (User user: allPlayers){
            playersScores.put(user, user.getCurrentGameScore());
        }
        // rank the user by scores
        List<User> rankedUsers =  playersScores.entrySet().stream()
                .sorted((Map.Entry<User, Integer> e1, Map.Entry<User, Integer> e2) -> e2.getValue() - e1.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());

        return rankedUsers;
    }

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
