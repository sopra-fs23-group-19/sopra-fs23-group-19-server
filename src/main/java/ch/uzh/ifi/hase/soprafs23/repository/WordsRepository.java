package ch.uzh.ifi.hase.soprafs23.repository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WordsRepository {

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
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public String[] getAllWords(){
        return new String[]{response.body()};
    }
}
