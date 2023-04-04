package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
    }

    public User login(User loginUser){
        User userByUsername = userRepository.findByUsername(loginUser.getUsername());

        if(userByUsername==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found.");
        }

        if(userByUsername!=null && userByUsername.getPassword().equals(loginUser.getPassword())){
            userByUsername.setStatus(UserStatus.ONLINE);
            userByUsername.setToken(UUID.randomUUID().toString());
            return userByUsername;
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Wrong password.");
        }
    }

    public void logout(long id){
        User user = userRepository.findById(id);

        if(user!=null){
            user.setStatus(UserStatus.OFFLINE);
            user.setToken(null);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logout failed.");
        }
    }

    public User retrieveUser(long userId){
        User user =  userRepository.findById(userId);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");}
        return user;
    }

    public boolean findByToken(String token){
        if(userRepository.findByToken(token)==null){
            return false;
        }
        return true;
    }

    public List<User> getUsers() {
    return this.userRepository.findAll();
    }

    //  public User createUser(User newUser) {
    //    newUser.setToken(UUID.randomUUID().toString());
    //    newUser.setStatus(UserStatus.OFFLINE);
    //    checkIfUserExists(newUser);
    //    // saves the given entity but data is only persisted in the database once
    //    // flush() is called
    //    newUser = userRepository.save(newUser);
    //    userRepository.flush();
    //
    //    log.debug("Created Information for User: {}", newUser);
    //    return newUser;
    //  }


  public User createUser(User newUser) {
      newUser.setToken(UUID.randomUUID().toString());
	  newUser.setStatus(UserStatus.ONLINE);

      checkIfUserExists(newUser);
      checkNullPassword(newUser);
      
	  //create the creation date
	  Date today = Calendar.getInstance().getTime();
	  newUser.setCreationDate(today);

	  // saves the given entity but data is only persisted in the database once
	  // flush() is called
	  newUser = userRepository.save(newUser);
	  userRepository.flush();

	  log.debug("Created Information for User: {}", newUser);
	  return newUser;
  }
  
  private void checkNullPassword(User newUser) {
      if(newUser.getPassword() == null){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Password is Null"));
      }
}

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
	User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

	String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
	if (userByUsername != null ) {
	  throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
	}
  }

  public void updateUsernameOrPassword(UserPutDTO userPutDTO) {
    long userId =  userPutDTO.getId();
    User user = userRepository.findById(userId);

    if(user == null){
        // user with userId was not found, return 404
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
    
      // update the password
      if (!userInput.getPassword().equals("")) {
          // The password is the same as before, the user needs to enter a new one
          if(userInput.getPassword().equals(user.getPassword())) {
              throw new ResponseStatusException(HttpStatus.CONFLICT, "Password is the same as before, please enter a different one.");
          }else {
              user.setPassword(userInput.getPassword());
              userRepository.flush();
          }
      }
      // update username
      if (!userInput.getUsername().equals("")){
          User userByUsername = userRepository.findByUsername(userInput.getUsername());
          if(userByUsername != null) {
              // return 409: add User failed because username already exists
              throw new ResponseStatusException(HttpStatus.CONFLICT, "Username has already existed!");
          }else{
              // update the username
              user.setUsername(userInput.getUsername());
              // update the database
              userRepository.flush();
          }
      }
  }

}
