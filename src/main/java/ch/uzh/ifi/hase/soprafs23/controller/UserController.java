package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserLoginPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller This class is responsible for handling all REST request that are related to the
 * user. The controller will receive the request and delegate the execution to the UserService and
 * finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
    this.userService = userService;
    }

    @PostMapping("users/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String login(@RequestBody UserLoginPostDTO userLoginPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserLoginPostDTOtoEntity(userLoginPostDTO);
        // create user
        return userService.login(userInput);
    }

    @UserLoginToken
    @PostMapping("users/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logout(@RequestBody long id) {
        userService.logout(id);
    }


//      @GetMapping("/users")
//      @ResponseStatus(HttpStatus.OK)
//      @ResponseBody
//      public List<UserGetDTO> getAllUsers() {
//        // fetch all users in the internal representation
//        List<User> users = userService.getUsers();
//        List<UserGetDTO> userGetDTOs = new ArrayList<>();
//
//        // convert each user to the API representation
//        for (User user : users) {
//          userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
//        }
//        return userGetDTOs;
//      }


  @UserLoginToken
  @PostMapping("/users/register")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @UserLoginToken
  @PutMapping("/users/{userId}")
  //update user profile, return 204
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void updateProfile(@RequestBody UserPutDTO userPutDTO) {
      userService.updateUsernameOrPassword(userPutDTO);
  }

}
