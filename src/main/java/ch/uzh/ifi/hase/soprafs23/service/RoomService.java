package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomAfterGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomGetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

        newRoom.setStatus(RoomStatus.WAITING);
        newRoom.getPlayers().add(newRoom.getOwnerId());

        newRoom = roomRepository.save(newRoom);
        roomRepository.flush();
        System.out.println("create----"+newRoom.getId());
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

    public Room joinRoom(Long userId, Long roomId){
        Room room = retrieveRoom(roomId);
        room.getPlayers().add(userId);

        roomRepository.flush();

        if(room.getMode()== room.getPlayers().size()){
            room.setStatus(RoomStatus.STARTING);
        }

        roomRepository.flush();

        return room;
    }

    public Room leaveRoom(Long userId, Long roomId){
        Room room = retrieveRoom(roomId);

        if(userId==room.getOwnerId()){
            room.setStatus(RoomStatus.QUIT);
        }else{
            for(Long uid: room.getPlayers()){
                if(userId==uid){
                    room.getPlayers().remove(userId);
                    room.setStatus(RoomStatus.WAITING);
                }
            }
        }

        return room;
    }

    public Room retrieveRoom(Long roomId){
        Room room = roomRepository.findByid(roomId);
        if (room == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room was not found");
        }
        return room;
    }

    public List<Room> getRooms() {
        return this.roomRepository.findAll();
    }

}
