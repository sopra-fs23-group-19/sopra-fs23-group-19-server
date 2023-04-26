package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
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

class GameTurnServiceTest {

    @Mock
    private GameTurnRepository gameTurnRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private GameTurnService gameTurnService;
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
        testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setRoomName("hhh");
        testRoom.setOwnerId(1L);
        testRoom.setStatus(RoomStatus.END_GAME);
        testRoom.setMode(2);
        Mockito.when(roomRepository.save(Mockito.any())).thenReturn(testRoom);

        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("hello");
        testUser1.setPassword("111");
        testUser1.setToken("1");
        testUser1.setStatus(UserStatus.ISPLAYING);
        testUser1.setCurrentGameScore(2);
        testUser1.setCurrentScore(1);
        testUser1.setGuessingWord("apple");
        testUser1.setConfirmRank(true);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser1);

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("world");
        testUser2.setPassword("111");
        testUser2.setToken("2");
        testUser2.setStatus(UserStatus.ISPLAYING);
        testUser2.setCurrentGameScore(1);
        testUser2.setCurrentScore(0);
        testUser2.setGuessingWord("orange");
        testUser2.setConfirmRank(true);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser2);

        testGameTurn1 = new GameTurn();
        testGameTurn1.setId(1L);
        testGameTurn1.setImage("aaa");
        testGameTurn1.setDrawingPlayerId(1L);
        testGameTurn1.setStatus(TurnStatus.RANKING);
        testGameTurn1.setSubmitNum(0);
        testGameTurn1.setCurrentTurn(1);
        testGameTurn1.setTargetWord("apple");
        testGameTurn1.setRoomId(1L);
        Mockito.when(gameTurnRepository.save(Mockito.any())).thenReturn(testGameTurn1);

        testGameTurn2 = new GameTurn();
        testGameTurn2.setId(2L);
        testGameTurn2.setImage("bbb");
        testGameTurn2.setDrawingPlayerId(2L);
        testGameTurn2.setStatus(TurnStatus.RANKING);
        testGameTurn2.setSubmitNum(0);
        testGameTurn2.setCurrentTurn(2);
        testGameTurn2.setTargetWord("apple");
        testGameTurn2.setRoomId(1L);
        Mockito.when(gameTurnRepository.save(Mockito.any())).thenReturn(testGameTurn2);

    }

    @Test
    public void setTargetWord_success() {

        GameTurnPutDTO gameTurnPutDTO = new GameTurnPutDTO();
        gameTurnPutDTO.setTargetWord("apple");
        gameTurnPutDTO.setId(1L);

        Mockito.when(gameTurnRepository.findByid(testGameTurn1.getId())).thenReturn(testGameTurn1);
        Mockito.when(gameTurnRepository.saveAndFlush(Mockito.any())).thenReturn(testGameTurn1);

        gameTurnService.setTargetWord(gameTurnPutDTO);
        assertEquals(testGameTurn1.getTargetWord(), gameTurnPutDTO.getTargetWord());
        assertEquals(testGameTurn1.getId(), gameTurnPutDTO.getId());
        assertEquals(testGameTurn1.getStatus(), TurnStatus.PAINTING);
    }

    @Test
    public void updateImage_success() {

        GameTurnPutDTO gameTurnPutDTO = new GameTurnPutDTO();
        gameTurnPutDTO.setImage("aaa");
        gameTurnPutDTO.setId(1L);

        Mockito.when(gameTurnRepository.findByid(testGameTurn1.getId())).thenReturn(testGameTurn1);
        Mockito.when(gameTurnRepository.saveAndFlush(Mockito.any())).thenReturn(testGameTurn1);

        gameTurnService.updateImage(gameTurnPutDTO);
        assertEquals(testGameTurn1.getId(), gameTurnPutDTO.getId());
        assertEquals(testGameTurn1.getImage(), gameTurnPutDTO.getImage());

    }

    @Test
    public void getGameTurn_notExists_throwsException() {
        Mockito.when(gameTurnRepository.findByid(testGameTurn1.getId())).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameTurnService.getGameTurn(testGameTurn1.getId()));
        String exceptionMessage = "Sorry, this game turn has not started yet!";
        assertEquals(exceptionMessage,exception.getReason());
    }

    @Test
    public void getUser_notExists_throwsException() {
        Mockito.when(userRepository.findByid(testUser1.getId())).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameTurnService.getUser(testUser1.getId()));
        String exceptionMessage = "Sorry, the user is not found!";
        assertEquals(exceptionMessage,exception.getReason());
    }

    @Test
    public void submitImage_success() {

        GameTurnPutDTO gameTurnPutDTO = new GameTurnPutDTO();
        gameTurnPutDTO.setImage("aaa");
        gameTurnPutDTO.setId(1L);

        Mockito.when(gameTurnRepository.findByid(testGameTurn1.getId())).thenReturn(testGameTurn1);
        Mockito.when(gameTurnRepository.saveAndFlush(Mockito.any())).thenReturn(testGameTurn1);

        gameTurnService.submitImage(gameTurnPutDTO);
        assertEquals(testGameTurn1.getId(), gameTurnPutDTO.getId());
        assertEquals(testGameTurn1.getImage(), gameTurnPutDTO.getImage());
        assertEquals(testGameTurn1.getStatus(), TurnStatus.GUESSING);

    }

    @Test
    public void getGameTurnRank_success() {
        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};

        Mockito.when(gameTurnRepository.findByid(testGameTurn1.getId())).thenReturn(testGameTurn1);
        Mockito.when(userRepository.findByRoomId(testGameTurn1.getRoomId())).thenReturn(listOfUsers);
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testUser2);
        Mockito.when(gameTurnRepository.saveAndFlush(Mockito.any())).thenReturn(testGameTurn1);

        List<User> rankedUserList = gameTurnService.rank(testGameTurn1.getId());
        assertEquals(testUser2.getCurrentScore(), 0);
    }

    @Test
    public void calculateScore_user2_success() {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setId(2L);
        userPutDTO.setroomId(1L);
        userPutDTO.setGuessingWord("orange");

        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};

        Mockito.when(gameTurnRepository.findByid(testGameTurn1.getId())).thenReturn(testGameTurn1);
        Mockito.when(roomRepository.findByid(testGameTurn1.getRoomId())).thenReturn(testRoom);
        Mockito.when(userRepository.findByid(testUser2.getId())).thenReturn(testUser2);
        Mockito.when(userRepository.findByRoomId(testGameTurn1.getRoomId())).thenReturn(listOfUsers);
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testUser2);
        Mockito.when(gameTurnRepository.saveAndFlush(Mockito.any())).thenReturn(testGameTurn1);
        Mockito.when(roomRepository.saveAndFlush(Mockito.any())).thenReturn(testRoom);

        // then
        gameTurnService.calculateScore(userPutDTO, testGameTurn1.getId());
        assertEquals(testGameTurn1.getStatus(), TurnStatus.RANKING);
        assertEquals(testUser2.isConfirmRank(), false);
        assertEquals(testUser2.getCurrentScore(), 0);
        assertEquals(testUser2.getCurrentGameScore(), 1);
        assertEquals(testGameTurn1.getSubmitNum(), 1);

    }

    @Test
    public void calculateScore_user1_success() {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setId(1L);
        userPutDTO.setroomId(1L);
        userPutDTO.setGuessingWord("apple");

        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};

        Mockito.when(gameTurnRepository.findByid(testGameTurn2.getId())).thenReturn(testGameTurn2);
        Mockito.when(roomRepository.findByid(testGameTurn2.getRoomId())).thenReturn(testRoom);
        Mockito.when(userRepository.findByid(testUser1.getId())).thenReturn(testUser1);
        Mockito.when(userRepository.findByRoomId(testGameTurn2.getRoomId())).thenReturn(listOfUsers);
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testUser1);
        Mockito.when(gameTurnRepository.saveAndFlush(Mockito.any())).thenReturn(testGameTurn2);
        Mockito.when(roomRepository.saveAndFlush(Mockito.any())).thenReturn(testRoom);

        // then
        gameTurnService.calculateScore(userPutDTO, testGameTurn2.getId());
        assertEquals(testGameTurn2.getStatus(), TurnStatus.RANKING);
        assertEquals(testUser1.isConfirmRank(), false);
        assertEquals(testUser1.getCurrentScore(), 1);
        assertEquals(testUser1.getCurrentGameScore(), 3);
        assertEquals(testGameTurn2.getSubmitNum(), 1);

    }

    @Test
    public void confirmRank_success() {

        List<User> listOfUsers= new ArrayList<>() {{
            add(testUser1);
            add(testUser2);
        }};

        Mockito.when(gameTurnRepository.findByid(testGameTurn2.getId())).thenReturn(testGameTurn2);
        Mockito.when(roomRepository.findByid(testGameTurn2.getRoomId())).thenReturn(testRoom);
        Mockito.when(userRepository.findByid(testUser1.getId())).thenReturn(testUser1);
        Mockito.when(userRepository.findByRoomId(testGameTurn2.getRoomId())).thenReturn(listOfUsers);
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testUser1);
        Mockito.when(gameTurnRepository.saveAndFlush(Mockito.any())).thenReturn(testGameTurn2);
        Mockito.when(roomRepository.saveAndFlush(Mockito.any())).thenReturn(testRoom);

        gameTurnService.confirmRank(testGameTurn2.getId(), testUser1.getId());
        assertEquals(testGameTurn2.getStatus(), TurnStatus.END);
        assertEquals(testUser1.isConfirmRank(), true);
        assertEquals(testUser2.isConfirmRank(), true);
        assertEquals(testRoom.getStatus(), RoomStatus.END_GAME);

    }



}