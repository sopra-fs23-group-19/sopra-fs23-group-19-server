package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(GameControllerTest.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @Test
    public void startGame_givenRoomId_returnTurnsInfo() throws Exception {
        // given
        Room room = new Room();
        room.setId(1L);
        room.setMode(2);
        room.setOwnerId(1L);

        GameTurn gameTurn = new GameTurn();
        gameTurn.setRoomId(1L);
        gameTurn.setDrawingPlayerId(1L);
        gameTurn.setId(1L);

        // given roomId
        given(gameService.startGame(Mockito.any())).willReturn(gameTurn);

        // when/then -> do the request
        MockHttpServletRequestBuilder postRequest = post("/games/waitingArea/"+room.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // performing request should return CREATED status
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(gameTurn.getId().intValue())))
                .andExpect(jsonPath("$.roomId", is(gameTurn.getRoomId().intValue())))
                .andExpect(jsonPath("$.drawingPlayerId", is(gameTurn.getDrawingPlayerId().intValue())));

    }

    @Test
    public void getGameRank_givenRoomId_returnRankList() throws Exception {
        // given
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
        given(gameService.rankAll(Mockito.anyLong())).willReturn(users);

        // when/then -> do the request
        MockHttpServletRequestBuilder getRequest = get("/games/ranks/"+ user1.getRoomId())
                .contentType(MediaType.APPLICATION_JSON);

        // performing request should return OK status
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

    }

    @Test
    public void quitGame_givenGameId_success() throws Exception {
        // given
        Room room = new Room();
        room.setId(1L);
        room.setMode(2);
        room.setOwnerId(1L);

        doNothing().when(gameService).endGame(Mockito.anyLong());

        // when/then -> do the request
        MockHttpServletRequestBuilder putRequest = put("/games/ending/"+room.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // performing request should be no_content status
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());

    }

    @Test
    public void getLeaderBoard_success() throws Exception{
        // given
        User user1 = new User();
        user1.setId(1L);
        user1.setRoomId(1L);
        user1.setUsername("a");
        user1.setTotalScore(12);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("b");
        user2.setRoomId(1L);
        user2.setTotalScore(24);
        List<User> users = new ArrayList<>(){{
            add(user1);
            add(user2);
        }};

        // given roomId
        given(gameService.getLeaderboardRank()).willReturn(users);

        // when/then -> do the request
        MockHttpServletRequestBuilder getRequest = get("/leaderboard")
                .contentType(MediaType.APPLICATION_JSON);

        // performing request should return OK status
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

    }


}