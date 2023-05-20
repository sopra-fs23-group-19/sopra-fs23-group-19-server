package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
        gameTurn.setTargetWord(gameTurnInput.getTargetWord().toLowerCase());
        gameTurn.setStatus(TurnStatus.PAINTING);
        gameTurnRepository.saveAndFlush(gameTurn);
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

    public Room getRoom(Long roomId) {
        Room room = roomRepository.findByid(roomId);
        if(room == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, the room is not found!");
        }else{
            return room;
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

    // the drawing player finishes his/her drawing, and the guessing phase begins
    public GameTurn submitImage(GameTurnPutDTO gameTurnPutDTO) {
        GameTurn gameTurn = getGameTurn(gameTurnPutDTO.getId());
        gameTurn.setImage( gameTurnPutDTO.getImage());
        gameTurn.setStatus(TurnStatus.GUESSING);
        User drawingPlayer = getUser(gameTurn.getDrawingPlayerId());
        drawingPlayer.setCurrentScore(0);
        gameTurnRepository.flush();
        userRepository.flush();

        return gameTurn;
    }

    // calculate the score when each player handles his/her answer
    public void calculateScore(UserPutDTO userPutDTO, Long gameTurnId) {

        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        // find user in the db
        User user = getUser(userInput.getId());
        user.setGuessingWord(userInput.getGuessingWord());

        // find gameTurn info in the db
        GameTurn gameTurn = getGameTurn(gameTurnId);
        gameTurn.setSubmitNum(gameTurn.getSubmitNum()+1);
        Room room = getRoom(gameTurn.getRoomId());

        // find drawingPlayer
        User drawingPlayer = getUser(gameTurn.getDrawingPlayerId());

        String userGuess = userInput.getGuessingWord().toLowerCase();
        String target = gameTurn.getTargetWord().toLowerCase();
        double similarity = 0;
        //compute similarity, using the external API
        if(!userGuess.equals("") && userGuess != null && !target.equals("") && target!=null )
        {
        similarity = getWordSimilarity(userGuess, target );
        }
        if(userGuess.equals(target)){
            user.setCurrentScore(12);  // set current score in this game turn
            user.setCurrentGameScore(user.getCurrentGameScore()+12);  // total scores in this game accumulate
            user.setTotalScore(user.getTotalScore()+12);
            // set drawingPlayer's score
            if (room.getMode() == 4){  // 4-mode game
                drawingPlayer.setCurrentScore(drawingPlayer.getCurrentScore() + 4);
                drawingPlayer.setCurrentGameScore(drawingPlayer.getCurrentGameScore() + 4);
                drawingPlayer.setTotalScore(drawingPlayer.getTotalScore() + 4);
            }else{   // 2-mode game
                drawingPlayer.setCurrentScore(drawingPlayer.getCurrentScore() + 12);
                drawingPlayer.setCurrentGameScore(drawingPlayer.getCurrentGameScore() + 12);
                drawingPlayer.setTotalScore(drawingPlayer.getTotalScore() + 12);
            }
        }
        else if (similarity > 0.9) {
            user.setCurrentScore(6);  // set current score in this game turn
            user.setCurrentGameScore(user.getCurrentGameScore() + 6);  // total scores in this game accumulate
            user.setTotalScore(user.getTotalScore() + 6);
            // set drawingPlayer's score
            if (room.getMode() == 4) {  // 4-mode game
                drawingPlayer.setCurrentScore(drawingPlayer.getCurrentScore() + 2);
                drawingPlayer.setCurrentGameScore(drawingPlayer.getCurrentGameScore() + 2);
                drawingPlayer.setTotalScore(drawingPlayer.getTotalScore() + 2);
            }
            else { // 2-mode game
                drawingPlayer.setCurrentScore(drawingPlayer.getCurrentScore() + 6);
                drawingPlayer.setCurrentGameScore(drawingPlayer.getCurrentGameScore() + 6);
                drawingPlayer.setTotalScore(drawingPlayer.getTotalScore() + 6);
            }
        }
        else{
            user.setCurrentScore(0);
        }

        if(gameTurn.getSubmitNum() == roomRepository.findByid(gameTurn.getRoomId()).getMode()-1)
        {
            gameTurn.setStatus(TurnStatus.RANKING);
        }


        for(User u: userRepository.findByRoomId(gameTurn.getRoomId())){
            u.setConfirmRank(false);
        }

        userRepository.flush();
        gameTurnRepository.saveAndFlush(gameTurn);
        roomRepository.flush();
    }



    public List<User> rank(Long gameTurnId) {

    // rank all users by scores in this game turn

        GameTurn gameTurn = getGameTurn(gameTurnId);
        List<User> allPlayers = getAllUsers(gameTurn.getRoomId());

        Map<User,Integer> playersScores = new HashMap<>();
        for (User user: allPlayers){  // only rank guessingPlayers
            if (!user.getId().equals(gameTurn.getDrawingPlayerId())){
                playersScores.put(user, user.getCurrentScore());
            }
        }
        // rank the user by scores
        List<User> rankedUsers =  playersScores.entrySet().stream()
                .sorted((Map.Entry<User, Integer> e1, Map.Entry<User, Integer> e2) -> e2.getValue() - e1.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList());

        // set users' bestScore and totalScore
        for(Map.Entry<User,Integer> entry: playersScores.entrySet()){
            if(entry.getKey().getBestScore() < entry.getKey().getCurrentGameScore()){
                entry.getKey().setBestScore(entry.getKey().getCurrentGameScore());
            }
            userRepository.saveAndFlush(entry.getKey());
        }

        gameTurnRepository.flush();


        return rankedUsers;
    }


    public GameTurn confirmRank(Long turnId, Long userId){
        GameTurn gameTurn = getGameTurn(turnId);
        List<User> users = getAllUsers(getRoom(gameTurn.getRoomId()).getId());

        boolean allConfirm = true;
        for(User u: users){

            if(userId.equals(u.getId())){
                u.setConfirmRank(true);
            }

            if(u.isConfirmRank()!=true){
                allConfirm=false;
            }

        }

        if(allConfirm) {
            gameTurn.setStatus(TurnStatus.END);
            if (gameTurn.getCurrentTurn() == getRoom(gameTurn.getRoomId()).getMode()) {
                getRoom(gameTurn.getRoomId()).setStatus(RoomStatus.END_GAME);
            }
        }

        userRepository.flush();
        gameTurnRepository.flush();
        roomRepository.flush();

        return gameTurn;
    }
    // get the word similarity
    public double getWordSimilarity( String word0, String word1) {
//        System.out.println(word0);
//        System.out.println(word1);

        String requestContent = "https://twinword-text-similarity-v1.p.rapidapi.com/similarity/?text1="+
                URLEncoder.encode(word0, StandardCharsets.UTF_8).replace("+", "%20")+"&text2="+URLEncoder.encode(word1, StandardCharsets.UTF_8).replace("+", "%20");
//        System.out.println(requestContent);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestContent))
                .header("X-RapidAPI-Key", "f1ba58b38bmsh7f13519be0f7c4ep180d7cjsn1d03862b849f")
                .header("X-RapidAPI-Host", "twinword-text-similarity-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        {
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
            catch (IOException | InterruptedException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word similarity is not found.");
            }
        }

        String str = response.body().toString();
//        System.out.println(str);
        JSONObject result = new JSONObject(str);
        double similarity = 0;
        if (result.has("similarity")) {
             similarity = result.getDouble("similarity");

        }
        return similarity;
    }

}
