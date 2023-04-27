package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserNameDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserPostDTO;

import static org.junit.jupiter.api.Assertions.*;


/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */

class DTOMapperTest {

    @Test
    public void test_fromGameTurn_toGameTurnGetDTO_success(){
        // create gameTurn
        GameTurn gameTurn = new GameTurn();
        gameTurn.setId(1L);
        gameTurn.setStatus(TurnStatus.CHOOSE_WORD);

        // MAP -> create gameTurnGetDTO
        GameTurnGetDTO gameTurnGetDTO = DTOMapper.INSTANCE.convertEntityToGameTurnGetDTO(gameTurn);

        // check content
        assertEquals(gameTurn.getId(), gameTurnGetDTO.getId());
        assertEquals(gameTurn.getStatus(), gameTurnGetDTO.getStatus());
    }

    @Test
    public void test_fromGameTurnPutDTO_toGameTurn_success() {
        // create gameTurnPutDTO
        GameTurnPutDTO gameTurnPutDTO = new GameTurnPutDTO();
        gameTurnPutDTO.setId(1L);
        gameTurnPutDTO.setTargetWord("apple");

        // MAP -> create gameTurn
        GameTurn gameTurn = DTOMapper.INSTANCE.convertGameTurnPutDTOtoEntity(gameTurnPutDTO);

        // check
        assertEquals(gameTurnPutDTO.getId(), gameTurn.getId());
        assertEquals(gameTurnPutDTO.getTargetWord(), gameTurn.getTargetWord());

    }

    @Test
    public void test_fromUser_toUserNameDTO_success() {
        // create
        User user = new User();
        user.setId(1L);
        user.setUsername("hello");
        user.setToken("a");
        user.setPassword("111");
        user.setStatus(UserStatus.ONLINE);

        // MA
        UserNameDTO userNameDTO = DTOMapper.INSTANCE.convertEntityToUserNameDTO(user);

        // check
        assertEquals(user.getId(), userNameDTO.getId());
        assertEquals(user.getUsername(), userNameDTO.getUsername());

    }

        @Test
        public void testCreateUser_fromUserPostDTO_toUser_success() {
            // create UserPostDTO
            UserPostDTO userPostDTO = new UserPostDTO();
            userPostDTO.setPassword("name");
            userPostDTO.setUsername("username");

            // MAP -> Create user
            User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

            // check content
            // assertEquals(userPostDTO.getPassword(), user.get());
            assertEquals(userPostDTO.getUsername(), user.getUsername());
        }


    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        // assertEquals(user.getName(), userGetDTO.getName());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void room_success(){
        RoomPostDTO room = new RoomPostDTO();
        room.setMode(2);
        room.setRoomName("1");
        room.setOwnerId(1L);

        Room room1 = DTOMapper.INSTANCE.convertRoomPostDTOtoEntity(room);

        assertEquals(room.getRoomName(),room1.getRoomName());
        assertEquals(room.getMode(),room1.getMode());
        assertEquals(room.getOwnerId(),room1.getOwnerId());
    }

}