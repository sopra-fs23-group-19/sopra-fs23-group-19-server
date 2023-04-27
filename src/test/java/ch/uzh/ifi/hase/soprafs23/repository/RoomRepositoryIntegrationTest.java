package ch.uzh.ifi.hase.soprafs23.repository;


import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RoomRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void findByid_success() {
        // given
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        entityManager.persist(room);
        entityManager.flush();

        // when
        Room newRoom = roomRepository.findByid(room.getId());

        // then
        assertNotNull(newRoom.getId());
        assertEquals(room.getId(),newRoom.getId());
        assertEquals(room.getRoomName(),newRoom.getRoomName());
        assertEquals(room.getMode(),newRoom.getMode());
        assertEquals(room.getOwnerId(),newRoom.getOwnerId());
        assertEquals(room.getStatus(),newRoom.getStatus());
    }

    @Test
    public void findByRoomName_success() {
        // given
        Room room = new Room();
        room.setMode(2);
        room.setId(1L);
        room.setRoomName("1");
        room.setOwnerId(1L);
        room.setStatus(RoomStatus.WAITING);

        entityManager.persist(room);
        entityManager.flush();

        // when
        Room newRoom = roomRepository.findByRoomName(room.getRoomName());

        // then
        assertNotNull(newRoom.getId());
        assertEquals(room.getId(),newRoom.getId());
        assertEquals(room.getRoomName(),newRoom.getRoomName());
        assertEquals(room.getMode(),newRoom.getMode());
        assertEquals(room.getOwnerId(),newRoom.getOwnerId());
        assertEquals(room.getStatus(),newRoom.getStatus());
    }
}
