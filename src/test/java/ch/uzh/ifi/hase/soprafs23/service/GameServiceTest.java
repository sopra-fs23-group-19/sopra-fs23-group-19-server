package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.MessageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameServiceTest {

    @Mock
    private GameTurnRepository gameTurnRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private GameService gameService;
    @InjectMocks
    private GameTurn testGameTurn1;
    @InjectMocks
    private GameTurn testGameTurn2;
    @InjectMocks
    private User testUser1;
    @InjectMocks
    private User testUser2;
    @InjectMocks
    private Room testRoom;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        // given
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("hello");
        testUser1.setPassword("111");
        testUser1.setToken("1");
        testUser1.setStatus(UserStatus.ISPLAYING);
        testUser1.setCurrentGameScore(2);
        testUser1.setTotalScore(12);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser1);

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("world");
        testUser2.setPassword("111");
        testUser2.setToken("2");
        testUser2.setStatus(UserStatus.ISPLAYING);
        testUser2.setCurrentGameScore(1);
        testUser2.setTotalScore(24);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser2);

        testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setRoomName("hhh");
        testRoom.setOwnerId(1L);
        testRoom.setStatus(RoomStatus.PLAYING);
        testRoom.setMode(2);
        Mockito.when(roomRepository.save(Mockito.any())).thenReturn(testRoom);

        testGameTurn1 = new GameTurn();
        testGameTurn1.setId(1L);
        testGameTurn1.setImage("aaa");
        testGameTurn1.setDrawingPlayerId(1L);
        testGameTurn1.setStatus(TurnStatus.CHOOSE_WORD);
        testGameTurn1.setSubmitNum(0);
        testGameTurn1.setCurrentTurn(1);
        testGameTurn1.setTargetWord("apple");
        testGameTurn1.setRoomId(1L);
        Mockito.when(gameTurnRepository.save(Mockito.any())).thenReturn(testGameTurn1);

        testGameTurn2 = new GameTurn();
        testGameTurn2.setId(2L);
        testGameTurn2.setImage("bbb");
        testGameTurn2.setDrawingPlayerId(2L);
        testGameTurn2.setStatus(TurnStatus.CHOOSE_WORD);
        testGameTurn2.setSubmitNum(0);
        testGameTurn2.setCurrentTurn(2);
        testGameTurn2.setTargetWord("apple");
        testGameTurn2.setRoomId(1L);
        Mockito.when(gameTurnRepository.save(Mockito.any())).thenReturn(testGameTurn2);

        // when -> any object is being saved in the repository -> return the dummy

    }

    @Test
    public void startGame_validInputs() {

        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};
        List<GameTurn> listOfGameTurns= new ArrayList<>() {{
            add(testGameTurn1);
            add(testGameTurn2);
        }};

        Mockito.when(userRepository.findByRoomId(testRoom.getId())).thenReturn(listOfUsers);
        Mockito.when(gameTurnRepository.findByRoomId(testRoom.getId())).thenReturn(listOfGameTurns);
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testUser1);
        Mockito.when(gameTurnRepository.saveAndFlush(Mockito.any())).thenReturn(testGameTurn1);
        Mockito.when(roomRepository.saveAndFlush(Mockito.any())).thenReturn(testRoom);

        // testGameTurn
        GameTurn createdGameTurn = gameService.startGame(testRoom);
        assertEquals(testGameTurn1, createdGameTurn);

    }

    @Test
    public void getRoom_notExists_throwsException() {
        Mockito.when(roomRepository.findByid(testRoom.getId())).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameService.getRoom(testRoom.getId()));
        String exceptionMessage = "Sorry, the room is not found!";
        assertEquals(exceptionMessage,exception.getReason());
    }

    @Test
    public void getGameRank_success() {

        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};

        Mockito.when(roomRepository.findByid(testRoom.getId())).thenReturn(testRoom);
        Mockito.when(userRepository.findByRoomId(testRoom.getId())).thenReturn(listOfUsers);

        List<User> rankedUserList = gameService.rankAll(testRoom.getId());
        assertEquals(testUser1.getId(), rankedUserList.get(0).getId());

    }

    @Test
    public void getAllPlayersIds_success() {
        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};

        Mockito.when(roomRepository.findByid(testRoom.getId())).thenReturn(testRoom);
        Mockito.when(gameTurnRepository.findByid(testRoom.getId())).thenReturn(testGameTurn1);
        Mockito.when(userRepository.findByRoomId(testRoom.getId())).thenReturn(listOfUsers);

        List<Long> allIds = gameService.getAllPlayersIds(testGameTurn1.getId());
        assertEquals(testUser1.getId(), allIds.get(0));
        assertEquals(testUser2.getId(), allIds.get(1));

    }

    @Test
    public void endGame_success() {
        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};

        Mockito.when(roomRepository.findByid(testRoom.getId())).thenReturn(testRoom);
        Mockito.when(userRepository.findByRoomId(testRoom.getId())).thenReturn(listOfUsers);
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testUser1);
        Mockito.when(roomRepository.saveAndFlush(Mockito.any())).thenReturn(testRoom);
        Mockito.when(messageRepository.findByRoomId(Mockito.any())).thenReturn(new ArrayList<>());

        gameService.endGame(testRoom.getId());
        gameTurnRepository.deleteByid(testGameTurn1.getId());
        assertEquals(null, testUser1.getGuessingWord());
        assertEquals(0, testUser1.getCurrentGameScore());
        assertEquals(0, testUser1.getCurrentScore());
        assertEquals(null, testUser1.getRoomId());
        assertEquals(UserStatus.ONLINE, testUser1.getStatus());

    }

    @Test
    public void endGame_roomNotExists_throwsException() {
        Mockito.when(roomRepository.findByid(testRoom.getId())).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameService.endGame(testRoom.getId()));
        String exceptionMessage = "Sorry, the room is not found!";
        assertEquals(exceptionMessage,exception.getReason());

    }

    @Test
    public void getGameTurn_notExists_throwsException() {
        Mockito.when(gameTurnRepository.findByid(testGameTurn1.getId())).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameService.getGameTurn(testGameTurn1.getId()));
        String exceptionMessage = "Sorry, this turn does not exist, maybe you haven't started it yet?";
        assertEquals(exceptionMessage,exception.getReason());
    }

    @Test
    public void getAllUsers_notExists_throwsException() {
        Mockito.when(userRepository.findByRoomId(testRoom.getId())).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameService.getAllUsers(testRoom.getId()));
        String exceptionMessage = "Sorry, there is something wrong when fetching users." +
        "Maybe the gameId is not correct?";
        assertEquals(exceptionMessage,exception.getReason());
    }

    @Test
    public void getLeaderBoard_success() {
        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};
        Mockito.when(userRepository.findAll()).thenReturn(listOfUsers);

        List<User> rankedUserList = gameService.getLeaderboardRank();

        assertEquals(testUser2.getId(), rankedUserList.get(0).getId());
    }


}