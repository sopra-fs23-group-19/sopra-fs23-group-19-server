package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RoomService {
    private final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(@Qualifier("roomRepository") RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room newRoom) {
        checkIfRoomExists(newRoom);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newRoom = roomRepository.save(newRoom);
        roomRepository.flush();

        log.debug("Created Information for Room: {}", newRoom);
        return newRoom;
    }

    private void checkIfRoomExists(Room RoomToBeCreated) {
        Room roomByRoomName = roomRepository.findByRoomName(RoomToBeCreated.getRoomName());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the Room could not be created!";
        if (roomByRoomName != null ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "RoomName", "is"));
        }
    }

    public Room joinRoom(long userId, long roomId){
        Room room = roomRepository.findById(roomId);

        room.setPlayers(room.getPlayers()+String.valueOf(userId));
        return room;
    }

}
