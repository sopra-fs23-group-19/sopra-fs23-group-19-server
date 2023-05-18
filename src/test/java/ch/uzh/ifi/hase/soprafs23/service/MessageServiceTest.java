package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.MessageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.ConfirmMessageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.FriendMessagePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.MessageGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void inviteGame_success() {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");

        User user1 = new User();
        user1.setPassword("Firstname Lastname");
        user1.setUsername("firstname@lastname");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setId(2L);
        user1.setToken("1");

        Message message = new Message();
        message.setType(MessageType.FRIEND);
        message.setStatus(MessageStatus.PENDING);
        message.setId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);

        List<Message> messageList = new ArrayList<>();
        messageList.add(message);

        Mockito.when(messageRepository.findAll()).thenReturn(messageList);
        Mockito.when(roomRepository.findByid(Mockito.any())).thenReturn(room);
        Mockito.when(messageRepository.saveAndFlush(Mockito.any())).thenReturn(message);

        Message newMessage = messageService.inviteGame(message);
        Mockito.verify(messageRepository, Mockito.times(1)).findAll();
        Mockito.verify(messageRepository, Mockito.times(1)).saveAndFlush(Mockito.any());

        assertEquals(message.getId(), newMessage.getId());
        assertEquals(newMessage.getStatus(),MessageStatus.PENDING);
        assertEquals(newMessage.getUseridFrom(),message.getUseridFrom());
        assertEquals(newMessage.getUseridTo(),message.getUseridTo());
    }

    @Test
    public void inviteGame_conflict() {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");

        User user1 = new User();
        user1.setPassword("Firstname Lastname");
        user1.setUsername("firstname@lastname");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setId(2L);
        user1.setToken("1");

        Message message = new Message();
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.PENDING);
        message.setId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);

        List<Message> messageList = new ArrayList<>();
        messageList.add(message);

        Mockito.when(messageRepository.findAll()).thenReturn(messageList);
        Mockito.when(roomRepository.findByid(Mockito.any())).thenReturn(room);
        Mockito.when(messageRepository.saveAndFlush(Mockito.any())).thenReturn(message);

        assertThrows(ResponseStatusException.class, () -> messageService.inviteGame(message));
    }

    @Test
    public void confirmGame_notfound(){
        ConfirmMessageDTO confirmMessageDTO = new ConfirmMessageDTO();
        confirmMessageDTO.setAction("AGREE");
        Mockito.when(messageRepository.findByid(Mockito.any())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> messageService.comfirmGame(1L,confirmMessageDTO));
    }

    @Test
    public void confirmGame_succeed(){
        Message message = new Message();
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.AGREE);
        message.setId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);

        ConfirmMessageDTO confirmMessageDTO = new ConfirmMessageDTO();
        confirmMessageDTO.setAction("AGREE");
        Mockito.when(messageRepository.findByid(Mockito.any())).thenReturn(message);

        assertThrows(ResponseStatusException.class, () -> messageService.comfirmGame(1L,confirmMessageDTO));
    }

    @Test
    public void confirmFriend_succeed(){
        Message message = new Message();
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.AGREE);
        message.setId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);

        ConfirmMessageDTO confirmMessageDTO = new ConfirmMessageDTO();
        confirmMessageDTO.setAction("AGREE");
        Mockito.when(messageRepository.findByid(Mockito.any())).thenReturn(message);

        assertThrows(ResponseStatusException.class, () -> messageService.comfirmFriend(1L,confirmMessageDTO));
    }

    @Test
    public void getMessageByUser(){
        Message message = new Message();
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.PENDING);
        message.setId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        Mockito.when(messageRepository.findByUseridTo(Mockito.any())).thenReturn(messageList);

        Message newMessage = messageService.getMessagesByUser(1L).get(0);

        assertEquals(message.getId(), newMessage.getId());
        assertEquals(newMessage.getStatus(),MessageStatus.PENDING);
        assertEquals(newMessage.getUseridFrom(),message.getUseridFrom());
        assertEquals(newMessage.getUseridTo(),message.getUseridTo());
    }

    @Test
    public void getMessageInfo(){
        Message message = new Message();
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.PENDING);
        message.setId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);

        Mockito.when(messageRepository.findByid(Mockito.any())).thenReturn(message);

        Message newMessage = messageService.getMessageInfo(1L);

        assertEquals(message.getId(), newMessage.getId());
        assertEquals(newMessage.getStatus(),MessageStatus.PENDING);
        assertEquals(newMessage.getUseridFrom(),message.getUseridFrom());
        assertEquals(newMessage.getUseridTo(),message.getUseridTo());

    }

    @Test
    public void addFriend(){
        FriendMessagePostDTO friendMessagePostDTO = new FriendMessagePostDTO();
        friendMessagePostDTO.setUseridFrom(1L);
        friendMessagePostDTO.setUseridTo(2L);

        Message message = new Message();
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.PENDING);
        message.setId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        List<Long> longList = new ArrayList<>();
        Mockito.when(messageRepository.findAll()).thenReturn(messageList);

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.ONLINE);
        user.setId(1L);
        user.setToken("1");
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);

        Mockito.when(messageRepository.saveAndFlush(Mockito.any())).thenReturn(message);

        Message newMessage = messageService.addFriend(friendMessagePostDTO);

        assertEquals(message.getId(), newMessage.getId());
        assertEquals(newMessage.getStatus(),MessageStatus.PENDING);
        assertEquals(newMessage.getUseridFrom(),message.getUseridFrom());
        assertEquals(newMessage.getUseridTo(),message.getUseridTo());
    }

    @Test
    public void refreshFriend_success(){
        Message message = new Message();
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.AGREE);
        message.setId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");

        User user1 = new User();
        user1.setPassword("Firstname Lastname");
        user1.setUsername("firstname@lastname");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setId(2L);
        user1.setToken("1");

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);

        Message newMessage = messageService.refreshFriends(message);

        assertEquals(message.getId(), newMessage.getId());
        assertEquals(newMessage.getStatus(),MessageStatus.AGREE);
        assertEquals(newMessage.getUseridFrom(),message.getUseridFrom());
        assertEquals(newMessage.getUseridTo(),message.getUseridTo());
    }

    @Test
    public void completeFriendsMessage_success(){
        MessageGetDTO message = new MessageGetDTO();
        message.setMessageId(1L);
        message.setUseridTo(1L);
        message.setUseridFrom(2L);
        message.setRoomId(1L);

        User user1 = new User();
        user1.setPassword("Firstname Lastname");
        user1.setUsername("firstname@lastname");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setId(2L);
        user1.setToken("1");

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user1);

        MessageGetDTO newMessage = messageService.completeReturnMessage(message);

        assertEquals(message.getMessageId(), newMessage.getMessageId());
        assertEquals(newMessage.getUseridFrom(),message.getUseridFrom());
        assertEquals(newMessage.getUseridTo(),message.getUseridTo());
    }

}
