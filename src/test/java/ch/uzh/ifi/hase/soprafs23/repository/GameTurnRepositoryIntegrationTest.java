package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class GameTurnRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameTurnRepository gameTurnRepository;

    @Test
    public void findByid_success() {
        // given
        GameTurn gameTurn = new GameTurn();
        gameTurn.setImage("aaa");
        gameTurn.setDrawingPlayerId(1L);
        gameTurn.setStatus(TurnStatus.CHOOSE_WORD);
        gameTurn.setSubmitNum(0);
        gameTurn.setCurrentTurn(1);
        gameTurn.setTargetWord("apple");
        gameTurn.setRoomId(1L);
        entityManager.persist(gameTurn);
        entityManager.flush();

        // when
        GameTurn found = gameTurnRepository.findByid(gameTurn.getId());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getImage(), gameTurn.getImage());
        assertEquals(found.getDrawingPlayerId(), gameTurn.getDrawingPlayerId());
        assertEquals(found.getStatus(), gameTurn.getStatus());
        assertEquals(found.getSubmitNum(), gameTurn.getSubmitNum());
        assertEquals(found.getCurrentTurn(), gameTurn.getCurrentTurn());
        assertEquals(found.getTargetWord(), gameTurn.getTargetWord());
        assertEquals(found.getRoomId(), gameTurn.getRoomId());
    }

    @Test
    public void findByRoomId_success() {
        // given
        Room room = new Room();
        room.setOwnerId(1L);
        room.setRoomName("pig");
        room.setMode(2);
        entityManager.persist(room);
        entityManager.flush();

        GameTurn gameTurn1 = new GameTurn();
        gameTurn1.setImage("aaa");
        gameTurn1.setDrawingPlayerId(1L);
        gameTurn1.setStatus(TurnStatus.CHOOSE_WORD);
        gameTurn1.setSubmitNum(0);
        gameTurn1.setCurrentTurn(1);
        gameTurn1.setTargetWord("apple");
        gameTurn1.setRoomId(room.getId());

        GameTurn gameTurn2 = new GameTurn();
        gameTurn2.setImage("bbb");
        gameTurn2.setDrawingPlayerId(2L);
        gameTurn2.setStatus(TurnStatus.CHOOSE_WORD);
        gameTurn2.setSubmitNum(0);
        gameTurn2.setCurrentTurn(2);
        gameTurn2.setTargetWord("apple");
        gameTurn2.setRoomId(room.getId());
        entityManager.persist(gameTurn1);
        entityManager.persist(gameTurn2);
        entityManager.flush();

        // when
        List<GameTurn> found = gameTurnRepository.findByRoomId(room.getId());

        // then
        assertNotNull(found.get(0).getId());
        assertEquals(found.get(0).getImage(), gameTurn1.getImage());
        assertEquals(found.get(0).getDrawingPlayerId(), gameTurn1.getDrawingPlayerId());
        assertEquals(found.get(0).getStatus(), gameTurn1.getStatus());
        assertEquals(found.get(0).getSubmitNum(), gameTurn1.getSubmitNum());
        assertEquals(found.get(0).getCurrentTurn(), gameTurn1.getCurrentTurn());
        assertEquals(found.get(0).getTargetWord(), gameTurn1.getTargetWord());
        assertEquals(found.get(0).getRoomId(), gameTurn1.getRoomId());

        assertNotNull(found.get(1).getId());
        assertEquals(found.get(1).getImage(), gameTurn2.getImage());
        assertEquals(found.get(1).getDrawingPlayerId(), gameTurn2.getDrawingPlayerId());
        assertEquals(found.get(1).getStatus(), gameTurn2.getStatus());
        assertEquals(found.get(1).getSubmitNum(), gameTurn2.getSubmitNum());
        assertEquals(found.get(1).getCurrentTurn(), gameTurn2.getCurrentTurn());
        assertEquals(found.get(1).getTargetWord(), gameTurn2.getTargetWord());
        assertEquals(found.get(1).getRoomId(), gameTurn2.getRoomId());
    }

}