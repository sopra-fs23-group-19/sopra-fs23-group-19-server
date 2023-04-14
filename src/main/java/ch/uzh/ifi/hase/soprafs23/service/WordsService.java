package ch.uzh.ifi.hase.soprafs23.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WordsService {

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://pictionary-charades-word-generator.p.rapidapi.com/pictionary"))
            .header("X-RapidAPI-Key", "SIGN-UP-FOR-KEY")
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


    public String getWord(){
        return response.body();  // retrieve "word"
    }

    public String getThreeWords(){

        String listOfWords = new String();
        for(int i=0; i<3; i++){
            listOfWords = listOfWords + getWord() + ",";
        }
        return listOfWords;
    }

}
