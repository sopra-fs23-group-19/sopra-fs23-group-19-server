package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.*;
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
    @PutMapping("/games/join")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void joinRoom(@RequestBody RoomPutDTO roomPutDTO) {
//         Room room = roomService.joinRoom(roomPutDTO.getUserId(),roomPutDTO.getRoomId());
//         List<Long> players = transferStringToLong(room.getPlayers());
//         for(int i=0; i<players.size(); i++){
//             simpMessagingTemplate.convertAndSend("/topic/waiting/"+Long.toString(players.get(0)), players);
//         }
        String s1 = "/topic/waiting/1";
        simpMessagingTemplate.convertAndSend(s1, "send message");
        
    }

//    @MessageMapping("/games/join") // 处理客户端发送的消息
//    @SendTo("/topic/waitingArea") // 将处理结果广播给所有订阅了该路径的客户端
//    public String joinRoom(RoomPutDTO roomPutDTO){
//        Room room = roomService.joinRoom(roomPutDTO.getUserId(),roomPutDTO.getRoomId());
//        return room.getPlayers();
//    }

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
