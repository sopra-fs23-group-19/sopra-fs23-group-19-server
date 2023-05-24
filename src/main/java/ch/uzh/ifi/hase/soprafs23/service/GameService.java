package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.MessageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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
    private final GameTurnRepository gameTurnRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public GameService(
                       @Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("roomRepository") RoomRepository roomRepository,
                       @Qualifier("messageRepository") MessageRepository messageRepository) {
        this.gameTurnRepository = gameTurnRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
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
            User user = userRepository.findByRoomId(room.getId()).get(i);
            if (user == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Sorry, we cannot start, maybe some users have left this room?");
            }
            gameTurn.setDrawingPlayerId(user.getId());
            user.setStatus(UserStatus.ISPLAYING);

            gameTurnRepository.saveAndFlush(gameTurn);
            userRepository.flush();
        }

        List<GameTurn> turns = gameTurnRepository.findByRoomId(room.getId());
        if(turns == null || turns.size()==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is something wrong when starting a new game, " +
                    "this room will be dismissed; you can create a new room or join another room.");
        }
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

    public GameTurn getGameTurn(Long gameTurnId){
        GameTurn gameTurn = gameTurnRepository.findByid(gameTurnId);
        if(gameTurn == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Sorry, this turn does not exist, maybe you haven't started it yet?");
        }else{
            return gameTurn;
        }
    }

    public List<User> getAllUsers(Long gameId){
        List<User> users = userRepository.findByRoomId(gameId);
        if (users == null || users.size()==0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Sorry, there is something wrong when fetching users." +
                    "Maybe the gameId is not correct?");
        }else{
            return users;
        }
    }

    public void endGame(Long gameId) { //roomId
        Room room = getRoom(gameId);

        room.setStatus(RoomStatus.END_GAME);
        roomRepository.saveAndFlush(room);
        List<User> users = userRepository.findByRoomId(gameId);
        List<GameTurn> gameTurns = gameTurnRepository.findByRoomId(gameId);
        if(users != null && !users.isEmpty()){
            for (User user: users){
                user.setGuessingWord(null);
                user.setCurrentScore(0);
                user.setCurrentGameScore(0);
                user.setRoomId(null);
                user.setStatus(UserStatus.ONLINE);
                userRepository.saveAndFlush(user);
            }
        }
        List<Message> messages = messageRepository.findByRoomId(gameId);
        if (gameTurns != null && !gameTurns.isEmpty()){
            for (GameTurn gameTurn: gameTurns){
                gameTurnRepository.deleteByid(gameTurn.getId());
            }
            for(Message m:messages){
                messageRepository.deleteByid(m.getId());
            }
        }
    }

    // rank all players in this game
    public List<User> rankAll(Long gameId) { //find room
        Room room = getRoom(gameId);
        // find all players
        List<User> playersScores = getAllUsers(room.getId());

        Comparator<User> compareByGameScore = Comparator
                .comparing(User::getCurrentGameScore)
                .thenComparing(User::getId).reversed();

        Collections.sort(playersScores, compareByGameScore);
//        playersScores = playersScores.stream().limit(10).collect(Collectors.toList());

        Map<User,Integer> playersBestScores = new HashMap<>();
        for (User user: playersScores){
            playersBestScores.put(user, user.getCurrentGameScore());
        }

        for(Map.Entry<User,Integer> entry: playersBestScores.entrySet()){
            if(entry.getKey().getBestScore() < entry.getKey().getCurrentGameScore()){
                entry.getKey().setBestScore(entry.getKey().getCurrentGameScore());
            }
            userRepository.saveAndFlush(entry.getKey());
        }


        return playersScores;
    }

    public List<Long> getAllPlayersIds(Long id){ //turn id
        GameTurn gameTurn = getGameTurn(id);
        Room room  = getRoom(gameTurn.getRoomId());
        List<User> users = userRepository.findByRoomId(room.getId());

        List<Long> ids = new ArrayList<>();
        for(User u:users){
            ids.add(u.getId());
        }

        return ids;
    }

    public List<User> getLeaderboardRank() {
        List<User> playersScores = userRepository.findAll();
        if(playersScores.size()==0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is no ranking information.");
        }

        Comparator<User> compareByTotalScore = Comparator
                .comparing(User::getTotalScore)
                .thenComparing(User::getId).reversed();

        Collections.sort(playersScores, compareByTotalScore);
        playersScores = playersScores.stream().limit(10).collect(Collectors.toList());

        return playersScores;
    }
}
