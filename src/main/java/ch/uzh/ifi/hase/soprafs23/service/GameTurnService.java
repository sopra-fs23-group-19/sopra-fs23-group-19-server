package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameTurnService {
    private final RoomRepository roomRepository;
    private final GameTurnRepository gameTurnRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameTurnService(@Qualifier("roomRepository") RoomRepository roomRepository,
                           @Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository,
                           @Qualifier("userRepository")UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.gameTurnRepository = gameTurnRepository;
        this.userRepository = userRepository;
    }

    // save drawing player's image renewal
    public void updateImage(GameTurnPutDTO gameTurnPutDTO) {

        // refresh current gameTurn status - add new image string
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        gameTurn.setImage(gameTurnPutDTO.getImage());
        gameTurnRepository.saveAndFlush(gameTurn);

    }

    // save the target word in db
    public void setTargetWord(GameTurnPutDTO gameTurnPutDTO) {
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        GameTurn gameTurnInput = DTOMapper.INSTANCE.convertGameTurnPutDTOtoEntity(gameTurnPutDTO);
        gameTurn.setTargetWord(gameTurnInput.getTargetWord());
        gameTurn.setStatus(TurnStatus.PAINTING);
        gameTurnRepository.flush();
    }

    public GameTurn getGameTurn(Long gameTurnId){
        GameTurn gameTurn = gameTurnRepository.findByid(gameTurnId);
        if(gameTurn == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, this game turn has not started yet!");
        }else{
            return gameTurn;
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

    // the drawing player finishes his/her drawing, and the guessing phase begins
    public GameTurn submitImage(GameTurnPutDTO gameTurnPutDTO) {
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        gameTurn.setImage( gameTurnPutDTO.getImage());
        gameTurn.setStatus(TurnStatus.GUESSING);
        gameTurnRepository.flush();

        return gameTurn;
    }

    // calculate the score when each player handles his/her answer
    public void calculateScore(UserPutDTO userPutDTO, long gameTurnId) {

        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        // find user in the db
        User user = getUser(userInput.getId());
        user.setGuessingWord(userInput.getGuessingWord());

        // find gameTurn info in the db
        GameTurn gameTurn = getGameTurn(gameTurnId);
        gameTurn.setSubmitNum(gameTurn.getSubmitNum()+1);

        if(userInput.getGuessingWord().equals(gameTurn.getTargetWord())){
            user.setCurrentScore(1);  // set current score in this game turn
            user.setCurrentGameScore(user.getCurrentGameScore()+1);  // total scores in this game accumulate
        }else{
            user.setCurrentScore(0);
        }

        if(gameTurn.getSubmitNum() == roomRepository.findByid(gameTurn.getRoomId()).getMode()-1)
        {
            gameTurn.setStatus(TurnStatus.RANKING);
        }


        for(User u: userRepository.findByRoomId(gameTurn.getRoomId())){
            u.setConfirmRank(false);
        }

        userRepository.saveAndFlush(user);
        gameTurnRepository.saveAndFlush(gameTurn);
        roomRepository.flush();
    }



    public List<User> rank(long gameTurnId) {

    // rank all users by scores in this game turn

        GameTurn gameTurn = getGameTurn(gameTurnId);
        List<User> allPlayers = userRepository.findByRoomId(gameTurn.getRoomId());

        Map<User,Integer> playersScores = new HashMap<>();
        for (User user: allPlayers){
            if(user.getId() == gameTurn.getDrawingPlayerId()){
                user.setCurrentScore(0);
                playersScores.put(user, 0);
            }else{
                playersScores.put(user, user.getCurrentScore());
            }
        }
        // rank the user by scores
        List<User> rankedUsers =  playersScores.entrySet().stream()
                .sorted((Map.Entry<User, Integer> e1, Map.Entry<User, Integer> e2) -> e2.getValue() - e1.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());

        // set users' bestScore and totalScore
        for(Map.Entry<User,Integer> entry: playersScores.entrySet()){
            entry.getKey().setTotalScore(entry.getKey().getTotalScore()+entry.getValue());
            if(entry.getKey().getBestScore() < entry.getKey().getCurrentGameScore()){
                entry.getKey().setBestScore(entry.getKey().getCurrentGameScore());
            }
            userRepository.saveAndFlush(entry.getKey());
        }

        gameTurnRepository.flush();


        return rankedUsers;
    }


    public GameTurn confirmRank(long turnId, long userId){
        GameTurn gameTurn = gameTurnRepository.findByid(turnId);
        List<User> users = userRepository.findByRoomId(roomRepository.findByid(gameTurn.getRoomId()).getId());

        boolean allConfirm = true;
        for(User u: users){

            if(userId==u.getId()){
                u.setConfirmRank(true);
            }

            if(u.isConfirmRank()!=true){
                allConfirm=false;
            }

        }

        if(allConfirm) {
            gameTurn.setStatus(TurnStatus.END);
            if (gameTurn.getCurrentTurn() == roomRepository.findByid(gameTurn.getRoomId()).getMode()) {
                roomRepository.findByid(gameTurn.getRoomId()).setStatus(RoomStatus.END_GAME);
            }
        }

        userRepository.flush();
        gameTurnRepository.flush();
        roomRepository.flush();

        return gameTurn;
    }

}
