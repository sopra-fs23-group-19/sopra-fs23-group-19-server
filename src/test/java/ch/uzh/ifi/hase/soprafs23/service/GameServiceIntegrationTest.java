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

import static org.junit.jupiter.api.Assertions.*;

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
//        testUser1.setId(1L);
        testUser1.setUsername("hello");
        testUser1.setPassword("111");
        testUser1.setToken("1");
        createdUser1 = userService.createUser(testUser1);


        testUser2 = new User();
//        testUser2.setId(2L);
        testUser2.setUsername("world");
        testUser2.setPassword("111");
        testUser2.setToken("2");
        createdUser2 = userService.createUser(testUser2);

        testRoom = new Room();
//        testRoom.setId(1L);
        testRoom.setRoomName("hhh");
        testRoom.setOwnerId(createdUser1.getId());
        testRoom.setMode(2);
        createdRoom = roomService.createRoom(testRoom);
        createdRoom = roomService.joinRoom(createdUser2.getId(), createdRoom.getId());
        createdGameTurn1 = gameService.startGame(createdRoom);
    }

    @Test
    public void createGame_success() {

        assertNotNull(createdGameTurn1.getId());
        assertNotNull(createdGameTurn1.getRoomId());
    }

    @Test
    public void endGame_reset() {

        gameService.endGame(createdRoom.getId());

        assertNull(createdUser2.getRoomId());
        assertNull(createdUser2.getGuessingWord());
        assertEquals(0, createdUser2.getCurrentScore());
        assertEquals(0, createdUser2.getCurrentGameScore());
        assertEquals(UserStatus.ONLINE, createdUser2.getStatus());

    }

    @AfterEach
    public void cleanUp() {
        roomRepository.deleteAll();
        userRepository.deleteAll();
        gameTurnRepository.deleteAll();
    }
}