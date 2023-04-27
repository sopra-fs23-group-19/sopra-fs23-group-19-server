//package ch.uzh.ifi.hase.soprafs23.service;
//
//import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
//import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs23.entity.Room;
//import ch.uzh.ifi.hase.soprafs23.entity.User;
//import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
//import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.web.server.ResponseStatusException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@WebAppConfiguration
//@SpringBootTest
//public class RoomServiceIntegrationTest {
//
//    @Qualifier("roomRepository")
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private RoomService roomService;
//
//    @Qualifier("userRepository")
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @BeforeEach
//    public void setup() {
//        roomRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    public void createRoom_success(){
//        User user = new User();
//        user.setPassword("Firstname Lastname");
//        user.setUsername("firstname@lastname");
//        User u1 = userService.createUser(user);
//
//        Room room = new Room();
//        room.setMode(2);
//        room.setRoomName("1");
//        room.setOwnerId(1L);
//
//        Room newRoom = roomService.createRoom(room);
//
//        assertEquals(1L,newRoom.getId());
//        assertEquals(room.getRoomName(),newRoom.getRoomName());
//        assertEquals(room.getMode(),newRoom.getMode());
//        assertEquals(room.getOwnerId(),newRoom.getOwnerId());
//        assertEquals(RoomStatus.WAITING,newRoom.getStatus());
//
//        System.out.println(u1.getRoomId());
//        assertEquals(newRoom.getOwnerId(),u1.getId());
//        assertEquals(newRoom.getId(),u1.getRoomId());
//    }
//
//    @Test
//    public void joinRoom_success(){
//        User user = new User();
//        user.setPassword("Firstname Lastname");
//        user.setUsername("firstname@lastname");
//        User u = userService.createUser(user);
//
//        Room room = new Room();
//        room.setMode(2);
//        room.setRoomName("1");
//        room.setOwnerId(1L);
//
//        Room createdRoom = roomService.createRoom(room);
//
//
//        User user2 = new User();
//        user2.setPassword("2");
//        user2.setUsername("2");
//        User u2 = userService.createUser(user);
//
//        Room newRoom = roomService.joinRoom(u2.getId(),createdRoom.getId());
//
//        assertEquals(createdRoom.getId(),newRoom.getId());
//        assertEquals(createdRoom.getRoomName(),newRoom.getRoomName());
//        assertEquals(createdRoom.getMode(),newRoom.getMode());
//        assertEquals(createdRoom.getOwnerId(),newRoom.getOwnerId());
//        assertEquals(createdRoom.getStatus(),newRoom.getStatus());
//
//        assertEquals(newRoom.getId(),u.getRoomId());
//    }
//}
