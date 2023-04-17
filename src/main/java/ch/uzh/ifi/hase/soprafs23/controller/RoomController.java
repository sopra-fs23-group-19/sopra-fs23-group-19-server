package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@RestController
public class RoomController {
    private final RoomService roomService;
    RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    // owner doesn't need to join the room after creating

    @UserLoginToken
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RoomAfterGetDTO createRoom(@RequestBody RoomBeforePostDTO roomBeforePostDTO) {
        RoomPostDTO roomPostDTO=changeBeforeToPost(roomBeforePostDTO);

        // convert API user to internal representation
        Room roomInput = DTOMapper.INSTANCE.convertRoomPostDTOtoEntity(roomPostDTO);

        // convert internal representation of user back to API
        RoomGetDTO roomGetDTO = DTOMapper.INSTANCE.convertEntityToRoomGetDTO(roomService.createRoom(roomInput));
        RoomAfterGetDTO result =  changeGetToAfter(roomGetDTO);

        // simpMessagingTemplate.convertAndSend("/topic/waiting/"+Long.toString(result.getOwnerId()), result.getPlayers());

        return result;
    }

    @UserLoginToken
    @PutMapping("/games/join")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoomAfterGetDTO joinRoom(@RequestBody RoomPutDTO roomPutDTO) {
        Room room = roomService.joinRoom(roomPutDTO.getUserId(),roomPutDTO.getRoomId());
        return changeRoomToAfter(room);
//         for(int i=0; i<players.size(); i++){
//             simpMessagingTemplate.convertAndSend("/topic/waiting/"+Long.toString(players.get(i)), players);
//         }
//        System.out.println(players);
//        String s1 = "/topic/waiting/1";
//        simpMessagingTemplate.convertAndSend(s1, "send message");

    }

    @UserLoginToken
    @GetMapping(value = "/gameRounds/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoomAfterGetDTO retrieveRoom(@PathVariable("roomId") long roomId){
        return changeRoomToAfter(roomService.retrieveRoom(roomId));
    }

    @UserLoginToken
    @PutMapping("/games/leave")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoomAfterGetDTO leaveRoom(@RequestBody RoomPutDTO roomPutDTO) {
        Room room = roomService.leaveRoom(roomPutDTO.getUserId(),roomPutDTO.getRoomId());
        return changeRoomToAfter(room);

    }


    public RoomPostDTO changeBeforeToPost(RoomBeforePostDTO roomBeforePostDTO){
        RoomPostDTO roomPostDTO = new RoomPostDTO();

        roomPostDTO.setId(roomBeforePostDTO.getId());
        roomPostDTO.setRoomName(roomBeforePostDTO.getRoomName());
        roomPostDTO.setOwnerId(roomBeforePostDTO.getOwnerId());
        roomPostDTO.setPlayers(Long.toString(roomBeforePostDTO.getOwnerId()));

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
            roomAfterGetDTO.setPlayers(transferStringToLong(roomGetDTO.getPlayers()));
        }
        return roomAfterGetDTO;
    }


    public RoomAfterGetDTO changeRoomToAfter(Room roomGetDTO){
        RoomAfterGetDTO roomAfterGetDTO = new RoomAfterGetDTO();

        roomAfterGetDTO.setMode(roomGetDTO.getMode());
        roomAfterGetDTO.setId(roomGetDTO.getId());
        roomAfterGetDTO.setOwnerId(roomGetDTO.getOwnerId());
        roomAfterGetDTO.setRoomName(roomGetDTO.getRoomName());

        if(roomGetDTO.getPlayers()==null) {
            roomAfterGetDTO.setPlayers(null);
        }else {
            roomAfterGetDTO.setPlayers(transferStringToLong(roomGetDTO.getPlayers()));
        }
        return roomAfterGetDTO;
    }

    public List<Long> transferStringToLong(String players){
        String[] strArray = players.split(",");
        List<Long> longList = new ArrayList<>();

        for (String s : strArray) {
            longList.add(Long.valueOf(s));
        }

        return longList;
    }

}

