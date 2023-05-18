package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameTurnRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final GameTurnRepository gameTurnRepository;

    @Autowired
    public RoomService(@Qualifier("roomRepository") RoomRepository roomRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("gameTurnRepository") GameTurnRepository gameTurnRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.gameTurnRepository = gameTurnRepository;
    }

    public Room createRoom(Room newRoom) {
        checkIfRoomExists(newRoom);
        // saves the given entity but data is only persisted in the database once
        // flush() is called

        newRoom.setStatus(RoomStatus.WAITING);

        if(newRoom.getOwnerId() == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        }

        if(userRepository.findByid(newRoom.getOwnerId())==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        }

        newRoom = roomRepository.save(newRoom);

        roomRepository.flush();

        userRepository.findByid(newRoom.getOwnerId()).setRoomId(newRoom.getId());
        userRepository.findByid(newRoom.getOwnerId()).setStatus(UserStatus.ONLINE);
        userRepository.flush();

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
        List<User> userList = userRepository.findByRoomId(roomId);

        if(room==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room doesn't exist!");
        }

        if(room.getMode() > userList.size()) {
            User user = userRepository.findByid(userId);

            if(user==null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist!");
            }

            user.setRoomId(roomId);

            if (room.getMode() == userRepository.findByRoomId(roomId).size()) {
                room.setStatus(RoomStatus.READY);
            }

            roomRepository.flush();
            userRepository.flush();

            return room;
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Room is full!");
        }
    }

    public Room leaveRoom(Long userId, Long roomId){
        Room room = retrieveRoom(roomId);

        if(userId.equals(room.getOwnerId())){
            room.setStatus(RoomStatus.END);
        }else{
            for(User user: userRepository.findByRoomId(roomId)){
                if(userId.equals(user.getId())){
                    user.setRoomId(null);
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

    public List<Room> getAvailableRooms(){
        List<Room> availableRooms = new ArrayList<>();
        List<Room> allRooms=this.roomRepository.findAll();
        for (Room room: allRooms){
            if(room.getStatus() == RoomStatus.WAITING)
            {
                if(userRepository.findByRoomId(room.getId())!=null){
                    if(userRepository.findByRoomId(room.getId()).size()!=0){
                        availableRooms.add(room);
                    }
                }
            }
        }
        return availableRooms;
    }

    public List<Long> getAllPlayersIds(Long id){ //turn id
        Room room  = roomRepository.findByid(id);

        List<User> users = userRepository.findByRoomId(room.getId());

        List<Long> ids = new ArrayList<>();

        for(User u:users){
            ids.add(u.getId());
        }

        return ids;
    }

    public List<GameTurn> getAllTurns(Long id){
        return gameTurnRepository.findByRoomId(id);
    }

}
