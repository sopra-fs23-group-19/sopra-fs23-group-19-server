//package ch.uzh.ifi.hase.soprafs23.service;
//
//import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
//import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//@Service
//@Transactional
//public class WordsService {
//
//    private final Logger log = LoggerFactory.getLogger(GameService.class);
//    private final GameTurnRepository gameTurnRepository;
//
//    @Autowired
//    public WordsService(@Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository) {
//        this.gameTurnRepository = gameTurnRepository;
//    }
//
//    public String getWord(){
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://pictionary-charades-word-generator.p.rapidapi.com/pictionary"))
//                .header("X-RapidAPI-Key", "SIGN-UP-FOR-KEY")
//                .header("X-RapidAPI-Host", "pictionary-charades-word-generator.p.rapidapi.com")
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//        HttpResponse<String> response;
//
//        {
//            try {
//                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//            }
//            catch (IOException e) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word is not found.");
//            }
//            catch (InterruptedException e) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word is not found.");
//            }
//        }
//        return response.body();
//    }
//
//
//    public String getThreeWords(){
//
//        String listOfWords = new String();
//        for(int i=0; i<3; i++){
//            listOfWords = listOfWords + getWord() + ",";
//        }
//        return listOfWords;
//    }
//
//
//    public GameTurn setThreeWords(Long gameTurnInputId) {
//        GameTurn gameTurn = gameTurnRepository.findByid(gameTurnInputId);
//        if(gameTurn == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This game turn is not found!");
//        }
//
//        gameTurn.setWordsToBeChosen(getThreeWords());
//        gameTurnRepository.flush();
//        return gameTurn;
//    }
//}
