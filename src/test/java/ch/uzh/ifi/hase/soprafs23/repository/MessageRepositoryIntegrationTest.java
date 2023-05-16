package ch.uzh.ifi.hase.soprafs23.repository;


import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MessageRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void findById_success() {
        // given
        Message message = new Message();
        message.setUseridFrom(1L);
        message.setUseridTo(2L);
        message.setRoomId(1L);
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.AGREE);
        entityManager.persist(message);
        entityManager.flush();

        // when
        Message found = messageRepository.findByid(message.getId());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUseridFrom(), message.getUseridFrom());
        assertEquals(found.getUseridTo(), message.getUseridTo());
        assertEquals(found.getType(), message.getType());
        assertEquals(found.getStatus(), message.getStatus());
        assertEquals(found.getRoomId(), message.getRoomId());
    }

    @Test
    public void findByUseridTo_success() {
        // given
        User userFrom = new User();
        userFrom.setPassword("111");
        userFrom.setUsername("hello");
        userFrom.setStatus(UserStatus.ONLINE);
        userFrom.setToken("1");
        entityManager.persist(userFrom);
        entityManager.flush();

        User userTo = new User();
        userTo.setPassword("222");
        userTo.setUsername("world");
        userTo.setStatus(UserStatus.ONLINE);
        userTo.setToken("2");
        entityManager.persist(userTo);
        entityManager.flush();

        Message message = new Message();
        message.setUseridFrom(1L);
        message.setUseridTo(2L);
        message.setRoomId(1L);
        message.setType(MessageType.GAME);
        message.setStatus(MessageStatus.AGREE);
        entityManager.persist(message);
        entityManager.flush();

        // when
        List<Message> messageList = messageRepository.findByUseridTo(userTo.getId());

        // then
        assertNotNull(messageList.get(0).getId());
        assertEquals(messageList.get(0).getUseridFrom(), message.getUseridFrom());
        assertEquals(messageList.get(0).getUseridTo(), message.getUseridTo());
        assertEquals(messageList.get(0).getType(), message.getType());
        assertEquals(messageList.get(0).getStatus(), message.getStatus());
        assertEquals(messageList.get(0).getRoomId(), message.getRoomId());

    }

}