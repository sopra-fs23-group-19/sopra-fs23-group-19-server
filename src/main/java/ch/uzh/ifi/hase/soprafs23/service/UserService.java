package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    public String login(User loginUser){
        User userByUsername = userRepository.findByUsername(loginUser.getUsername());

        if(userByUsername==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found.");
        }

        if(userByUsername!=null && userByUsername.getPassword().equals(loginUser.getPassword())){
            userByUsername.setStatus(UserStatus.ONLINE);
            userByUsername.setToken(UUID.randomUUID().toString());
            return userByUsername.getToken();
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Wrong password.");
        }
    }

    public User logout(Long id){
        User user = userRepository.findByid(id);

        if(user!=null){
            user.setStatus(UserStatus.OFFLINE);
            user.setToken(null);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logout failed.");
        }
    }

    public void updateUser(Long userId, User newUser){
        checkIfUpdate(userId, newUser);
        User oldUser = retrieveUser(userId);
        if(oldUser!=null){
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(newUser.getPassword());
        }
    }

    public void checkIfUpdate(Long userId, User userToBeCreated) {
        User user = userRepository.findByid(userId);

        if(!Objects.equals(user.getUsername(), userToBeCreated.getUsername())) {
            if (userRepository.findByUsername(userToBeCreated.getUsername()) != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed because username already exists.");
            }
        }
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

}
