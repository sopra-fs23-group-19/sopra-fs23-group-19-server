package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.RoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;
    @MockBean
    private UserService userService;

    //create room
    @Test
    public void createRoom_valid() throws Exception{
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        RoomPostDTO roomPostDTO = new RoomPostDTO();
        roomPostDTO.setRoomName("1");
        roomPostDTO.setMode(2);
        roomPostDTO.setOwnerId(1L);

        given(roomService.createRoom(Mockito.any())).willReturn(room);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(roomPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(room.getId().intValue())))
                .andExpect(jsonPath("$.roomName", is(room.getRoomName())))
                .andExpect(jsonPath("$.roomSeats", is(room.getMode())))
                .andExpect(jsonPath("$.status", is(room.getStatus().toString())))
                .andExpect(jsonPath("$.ownerId", is(room.getOwnerId().intValue())));
    }

    // join room valid
    @Test
    public void joinRoom_valid() throws Exception {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        RoomPutDTO roomPutDTO = new RoomPutDTO();
        roomPutDTO.setRoomId(1L);
        roomPutDTO.setUserId(2L);

        given(roomService.joinRoom(Mockito.any(),Mockito.any())).willReturn(room);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = put("/rooms/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(roomPutDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(room.getId().intValue())))
                .andExpect(jsonPath("$.roomName", is(room.getRoomName())))
                .andExpect(jsonPath("$.roomSeats", is(room.getMode())))
                .andExpect(jsonPath("$.status", is(room.getStatus().toString())))
                .andExpect(jsonPath("$.ownerId", is(room.getOwnerId().intValue())));
    }

    //retrieve room
    @Test
    public void retrieveRoom_valid() throws Exception {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(roomService.retrieveRoom(Mockito.any())).willReturn(room);

        // when
        MockHttpServletRequestBuilder getRequest = get("/rooms/1").contentType(MediaType.APPLICATION_JSON)
                .header("Token","1");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(room.getId().intValue())))
                .andExpect(jsonPath("$.roomName", is(room.getRoomName())))
                .andExpect(jsonPath("$.roomSeats", is(room.getMode())))
                .andExpect(jsonPath("$.status", is(room.getStatus().toString())))
                .andExpect(jsonPath("$.ownerId", is(room.getOwnerId().intValue())));
    }

    @Test
    public void retrieveRoom_invalid() throws Exception {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(roomService.retrieveRoom(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Room was not found"));

        // when
        MockHttpServletRequestBuilder getRequest = get("/rooms/12").contentType(MediaType.APPLICATION_JSON)
                .header("Token","1");

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    //get all rooms
    @Test
    public void givenRooms_valid() throws Exception {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        List<Room> allRooms = Collections.singletonList(room);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(roomService.getAvailableRooms()).willReturn(allRooms);

        // when
        MockHttpServletRequestBuilder getRequest = get("/rooms").contentType(MediaType.APPLICATION_JSON)
                .header("Token","1");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(room.getId().intValue())))
                .andExpect(jsonPath("$[0].roomName", is(room.getRoomName())))
                .andExpect(jsonPath("$[0].roomSeats", is(room.getMode())))
                .andExpect(jsonPath("$[0].status", is(room.getStatus().toString())))
                .andExpect(jsonPath("$[0].ownerId", is(room.getOwnerId().intValue())));

    }

    //leave room
    @Test
    public void leaveRoom_valid() throws Exception {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        RoomPutDTO roomPutDTO = new RoomPutDTO();
        roomPutDTO.setRoomId(1L);
        roomPutDTO.setUserId(2L);

        given(roomService.leaveRoom(Mockito.any(),Mockito.any()))
                .willReturn(room);


        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        //user.setCreation_date(q);
        user.setId(1L);
        user.setToken("1");
        //user.setBirthday(null);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.retrieveUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = put("/rooms/leave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(roomPutDTO)).header("Token","1");

        // then
        mockMvc.perform(postRequest).andExpect(status().isOk());
    }


    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
