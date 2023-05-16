package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.ConfirmMessageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.FriendMessagePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.GameMessagePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.MessageService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
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
    public void inviteGame_givenPostMessage_returnGetMessage() throws Exception{

        // given
        Message message = new Message();
        message.setUseridTo(2L);
        message.setUseridFrom(1L);
        message.setRoomId(1L);

        GameMessagePostDTO gameMessagePostDTO = new GameMessagePostDTO();
        gameMessagePostDTO.setRoomId(1L);
        gameMessagePostDTO.setUseridTo(2L);
        gameMessagePostDTO.setUseridFrom(1L);

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setRoomId(1L);
        messageGetDTO.setUseridTo(2L);
        messageGetDTO.setUseridFrom(1L);

        given(messageService.inviteGame(Mockito.any())).willReturn(message);
        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/notification/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameMessagePostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.useridTo", is(message.getUseridTo().intValue())))
                .andExpect(jsonPath("$.useridFrom", is(message.getUseridFrom().intValue())))
                .andExpect(jsonPath("$.roomId", is(message.getRoomId().intValue())));

    }

    @Test
    public void addFriends_givenFriendMessage_returnGetMessage() throws Exception{

        // given
        Message message = new Message();
        message.setUseridTo(2L);
        message.setUseridFrom(1L);

        FriendMessagePostDTO friendMessagePostDTO = new FriendMessagePostDTO();
        friendMessagePostDTO.setUseridTo(2L);
        friendMessagePostDTO.setUseridFrom(1L);

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setUseridTo(2L);
        messageGetDTO.setUseridFrom(1L);

        given(messageService.addFriend(Mockito.any())).willReturn(message);
        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);

        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/notification/friend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(friendMessagePostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.useridTo", is(message.getUseridTo().intValue())))
                .andExpect(jsonPath("$.useridFrom", is(message.getUseridFrom().intValue())));
    }

    @Test
    public void getGameMessages_givenUserId_returnMessageList() throws Exception{

        // given
        Message gameMessage1 = new Message();
        gameMessage1.setType(MessageType.GAME);
        gameMessage1.setUseridTo(2L);

        User user = new User();
        user.setId(1L);

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setUseridTo(2L);
        messageGetDTO.setUseridFrom(1L);
        messageGetDTO.setType(MessageType.GAME);

        List<Message> messages = new ArrayList<>(){{
            add(gameMessage1);
        }};

        given(messageService.getMessagesByUser(Mockito.anyLong())).willReturn(messages);
        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/notification/game/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].useridTo", is(gameMessage1.getUseridTo().intValue())))
                .andExpect(jsonPath("$[0].type", is(gameMessage1.getType().toString())));

    }

    @Test
    public void getFriendMessages_givenUserId_returnMessageList() throws Exception{

        // given
        Message gameMessage1 = new Message();
        gameMessage1.setType(MessageType.FRIEND);
        gameMessage1.setUseridTo(2L);

        User user = new User();
        user.setId(1L);

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setUseridTo(2L);
        messageGetDTO.setUseridFrom(1L);
        messageGetDTO.setType(MessageType.FRIEND);

        List<Message> messages = new ArrayList<>(){{
            add(gameMessage1);
        }};

        given(messageService.getMessagesByUser(Mockito.anyLong())).willReturn(messages);
        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/notification/friend/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].useridTo", is(gameMessage1.getUseridTo().intValue())))
                .andExpect(jsonPath("$[0].type", is(gameMessage1.getType().toString())));

    }

    @Test
    public void getGamePendingMessages_givenUserId_returnMessageList() throws Exception {

        // given
        Message gameMessage1 = new Message();
        gameMessage1.setType(MessageType.GAME);
        gameMessage1.setStatus(MessageStatus.PENDING);
        gameMessage1.setUseridTo(2L);

        User user = new User();
        user.setId(1L);

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setUseridTo(2L);
        messageGetDTO.setUseridFrom(1L);
        messageGetDTO.setStatus(MessageStatus.PENDING);
        messageGetDTO.setType(MessageType.GAME);

        List<Message> messages = new ArrayList<>(){{
            add(gameMessage1);
        }};

        given(messageService.getPendingMessages(Mockito.anyLong())).willReturn(messages);
        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/notification/game/pending/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].useridTo", is(gameMessage1.getUseridTo().intValue())))
                .andExpect(jsonPath("$[0].status", is(gameMessage1.getStatus().toString())))
                .andExpect(jsonPath("$[0].type", is(gameMessage1.getType().toString())));
    }

    @Test
    public void getFriendPendingMessages_givenUserId_returnMessageList() throws Exception {

        // given
        Message gameMessage1 = new Message();
        gameMessage1.setType(MessageType.FRIEND);
        gameMessage1.setStatus(MessageStatus.PENDING);
        gameMessage1.setUseridTo(2L);

        User user = new User();
        user.setId(1L);

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setUseridTo(2L);
        messageGetDTO.setUseridFrom(1L);
        messageGetDTO.setStatus(MessageStatus.PENDING);
        messageGetDTO.setType(MessageType.FRIEND);

        List<Message> messages = new ArrayList<>(){{
            add(gameMessage1);
        }};

        given(messageService.getPendingMessages(Mockito.anyLong())).willReturn(messages);
        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/notification/friend/pending/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].useridTo", is(gameMessage1.getUseridTo().intValue())))
                .andExpect(jsonPath("$[0].status", is(gameMessage1.getStatus().toString())))
                .andExpect(jsonPath("$[0].type", is(gameMessage1.getType().toString())));
    }

    @Test
    public void getMessageInfo_givenMessageId_returnInfo() throws Exception{

        // given
        Message message = new Message();
        message.setId(1L);

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setMessageId(1L);

        given(messageService.getMessageInfo(Mockito.anyLong())).willReturn(message);
        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/notification/game/information/"+message.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.messageId", is(message.getId().intValue())));
    }

    @Test
    public void confirmMessage_givenMessageInfo_returnMessage() throws Exception {

        // given
        Message message = new Message();
        message.setId(1L);

        ConfirmMessageDTO confirmMessageDTO = new ConfirmMessageDTO();
        confirmMessageDTO.setAction("AGREE");

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setMessageId(1L);
        messageGetDTO.setStatus(MessageStatus.AGREE);

        given(messageService.comfirmGame(Mockito.anyLong(), Mockito.any())).willReturn(message);
        given(messageService.completeReturnMessage(Mockito.any())).willReturn(messageGetDTO);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/notification/game/"+message.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(confirmMessageDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.messageId", is(messageGetDTO.getMessageId().intValue())))
                .andExpect(jsonPath("$.status", is(messageGetDTO.getStatus().toString())));
    }

    @Test
    public void refreshFriendMessages_givenMessageId_returnMessage() throws Exception{

        // given
        Message message = new Message();
        message.setId(1L);
        message.setUseridFrom(1L);
        message.setUseridTo(2L);

        ConfirmMessageDTO confirmMessageDTO = new ConfirmMessageDTO();
        confirmMessageDTO.setAction("AGREE");

        MessageGetDTO messageGetDTO = new MessageGetDTO();
        messageGetDTO.setMessageId(1L);
        messageGetDTO.setStatus(MessageStatus.AGREE);
        messageGetDTO.setUseridFrom(1L);
        messageGetDTO.setUseridTo(2L);

        given(messageService.comfirmGame(Mockito.anyLong(), Mockito.any())).willReturn(message);
        given(messageService.refreshFriends(Mockito.any())).willReturn(message);
        given(messageService.completeFriendsMessages(Mockito.any())).willReturn(messageGetDTO);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/friends/"+message.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(confirmMessageDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.messageId", is(messageGetDTO.getMessageId().intValue())))
                .andExpect(jsonPath("$.status", is(messageGetDTO.getStatus().toString())))
                .andExpect(jsonPath("$.useridFrom", is(messageGetDTO.getUseridFrom().intValue())))
                .andExpect(jsonPath("$.useridTo", is(messageGetDTO.getUseridTo().intValue())));
    }
}