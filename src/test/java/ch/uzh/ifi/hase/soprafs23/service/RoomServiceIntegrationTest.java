package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;


@WebAppConfiguration
@SpringBootTest
public class RoomServiceIntegrationTest {

    @Qualifier("roomRepository")
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createRoom_success(){
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setRoomId(1L);
        User u1 = userService.createUser(user);

        Room room = new Room();
        room.setMode(2);
        room.setRoomName("1");
        room.setOwnerId(u1.getId());

        Room newRoom = roomService.createRoom(room);

        assertEquals(1L,newRoom.getId());
        assertEquals(room.getRoomName(),newRoom.getRoomName());
        assertEquals(room.getMode(),newRoom.getMode());
        assertEquals(room.getOwnerId(),newRoom.getOwnerId());
        assertEquals(RoomStatus.WAITING,newRoom.getStatus());

        assertEquals(newRoom.getOwnerId(),u1.getId());
        assertEquals(newRoom.getId(),u1.getRoomId());
    }

    @Test
    public void joinRoom_success(){
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setRoomId(1L);
        User u = userService.createUser(user);

        User user2 = new User();
        user2.setPassword("209");
        user2.setUsername("209");
        user2.setRoomId(1L);
        User u2 = userService.createUser(user2);

        Room room = new Room();
        room.setMode(4);
        room.setRoomName("1");
        room.setOwnerId(u.getId());

        Room createdRoom = roomService.createRoom(room);

        Room newRoom = roomService.joinRoom(u2.getId(),createdRoom.getId());

        assertEquals(createdRoom.getId(),newRoom.getId());
        assertEquals(createdRoom.getRoomName(),newRoom.getRoomName());
        assertEquals(createdRoom.getMode(),newRoom.getMode());
        assertEquals(createdRoom.getOwnerId(),newRoom.getOwnerId());
        assertEquals(createdRoom.getStatus(),newRoom.getStatus());
    }

    @Test
    public void leaveRoom_success(){
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setRoomId(1L);
        User u = userService.createUser(user);

        Room room = new Room();
        room.setMode(4);
        room.setRoomName("1");
        room.setOwnerId(u.getId());
        Room createdRoom = roomService.createRoom(room);

        Room newRoom = roomService.leaveRoom(u.getId(),createdRoom.getId());

        assertEquals(createdRoom.getId(),newRoom.getId());
        assertEquals(createdRoom.getRoomName(),newRoom.getRoomName());
        assertEquals(createdRoom.getMode(),newRoom.getMode());
        assertEquals(createdRoom.getOwnerId(),newRoom.getOwnerId());
        assertEquals(RoomStatus.END,newRoom.getStatus());
    }

    @Test
    public void retrieveRoom_success(){

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setRoomId(1L);
        User u = userService.createUser(user);

        Room room = new Room();
        room.setMode(4);
        room.setRoomName("1");
        room.setOwnerId(u.getId());
        Room createdRoom = roomService.createRoom(room);

        Room newRoom = roomService.retrieveRoom(createdRoom.getId());

        assertEquals(createdRoom.getId(),newRoom.getId());
        assertEquals(createdRoom.getRoomName(),newRoom.getRoomName());
        assertEquals(createdRoom.getMode(),newRoom.getMode());
        assertEquals(createdRoom.getOwnerId(),newRoom.getOwnerId());
        assertEquals(createdRoom.getStatus(),newRoom.getStatus());
    }
}
