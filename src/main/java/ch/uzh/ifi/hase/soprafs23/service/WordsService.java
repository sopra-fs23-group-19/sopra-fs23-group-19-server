package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class WordsService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameTurnRepository gameTurnRepository;

    @Autowired
    public WordsService(@Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository) {
        this.gameTurnRepository = gameTurnRepository;
    }

    public String getWord(){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://pictionary-charades-word-generator.p.rapidapi.com/pictionary?difficulty=easy"))
                .header("X-RapidAPI-Key", "f1ba58b38bmsh7f13519be0f7c4ep180d7cjsn1d03862b849f")
                .header("X-RapidAPI-Host", "pictionary-charades-word-generator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;

        {
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
            catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word is not found.");
            }
            catch (InterruptedException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word is not found.");
            }
        }
        String str = response.body();
        List<String> words = Arrays.asList(str.split("\""));
        return words.get(words.size()-2);
    }


    public Set<String> getThreeWords(){

        Set<String> listOfWords = new HashSet<>();
        for(int i=0; i<3; i++){
            listOfWords.add(getWord());
        }

        if(listOfWords.size()<3){
            listOfWords.add(getWord());
        }
        return listOfWords;
    }

}
