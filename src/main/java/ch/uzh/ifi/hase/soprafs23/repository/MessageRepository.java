package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("messageRepository")
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findByid(Long id);

    List<Message> findByUseridTo(Long useridTo);

}
