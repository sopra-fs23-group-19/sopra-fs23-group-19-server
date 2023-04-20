package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameTurnService {
    private final GameRepository gameRepository;
    private final GameTurnRepository gameTurnRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameTurnService(@Qualifier("gameRepository") GameRepository gameRepository,
                           @Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository,
                           @Qualifier("userRepository")UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gameTurnRepository = gameTurnRepository;
        this.userRepository = userRepository;
    }

    public GameTurn updateImage(GameTurnPutDTO gameTurnPutDTO) {

        // refresh current gameTurn status - add new image string
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        String image = gameTurn.getImage();
        if(image == null) {
            gameTurn.setImage(gameTurnPutDTO.getImage());
        }else{
            gameTurn.setImage(image + gameTurnPutDTO.getImage());
        }
        gameTurnRepository.flush();

        return gameTurn;
    }


    public void setTargetWord(GameTurnPutDTO gameTurnPutDTO) {
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        GameTurn gameTurnInput = DTOMapper.INSTANCE.convertGameTurnPostDTOtoEntity(gameTurnPutDTO);
        gameTurn.setTargetWord(gameTurnInput.getTargetWord());
        gameTurnRepository.flush();
    }

    public List<Long> transferStringToLong(String players){
        String[] strArray = players.split(",");
        List<Long> longList = new ArrayList<>();

        for (String s : strArray) {
            longList.add(Long.valueOf(s));
        }

        return longList;
    }

    public GameTurn getGameTurn(long gameTurnId){
        GameTurn gameTurn = gameTurnRepository.findById(gameTurnId);
        if(gameTurn == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, this game turn has not started yet!");
        }else{
            return gameTurn;
        }
    }

    public User getUser(long userid){
        User user = userRepository.findById(userid);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the user is not found!");
        }else{
            return user;
        }
    }

    public GameTurn submitImage(GameTurnPutDTO gameTurnPutDTO) {
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        gameTurn.setDrawingPhase(false);
        gameTurnRepository.flush();
        return gameTurn;
    }

    public void calculateScore(UserPutDTO userPutDTO, long gameTurnId) {
        User user = getUser(userPutDTO.getId());
        GameTurn gameTurn = getGameTurn(gameTurnId);
        if(userPutDTO.getGuessingWord() == gameTurn.getTargetWord()){
            gameTurn.addPlayersScores(1, user);
            user.setCurrentScore(1);
        }else{
            gameTurn.addPlayersScores(0, user);
            user.setCurrentScore(0);
        }
    }

    public List<User> rank(long gameTurnId) {
        GameTurn gameTurn = getGameTurn(gameTurnId);
        Map<Integer, User> playersScores = gameTurn.getPlayersScores();
        // rank the user by scores
        Map<Integer, User> rankedMap = playersScores.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldV, newV) -> oldV, LinkedHashMap::new));

        List<User> rankedUsers = new ArrayList<>();
        rankedMap.forEach((key,value)->{
            rankedUsers.add(value);
        });
        return rankedUsers;
    }
}
