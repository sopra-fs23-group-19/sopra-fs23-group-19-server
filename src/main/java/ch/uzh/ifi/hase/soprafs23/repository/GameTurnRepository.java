package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("gameTurnRepository")
public interface GameTurnRepository extends JpaRepository<GameTurn, Long> {

    GameTurn findByid(Long id);

    List<GameTurn> findByRoomId(Long roomId);

    void deleteByid(Long id);

}
