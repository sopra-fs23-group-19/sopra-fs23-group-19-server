package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.interceptor.AuthenticationInterceptor;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserLoginPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.HttpHeaders;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Mock
    private AuthenticationInterceptor interceptor;
    @Mock
    private UserLoginToken userLoginToken;

    @BeforeEach
    public void setup() throws Exception {
        given(interceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).willReturn(true);
        given(userLoginToken.required()).willReturn(true);
        given(userService.findByToken(Mockito.any())).willReturn(true);
    }

    // login valid
    @Test
    public void login_valid() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserLoginPostDTO userPostDTO = new UserLoginPostDTO();
        userPostDTO.setPassword("Test User");
        userPostDTO.setUsername("testUsername");

        given(userService.login(Mockito.any())).willReturn(user);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO))
                .header(HttpHeaders.AUTHORIZATION,"qqqqqqq1");

        // then
        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }

    //login invalid
    @Test
    public void login_invalid_username() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserLoginPostDTO userPostDTO = new UserLoginPostDTO();
        userPostDTO.setPassword("Test User");
        userPostDTO.setUsername("testUsername");

        given(userService.login(Mockito.any())).willThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found."));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isNotFound());
    }

    //logout
    @Test
    public void logout_valid() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("12345678910");
        user.setStatus(UserStatus.ONLINE);

        doNothing().when(userService).logout(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/logout/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(postRequest).andExpect(status().isNoContent());
    }

    @Test
    public void logout_invalid() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logout failed."))
                .when(userService).logout(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/logout/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(postRequest).andExpect(status().isUnauthorized());
    }

    // get /users/{userId} ============================================================================================
    @Test
    public void userProfile_valid() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX",Locale.ENGLISH);
        Date q = df.parse("2023-03-06T20:19:12.560+00:00");
        // given
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
        user.setToken("1");
        //user.setBirthday(null);
        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.retrieveUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/"+user.getId()).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    // get /users/{userId} 4============================================================================================
    @Test
    public void userProfile_invalid() throws Exception {
        given(interceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).willReturn(true);
        given(userLoginToken.required()).willReturn(true);
        given(userService.findByToken(Mockito.any())).willReturn(true);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX",Locale.ENGLISH);
        Date q = df.parse("2023-03-06T20:19:12.560+00:00");
        // given
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        //user.setCreation_date(q);
        user.setId(1L);
        user.setToken("1");

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.retrieveUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"));

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/6774").contentType(MediaType.APPLICATION_JSON).header("Token","1").header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }


    // Test get /users interface and getUsers() in service
    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX",Locale.ENGLISH);
        Date q = df.parse("2023-03-06T20:19:12.560+00:00");
        String date = df.format(q);
        System.out.println(date);

        // given
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        //user.setCreation_date(q);
        user.setToken("1");
        //user.setBirthday(null);
        user.setId(1L);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));

    }


    // post /users 1====================================================================================================================
    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test User");
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO))
                .header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    // post /users 2===================================================================================================================
    @Test
    public void createUser_invalidInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test User");
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willThrow(
                new ResponseStatusException(HttpStatus.CONFLICT, "Failed because username already exists."));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO))
                .header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(postRequest).andExpect(status().isConflict());
    }


    // put /users/{userId} 5============================================================================================
    @Test
    public void updateProfile_valid() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX",Locale.ENGLISH);
        Date q = df.parse("2023-03-06T20:19:12.560+00:00");

        // given
        User user = new User();
        user.setId(1L);
        //user.setBirthday(null);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        //user.setCreation_date(q);

        UserPutDTO userPutDTO = new UserPutDTO();
        //userPutDTO.setBirthday(null);
        userPutDTO.setUsername("testUsername");

        doNothing().when(userService).updateUsernameOrPassword(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO)).header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(postRequest).andExpect(status().isNoContent());
    }


    // put /users/{userId} 6===========================================================================================
    @Test
    public void updateProfile_invalid() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX",Locale.ENGLISH);
        Date q = df.parse("2023-03-06T20:19:12.560+00:00");
        // given
        User user = new User();
        user.setId(1L);
        //user.setBirthday(null);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        //user.setCreation_date(q);
        user.setPassword("2");

        UserPutDTO userPutDTO = new UserPutDTO();
        //userPutDTO.setBirthday(null);
        userPutDTO.setUsername("testUsername");

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"))
                .when(userService).updateUsernameOrPassword(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/112318")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPutDTO)).header(HttpHeaders.AUTHORIZATION,"12345678910");

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

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
}
