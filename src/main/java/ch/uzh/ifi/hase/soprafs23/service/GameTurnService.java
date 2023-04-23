package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
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
//        String image = gameTurn.getImage();
//        if(image == null) {
//            gameTurn.setImage(gameTurnPutDTO.getImage());
//        }else{
//            gameTurn.setImage(image + gameTurnPutDTO.getImage());
//        }
        gameTurn.setImage(gameTurnPutDTO.getImage());
        gameTurnRepository.saveAndFlush(gameTurn);

    }


    public void setTargetWord(GameTurnPutDTO gameTurnPutDTO) {
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        GameTurn gameTurnInput = DTOMapper.INSTANCE.convertGameTurnPutDTOtoEntity(gameTurnPutDTO);
        gameTurn.setTargetWord(gameTurnInput.getTargetWord());
        gameTurn.setStatus(TurnStatus.PAINITING);
        gameTurnRepository.flush();
    }

    public Game getGame(Long gameId){
        Game game = gameRepository.findByid(gameId);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, this game has not started yet!");
        }else{
            return game;
        }
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

    public GameTurn submitImage(GameTurnPutDTO gameTurnPutDTO) {
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
//        String image = gameTurn.getImage();
//        if(image == null) {
//            gameTurn.setImage(gameTurnPutDTO.getImage());
//        }else{
//            gameTurn.setImage(image + gameTurnPutDTO.getImage());
//        }

        gameTurn.setImage( gameTurnPutDTO.getImage());
//        gameTurn.setDrawingPhase(false);
        gameTurn.setStatus(TurnStatus.GUESSING);
        gameTurnRepository.flush();
        return gameTurn;
    }

    public void calculateScore(UserPutDTO userPutDTO, long gameTurnId) {

        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        // find user in the db
        User user = getUser(userInput.getId());
        // find gameTurn info in the db
        GameTurn gameTurn = getGameTurn(gameTurnId);
        if(userInput.getGuessingWord().equals(gameTurn.getTargetWord())){
            user.setGuessingWord(userInput.getGuessingWord());
            user.setCurrentScore(1);
            user.setCurrentGameScore(user.getCurrentGameScore()+1);
        }else{
            user.setGuessingWord(userInput.getGuessingWord());
            user.setCurrentScore(0);
        }
        gameTurn.setSubmittedAnswerIds(userInput.getId());
        userRepository.saveAndFlush(user);
        gameTurnRepository.saveAndFlush(gameTurn);
    }


    public List<User> rank(long gameTurnId) {
        GameTurn gameTurn = getGameTurn(gameTurnId);
        Set<Long> allPlayersIds = new HashSet<>();
        for(Long id: gameTurn.getAllPlayersIds()){
            allPlayersIds.add(id);
        }
        // find all players
        List<User> allPlayers = new ArrayList<>();
        for (Long id: allPlayersIds){
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
//         set game turn status
//        gameTurn.setGameTurnStatus(false);

        gameTurnRepository.flush();

        return rankedUsers;
    }


//    public GameTurn startNewGameTurn(long gameId) {
//
//        Game game = getGame(gameId);
//        game.setCurrentGameTurn(game.getCurrentGameTurn()+1);
//
//        Set<Long> allPlayerIds = new HashSet<>();
//        for(Long id: game.getAllPlayersIds()){
//            allPlayerIds.add(id);
//        }
//        Set<Long> drawingPlayersIds = new HashSet<>();
//        for(Long id: game.getDrawingPlayerIds()){
//            drawingPlayersIds.add(id);
//        }
//        GameTurn gameTurn = new GameTurn();
//        gameTurn.setAllPlayersIds(allPlayerIds);
//        gameTurn.setGameId(game.getId());
////        gameTurn.setDrawingPhase(true);
////        gameTurn.setGameTurnStatus(true);
////        gameTurn.setGameStatus(true);
//
//
//        List<Long> allPlayersIds = new ArrayList<>(allPlayerIds);
//        List<Long> drawingPlayerIds = new ArrayList<>(drawingPlayersIds);
//        List<Long> leftDrawingPlayerIds =  allPlayersIds.stream().filter(item -> !drawingPlayerIds.contains(item)).collect(Collectors.toList());
//
//        // find all players
//        List<User> allPlayers = new ArrayList<>();
//        for (Long id: allPlayersIds){
//            allPlayers.add(userRepository.findByid(id));
//        }
//        // set new drawingPlayer
//        if(leftDrawingPlayerIds.size() == 0){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, this game has ended!");
//        }else if (leftDrawingPlayerIds.size() == 1){
//            Long drawingPlayerId = leftDrawingPlayerIds.get(0);
//            for(Long id: allPlayersIds){
//                if(id == drawingPlayerId){
//                    // set drawing player
//                    User drawingPlayer = allPlayers.get(0);
//                    if(drawingPlayer != null){
//                        gameTurn.setDrawingPlayer(id);
//                        game.setDrawingPlayerIds(id);
//                        drawingPlayer.setCurrentScore(0);
//                    }else{
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                    }
//                }else{
//                    // set guessing players
//                    User guessingPlayer = userRepository.findByid(id);
//                    if(guessingPlayer != null) {
//                        guessingPlayer.setCurrentScore(0);
//                    }else{
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                    }
//                }
//            }
//        }else{
//            // select randomly
//            Random random = new Random();
//            int n = random.nextInt(allPlayers.size());
//            Long m = leftDrawingPlayerIds.get(n);
//            for(Long id: allPlayersIds){
//                if(id == m){
//                    // set drawing player
//                    User drawingPlayer = allPlayers.get(n);
//                    if(drawingPlayer != null){
//                        gameTurn.setDrawingPlayer(id);
//                        game.setDrawingPlayerIds(id);
//                        drawingPlayer.setCurrentScore(0);
//                    }else{
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                    }
//                }else{
//                    // set guessing players
//                    User guessingPlayer = userRepository.findByid(id);
//                    if(guessingPlayer != null) {
//                        guessingPlayer.setCurrentScore(0);
//                    }else{
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there is something wrong when starting a new game turn!");
//                    }
//                }
//            }
//        }
//
//        gameTurnRepository.saveAndFlush(gameTurn);
//
//        // set game turn list <gameTurnId1, gameTurnId2,...>
//        game.setGameTurnList(gameTurn.getId());
//        gameRepository.saveAndFlush(game);
//
//        return gameTurn;
//    }

}
