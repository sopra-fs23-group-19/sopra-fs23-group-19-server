package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoomService roomService;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);}

    @Test
    public void createRoom_success(){
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        Mockito.when(roomRepository.save(Mockito.any())).thenReturn(room);

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);

        Room newRoom = roomService.createRoom(room);
        Mockito.verify(userRepository, Mockito.times(2)).findByid(Mockito.any());
        Mockito.verify(roomRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(room.getId(),newRoom.getId());
        assertEquals(room.getRoomName(),newRoom.getRoomName());
        assertEquals(room.getMode(),newRoom.getMode());
        assertEquals(room.getOwnerId(),newRoom.getOwnerId());
        assertEquals(room.getStatus(),newRoom.getStatus());
    }

    @Test
    public void joinRoom_success(){
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);
        Mockito.when(roomRepository.findByid(Mockito.any())).thenReturn(room);

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findByRoomId(Mockito.any())).thenReturn(users);

        roomService.joinRoom(1L,1L);

        assertEquals(1L, user.getRoomId());
    }

    @Test
    public void leaveRoom_player_success(){
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(2L);
        room.setStatus(RoomStatus.WAITING);
        Mockito.when(roomRepository.findByid(Mockito.any())).thenReturn(room);

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");
        user.setRoomId(1L);
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findByRoomId(Mockito.any())).thenReturn(users);

        roomService.leaveRoom(1L,1L);

        assertEquals(null, user.getRoomId());
    }

    @Test
    public void leaveRoom_owner_success(){
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);
        Mockito.when(roomRepository.findByid(Mockito.any())).thenReturn(room);

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");
        user.setRoomId(1L);
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findByRoomId(Mockito.any())).thenReturn(users);

        roomService.leaveRoom(1L,1L);

        assertEquals(RoomStatus.END, room.getStatus());
    }

    @Test
    public void retrieveRoom_success() {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);
        Mockito.when(roomRepository.findByid(Mockito.any())).thenReturn(room);

        Room newRoom = roomService.retrieveRoom(1L);
        Mockito.verify(roomRepository, Mockito.times(1)).findByid(Mockito.any());

        assertEquals(room.getId(),newRoom.getId());
        assertEquals(room.getRoomName(),newRoom.getRoomName());
        assertEquals(room.getMode(),newRoom.getMode());
        assertEquals(room.getOwnerId(),newRoom.getOwnerId());
        assertEquals(room.getStatus(),newRoom.getStatus());
    }

    @Test
    public void availableRooms_success() {
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);
        List<Room> rooms = Collections.singletonList(room);
        Mockito.when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> r = roomService.getAvailableRooms();
        Mockito.verify(roomRepository, Mockito.times(1)).findAll();

        assertEquals(r, rooms);
    }

}
