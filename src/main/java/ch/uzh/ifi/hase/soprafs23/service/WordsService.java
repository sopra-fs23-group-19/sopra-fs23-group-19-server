package ch.uzh.ifi.hase.soprafs23.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class WordsService {

    private static final List<String> words = Arrays.asList(
            "snake","water","earth","apple","train","ship","teacher","hand","computer","cucumber",
            "potato","cow","basketball","plane","bicycle","guitar","car","leg","bread","powder",
            "flower","bat","king","phone","cake","pineapple","sun","eye","desk","rice",
            "sunglasses","egg","mountain","stone","restaurant","hospital","hook","mouth","bag","telescope");
    private static final Random rand = new Random();

    public String getWord() {
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
            catch (InterruptedException | IOException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word is not found.");
            }
        }
        String str = response.body();
        List<String> words = Arrays.asList(str.split("\""));
        String word = words.get(words.size()-2);
        word = word.replaceAll("[^A-Za-z]", "");
        return word.toLowerCase();
    }


    public Set<String> getThreeWords() {

        Set<String> listOfWords = new HashSet<>();
        for(int i=0; i<3; i++){
            listOfWords.add(getWord());
        }

        // if getting empty from external API
        if(listOfWords.size()<3){
            int leftNum = 3 - listOfWords.size();
            List<String> wordsAll = new ArrayList<>(words);
            // filter same words
            List<String> wordsBackup = wordsAll.stream()
                    .filter(item -> !listOfWords.contains(item))
                    .collect(toList());

            for(int i=0;i<leftNum;i++){
                int randomIndex = rand.nextInt(wordsBackup.size());
                listOfWords.add(wordsBackup.get(randomIndex));
                wordsBackup.remove(wordsBackup.get(randomIndex));
            }
        }
        return listOfWords;
    }

}
