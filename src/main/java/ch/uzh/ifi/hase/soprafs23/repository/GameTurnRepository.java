package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gameTurnRepository")
public interface GameTurnRepository extends JpaRepository<GameTurn, Long> {

    GameTurn findById(long id);

}
