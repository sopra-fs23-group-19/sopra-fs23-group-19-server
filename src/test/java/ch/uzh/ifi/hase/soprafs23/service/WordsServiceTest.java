package ch.uzh.ifi.hase.soprafs23.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WordsServiceTest {

    @InjectMocks
    private WordsService wordsService;

    @BeforeEach
    void setup(){ MockitoAnnotations.openMocks(this);}

    @Test
    public void getWord_success() {

        String word = wordsService.getWord();
        assertNotNull(word);
    }

    @Test
    public void getThreeWords_success() {

        Set<String> words = wordsService.getThreeWords();
        assertNotNull(words);
    }

}