package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
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

    public void updateImage(GameTurnPutDTO gameTurnPutDTO) {

        // refresh current gameTurn status - add new image string
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        String image = gameTurn.getImage();
        if(image == null) {
            gameTurn.setImage(gameTurnPutDTO.getImage());
        }else{
            gameTurn.setImage(image + gameTurnPutDTO.getImage());
        }
        gameTurnRepository.saveAndFlush(gameTurn);

    }


    public void setTargetWord(GameTurnPutDTO gameTurnPutDTO) {
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        GameTurn gameTurnInput = DTOMapper.INSTANCE.convertGameTurnPutDTOtoEntity(gameTurnPutDTO);
        gameTurn.setTargetWord(gameTurnInput.getTargetWord());
        gameTurnRepository.flush();
    }

    public Game getGame(long gameId){
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
        String image = gameTurn.getImage();
        if(image == null) {
            gameTurn.setImage(gameTurnPutDTO.getImage());
        }else{
            gameTurn.setImage(image + gameTurnPutDTO.getImage());
        }
        gameTurn.setDrawingPhase(false);
        gameTurnRepository.flush();
        return gameTurn;
    }

    public void calculateScore(UserPutDTO userPutDTO, long gameTurnId) {

        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        // find user in the db
        User user = getUser(userInput.getId());
        // find gameTurn info in the db
        GameTurn gameTurn = getGameTurn(gameTurnId);
        if(userInput.getGuessingWord() == gameTurn.getTargetWord()){
            user.setGuessingWord(userInput.getGuessingWord());
            user.setCurrentScore(1);
            user.setCurrentGameScore(user.getCurrentGameScore()+1);
        }else{
            user.setGuessingWord(userInput.getGuessingWord());
            user.setCurrentScore(0);
        }
        userRepository.saveAndFlush(user);
        gameTurnRepository.saveAndFlush(gameTurn);
    }

    public List<User> rank(long gameId, long gameTurnId) {
        GameTurn gameTurn = getGameTurn(gameTurnId);
        List<Long> allPlayerIds = transferStringToLong(gameTurn.getAllPlayersIds());
        // find all players
        List<User> allPlayers = new ArrayList<>();
        for (Long id: allPlayerIds){
            allPlayers.add(getUser(id));
        }
        Map<User,Integer> playersScores = new HashMap<>();
        for (User user: allPlayers){
            playersScores.put(user, user.getCurrentScore());
        }
        // rank the user by scores
        List<User> rankedUsers =  playersScores.entrySet().stream()
                .sorted((Map.Entry<User, Integer> e1, Map.Entry<User, Integer> e2) -> e2.getValue() - e1.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());

        // set users' bestScore and totalScore
        for(Map.Entry<User,Integer> entry: playersScores.entrySet()){
            entry.getKey().setTotalScore(entry.getKey().getTotalScore()+entry.getValue());
            if(entry.getKey().getBestScore() < entry.getValue()){
                entry.getKey().setBestScore(entry.getValue());
            }
        }
        // set game turn status
        gameTurn.setGameTurnStatus(false);

        // check game status
        Game game = getGame(gameId);
        if(game.getCurrentGameTurn() == game.getTurnLength()){
            gameTurn.setGameStatus(false);
        }
        gameTurnRepository.flush();

        return rankedUsers;
    }

    public GameTurn startNewGameTurn(long gameId) {
        Game game = getGame(gameId);
        game.setCurrentGameTurn(game.getCurrentGameTurn()+1);
        GameTurn gameTurn = new GameTurn();
        gameTurn.setAllPlayersIds(game.getAllPlayersIds());
        gameTurn.setGameId(game.getId());
        gameTurn.setDrawingPhase(true);
        gameTurn.setGameTurnStatus(true);

        List<Long> allPlayersIds = transferStringToLong(game.getAllPlayersIds());
        List<Long> drawingPlayerIds = transferStringToLong(game.getDrawingPlayerIds());
        List<Long> leftDrawingPlayerIds =  allPlayersIds.stream().filter(item -> !drawingPlayerIds.contains(item)).collect(Collectors.toList());

        // find all players
        List<Optional<User>> allPlayers = new ArrayList<>();
        for (Long id: allPlayersIds){
            allPlayers.add(userRepository.findById(id));
        }
        // set new drawingPlayer
        if(leftDrawingPlayerIds.size() == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, this game has ended!");
        }else if (leftDrawingPlayerIds.size() == 1){
            long drawingPlayerId = leftDrawingPlayerIds.get(0);
            for(Long id: allPlayersIds){
                if(id == drawingPlayerId){
                    // set drawing player
                    Optional<User> drawingPlayer = allPlayers.get(0);
                    if(drawingPlayer.isPresent()){
                        gameTurn.setDrawingPlayer(id);
                        game.setDrawingPlayerIds(Long.toString(id) + ",");
                        drawingPlayer.get().setCurrentScore(0);
                    }
                }else{
                    // set guessing players
                    Optional<User> guessingPlayer = userRepository.findById(id);
                    if(guessingPlayer.isPresent()) {
                        guessingPlayer.get().setCurrentScore(0);
                    }
                }
            }
        }else{
            // select randomly
            Random random = new Random();
            int n = random.nextInt(allPlayers.size());
            Long m = leftDrawingPlayerIds.get(n);
            for(Long id: allPlayersIds){
                if(id == m){
                    // set drawing player
                    Optional<User> drawingPlayer = allPlayers.get(n);
                    if(drawingPlayer.isPresent()){
                        gameTurn.setDrawingPlayer(id);
                        game.setDrawingPlayerIds(Long.toString(id) + ",");
                        drawingPlayer.get().setCurrentScore(0);
                    }
                }else{
                    // set guessing players
                    Optional<User> guessingPlayer = userRepository.findById(id);
                    if(guessingPlayer.isPresent()) {
                        guessingPlayer.get().setCurrentScore(0);
                    }
                }
            }
        }

        gameTurnRepository.saveAndFlush(gameTurn);

        // set game turn list <gameTurnId1, gameTurnId2,...>
        game.setGameTurnList(Long.toString(gameTurn.getId()) + ",");
        gameRepository.saveAndFlush(game);

        return gameTurn;
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
