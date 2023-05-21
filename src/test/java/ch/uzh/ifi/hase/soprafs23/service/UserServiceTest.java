package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void login_success(){
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        User createdUser = userService.login(user);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.any());

        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getPassword(), createdUser.getPassword());
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }


    @Test
    public void logout_success(){
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);

        userService.logout(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findByid(Mockito.any());

        assertEquals(UserStatus.OFFLINE, user.getStatus());
    }

    @Test
    public void retrieveUser_success(){
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.ONLINE);
        user.setId(1L);
        user.setToken("1");
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);

        User createdUser = userService.retrieveUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findByid(Mockito.any());

        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getPassword(), createdUser.getPassword());
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void update_success(){
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setPassword("1");
        userPutDTO.setUsername("1");

        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.ONLINE);
        user.setId(1L);
        user.setToken("1");
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        userService.updateUsernameOrPassword(userPutDTO);

        assertEquals("1", user.getPassword());
        assertEquals("1", user.getUsername());
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        // Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        // Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
        // doNothing().when(userService).checkIfUserExists(Mockito.any());
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void searchUsers_null_throwsException() {

        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setUsername("");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.searchUsers(userGetDTO));
        String exceptionMessage = "please input a valid username";
        assertEquals(exceptionMessage,exception.getReason());
    }

    @Test
    public void searchUsers_NotFound_throwsException() {

        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setUsername("name");

        assertThrows(ResponseStatusException.class, () -> userService.searchUsers(userGetDTO));
    }

    @Test
    public void searchUsers_success() {
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setUsername("testUsername");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        assertEquals(testUser.getUsername(), userGetDTO.getUsername());
    }

    @Test
    public void returnFriends_success() {
         User createdUser = new User();
         createdUser.setId(2L);
         createdUser.getFriends().add(testUser.getId());
         Mockito.when(userRepository.findByid(Mockito.anyLong())).thenReturn(createdUser);

         List<User> createdUserFriends = userService.returnFriends(createdUser.getId());

         assertEquals(1, createdUserFriends.get(0).getFriends().size());
    }


}
