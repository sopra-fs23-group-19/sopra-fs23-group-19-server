package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomAfterGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.RoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomController {
    private final RoomService roomService;
    private final UserService userService;
    RoomController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    // owner doesn't need to join the room after creating
    @UserLoginToken
    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RoomAfterGetDTO createRoom(@RequestBody RoomPostDTO roomPostDTO) {
        // convert API user to internal representation
        return changeRoomToAfter(
                roomService.createRoom(
                        DTOMapper.INSTANCE.convertRoomPostDTOtoEntity(roomPostDTO)
                )
        );

    }

    @UserLoginToken
    @PutMapping("/rooms/join")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoomAfterGetDTO joinRoom(@RequestBody RoomPutDTO roomPutDTO) {
        Room room = roomService.joinRoom(roomPutDTO.getUserId(),roomPutDTO.getRoomId());
        return changeRoomToAfter(room);

    }

    @UserLoginToken
    @GetMapping(value = "/rooms/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoomAfterGetDTO retrieveRoom(@PathVariable Long roomId){
        return changeRoomToAfter(roomService.retrieveRoom(roomId));
    }

    @UserLoginToken
    @GetMapping("/rooms")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<RoomAfterGetDTO> getAllRooms() {
        // fetch all users in the internal representation
        List<Room> rooms = roomService.getAvailableRooms();
        List<RoomAfterGetDTO> roomAfterGetDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (Room room: rooms) {
            roomAfterGetDTOS.add(changeRoomToAfter(room));
        }
        return roomAfterGetDTOS;
    }

    @UserLoginToken
    @PutMapping("/rooms/leave")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leaveRoom(@RequestBody RoomPutDTO roomPutDTO) {
        roomService.leaveRoom(roomPutDTO.getUserId(),roomPutDTO.getRoomId());
        userService.retrieveUser(roomPutDTO.getUserId()).setStatus(UserStatus.ONLINE);
    }


    public RoomAfterGetDTO changeRoomToAfter(Room roomGetDTO){
        RoomAfterGetDTO roomAfterGetDTO = new RoomAfterGetDTO();

        roomAfterGetDTO.setRoomSeats(roomGetDTO.getMode());
        roomAfterGetDTO.setId(roomGetDTO.getId());
        roomAfterGetDTO.setOwnerId(roomGetDTO.getOwnerId());
        roomAfterGetDTO.setRoomName(roomGetDTO.getRoomName());
        roomAfterGetDTO.setStatus(roomGetDTO.getStatus());

        if(roomService.getAllPlayersIds(roomGetDTO.getId())==null) {
            roomAfterGetDTO.setPlayers(null);
        }else {
            for(Long iid: roomService.getAllPlayersIds(roomGetDTO.getId())){
                roomAfterGetDTO.getPlayers().add(DTOMapper.INSTANCE.convertEntityToUserNameDTO(userService.retrieveUser(iid)));
            }
        }

        roomAfterGetDTO.setNumberOfPlayers(roomService.getAllPlayersIds(roomGetDTO.getId()).size());

        if(roomService.getAllTurns(roomGetDTO.getId())==null) {
            roomAfterGetDTO.setTurns(null);
        }else {
            for(GameTurn t: roomService.getAllTurns(roomGetDTO.getId())){
                roomAfterGetDTO.getTurns().add(t);
            }
        }

        List<GameTurn> turns = roomAfterGetDTO.getTurns();
        for(int i=turns.size()-1; i>=0; i--){
            if(turns.get(i).getStatus()!= TurnStatus.END){
                roomAfterGetDTO.setCurrentTurnId(turns.get(i).getId());
                roomAfterGetDTO.setCurrentTurnStatus(turns.get(i).getStatus());
            }
        }

        return roomAfterGetDTO;
    }


}

