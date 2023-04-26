package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
class GameServiceIntegrationTest {
    @Qualifier("roomRepository")
    @Autowired
    private RoomRepository roomRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("gameTurnRepository")
    @Autowired
    private GameTurnRepository gameTurnRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    User testUser1;
    User testUser2;
    User createdUser1;
    User createdUser2;
    GameTurn createdGameTurn1;
    Room testRoom;
    Room createdRoom;


    @BeforeEach
    public void setup() {

        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("hello");
        testUser1.setPassword("111");
        testUser1.setToken("1");
        testUser1.setStatus(UserStatus.ISPLAYING);
        testUser1.setCurrentGameScore(2);
        testUser1.setCurrentScore(1);
        testUser1.setGuessingWord("apple");
        testUser1.setRoomId(1L);
        createdUser1 = userService.createUser(testUser1);


        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("world");
        testUser2.setPassword("111");
        testUser2.setToken("2");
        testUser2.setStatus(UserStatus.ISPLAYING);
        testUser2.setCurrentGameScore(1);
        testUser2.setCurrentScore(0);
        testUser2.setGuessingWord("orange");
        testUser2.setRoomId(1L);
        createdUser2 = userService.createUser(testUser2);

        testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setRoomName("hhh");
        testRoom.setOwnerId(1L);
        testRoom.setMode(2);
        createdRoom = roomService.createRoom(testRoom);
        createdRoom = roomService.joinRoom(createdUser2.getId(), createdRoom.getId());
        createdGameTurn1 = gameService.startGame(createdRoom);
    }

    @Test
    public void createGame_success() {

        assertEquals(1L, createdGameTurn1.getId());
        assertEquals(1L, createdGameTurn1.getRoomId());
    }

//    @Test
//    public void endGame_reset() {
//
//        gameService.endGame(createdRoom.getId());
//
//        assertNull(createdUser1.getRoomId());
//        assertNull(createdUser2.getRoomId());
//
//    }

    @AfterEach
    public void cleanUp() {
        roomRepository.deleteAll();
        userRepository.deleteAll();
        gameTurnRepository.deleteAll();
    }
}