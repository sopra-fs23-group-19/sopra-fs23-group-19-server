package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomController {
    private final RoomService roomService;
    RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @UserLoginToken
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RoomAfterGetDTO createRoom(@RequestBody RoomBeforePostDTO roomBeforePostDTO) {
        RoomPostDTO roomPostDTO=changeBeforeToPost(roomBeforePostDTO);
        roomPostDTO.setOwnerId(roomBeforePostDTO.getOwnerId());
        // convert API user to internal representation
        Room roomInput = DTOMapper.INSTANCE.convertRoomPostDTOtoEntity(roomPostDTO);

        // convert internal representation of user back to API
        RoomGetDTO roomGetDTO = DTOMapper.INSTANCE.convertEntityToRoomGetDTO(roomService.createRoom(roomInput));
        return changeGetToAfter(roomGetDTO);
    }

    @UserLoginToken
    @PutMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void joinRoom(@RequestBody RoomPutDTO roomPutDTO) {
        roomService.joinRoom(roomPutDTO.getUserId(),roomPutDTO.getRoomId());
    }

    public RoomPostDTO changeBeforeToPost(RoomBeforePostDTO roomBeforePostDTO){
        RoomPostDTO roomPostDTO = new RoomPostDTO();
        roomPostDTO.setId(roomBeforePostDTO.getId());
        roomPostDTO.setRoomName(roomBeforePostDTO.getRoomName());
        if(roomBeforePostDTO.getMode()==2){
            roomPostDTO.setMode(RoomMode.P2);
        }else if(roomBeforePostDTO.getMode()==4){
            roomPostDTO.setMode(RoomMode.P4);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong room mode!");
        }

        return roomPostDTO;
    }

    public RoomAfterGetDTO changeGetToAfter(RoomGetDTO roomGetDTO){
        RoomAfterGetDTO roomAfterGetDTO = new RoomAfterGetDTO();
        roomAfterGetDTO.setMode(roomGetDTO.getMode());
        roomAfterGetDTO.setId(roomGetDTO.getId());
        roomAfterGetDTO.setOwnerId(roomGetDTO.getOwnerId());
        roomAfterGetDTO.setRoomName(roomGetDTO.getRoomName());

        if(roomGetDTO.getPlayers()==null) {
            roomAfterGetDTO.setPlayers(null);
        }else {
            String[] strArray = roomGetDTO.getPlayers().split(",");
            List<Long> longList = new ArrayList<>();

            for (String s : strArray) {
                longList.add(Long.valueOf(s));
            }
            roomAfterGetDTO.setPlayers(longList);
        }
        return roomAfterGetDTO;
    }
}
