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

}