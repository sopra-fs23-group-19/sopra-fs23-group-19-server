package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GameTurnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordsService wordsService;

    @MockBean
    private UserService userService;

    @MockBean
    private GameTurnService gameTurnService;

    @MockBean
    private GameService gameService;

    @MockBean
    private RoomService roomService;

    /**
     * Helper Method to convert DTO into a JSON string such that the input
     * can be processed
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }

    @Test
    public void getThreeWords_givenGameTurnId_returnStrings() throws Exception {
        // given
        GameTurn gameTurn = new GameTurn();
        gameTurn.setId(1L);
        Set<String> words = Set.of("a", "b", "c");

        given(wordsService.getThreeWords()).willReturn(words);

        // when/then -> do the request
        MockHttpServletRequestBuilder getRequest = get("/gameRounds/words/"+gameTurn.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // performing request should be ok status
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

    }

    @Test
    public void setTargetWord_givenWord_success() throws Exception {
        // given
        GameTurnPutDTO gameTurnPutDTO = new GameTurnPutDTO();
        gameTurnPutDTO.setId(1L);
        gameTurnPutDTO.setImage("aaa");
        gameTurnPutDTO.setTargetWord("apple");

        doNothing().when(gameTurnService).setTargetWord(Mockito.any());

        // when/then -> do the request
        MockHttpServletRequestBuilder putRequest = put("/gameRounds/words")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameTurnPutDTO));

        // performing request should be no_content status
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());

    }

    @Test
    public void updateDrawing_givenImage_success() throws Exception {
        // given
        GameTurnPutDTO gameTurnPutDTO = new GameTurnPutDTO();
        gameTurnPutDTO.setId(1L);
        gameTurnPutDTO.setImage("aaa");

        doNothing().when(gameTurnService).updateImage(Mockito.any());

        // when/then -> do the request
        MockHttpServletRequestBuilder putRequest = put("/gameRounds/drawings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameTurnPutDTO));

        // performing request should be no_content status
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void submitDrawing_givenImage_returnImages() throws Exception {
        // given
        GameTurnPutDTO gameTurnPutDTO = new GameTurnPutDTO();
        gameTurnPutDTO.setId(1L);
        gameTurnPutDTO.setImage("aaa");

        GameTurn gameTurn = new GameTurn();
        gameTurn.setId(1L);
        gameTurn.setImage("bbbaaa");
        gameTurn.setRoomId(1L);

        given(gameTurnService.submitImage(Mockito.any())).willReturn(gameTurn);

        // when/then -> do the request
        MockHttpServletRequestBuilder postRequest = post("/gameRounds/finalDrawings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameTurnPutDTO));

        // performing request should return created status
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.image", Matchers.is(gameTurn.getImage())))
                .andExpect(jsonPath("$.id", Matchers.is(gameTurn.getId().intValue())))
                .andExpect(jsonPath("$.roomId", Matchers.is(gameTurn.getRoomId().intValue())));
    }

    @Test
    public void getGameTurnRank_givenTurnId_returnRank() throws Exception {
        // given
        GameTurn gameTurn = new GameTurn();
        gameTurn.setId(1L);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoomId(1L);
        user1.setUsername("a");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("b");
        user2.setRoomId(1L);
        List<User> users = new ArrayList<>(){{
            add(user1);
            add(user2);
        }};

        // given roomId
        given(gameTurnService.rank(Mockito.anyLong())).willReturn(users);

        // when/then -> do the request
        MockHttpServletRequestBuilder getRequest = get("/gameRounds/ranks/"+ gameTurn.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // performing request should return OK status
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void submitAnswer_givenAnswer_success() throws Exception {
        // given
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setId(1L);
        userPutDTO.setGuessingWord("orange");

        GameTurn gameTurn = new GameTurn();
        gameTurn.setId(1L);

        doNothing().when(gameTurnService).calculateScore(Mockito.any(), Mockito.anyLong());

        // when/then -> do the request
        MockHttpServletRequestBuilder putRequest = put("/gameRounds/answers/"+gameTurn.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        // performing request should be no_content status
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void getGameTurnInfo_givenGameTurnId_returnGameTurnInfo() throws Exception {
        // given
        GameTurn gameTurn = new GameTurn();
        gameTurn.setId(1L);
        gameTurn.setImage("bbbaaa");
        gameTurn.setRoomId(1L);

        given(gameTurnService.getGameTurn(Mockito.anyLong())).willReturn(gameTurn);

        // when/then -> do the request
        MockHttpServletRequestBuilder getRequest = get("/gameRounds/information/"+gameTurn.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // performing request should return ok status
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image", Matchers.is(gameTurn.getImage())))
                .andExpect(jsonPath("$.id", Matchers.is(gameTurn.getId().intValue())))
                .andExpect(jsonPath("$.roomId", Matchers.is(gameTurn.getRoomId().intValue())));
    }

    @Test
    public void confirmRank_givenId_success() throws Exception {
        // given
        GameTurn gameTurn = new GameTurn();
        gameTurn.setId(1L);
        gameTurn.setImage("bbbaaa");
        gameTurn.setRoomId(1L);

        User user = new User();
        user.setId(1L);
        user.setConfirmRank(true);
        user.setRoomId(1L);

        given(gameTurnService.confirmRank(Mockito.anyLong(), Mockito.anyLong())).willReturn(gameTurn);

        // when/then -> do the request
        MockHttpServletRequestBuilder getRequest = get("/gameRounds/rankConfirmation/"+gameTurn.getId()+"/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // performing request should return ok status
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image", Matchers.is(gameTurn.getImage())))
                .andExpect(jsonPath("$.id", Matchers.is(gameTurn.getId().intValue())))
                .andExpect(jsonPath("$.roomId", Matchers.is(gameTurn.getRoomId().intValue())));
    }


}