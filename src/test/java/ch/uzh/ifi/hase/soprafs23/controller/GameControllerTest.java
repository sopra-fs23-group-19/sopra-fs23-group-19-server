package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnAfterGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameControllerTest.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

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
    public void startGame_givenRoomId_returnTurnsInfo() throws Exception {
        // given
        Room room = new Room();
        room.setId(1L);
        room.setMode(2);

        GameTurn gameTurn = new GameTurn();
        gameTurn.setRoomId(1L);
        gameTurn.setDrawingPlayerId(1L);
        gameTurn.setId(1L);

        GameTurnAfterGetDTO gameTurnAfterGetDTO = new GameTurnAfterGetDTO();
        gameTurnAfterGetDTO.setId(1L);
        gameTurnAfterGetDTO.setDrawingPlayerId(1L);
        gameTurnAfterGetDTO.setRoomId(1L);

        // given roomId
        given(gameService.startGame(Mockito.any())).willReturn(gameTurn);

        // when/then -> do the request
        MockHttpServletRequestBuilder postRequest = post("/games/waitingArea/"+room.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameTurnAfterGetDTO));

        // performing request should return CREATED status
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(gameTurn.getId().intValue())))
                .andExpect(jsonPath("$.roomId", is(gameTurn.getRoomId().intValue())))
                .andExpect(jsonPath("$.drawingPlayerId", is(gameTurn.getDrawingPlayerId().intValue())));

    }

    @Test
    void getGameRank() {
    }

    @Test
    void quitGame() {
    }

    @Test
    void changeGetToAfter() {
    }
}