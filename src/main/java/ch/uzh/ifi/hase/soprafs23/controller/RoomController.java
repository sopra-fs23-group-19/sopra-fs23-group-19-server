package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.RoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
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
    private final UserService userService;
    RoomController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

//    @Autowired
//    SimpMessagingTemplate simpMessagingTemplate;

    // owner doesn't need to join the room after creating

    @UserLoginToken
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RoomAfterGetDTO createRoom(@RequestBody RoomPostDTO roomPostDTO) {

        // convert API user to internal representation
        return changeRoomToAfter(
                roomService.createRoom(
                        DTOMapper.INSTANCE.convertRoomPostDTOtoEntity(roomPostDTO)
                )
        );

        // simpMessagingTemplate.convertAndSend("/topic/waiting/"+Long.toString(result.getOwnerId()), result.getPlayers());
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
    public RoomAfterGetDTO retrieveRoom(@PathVariable Long roomId){
        return changeRoomToAfter(roomService.retrieveRoom(roomId));
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<RoomAfterGetDTO> getAllRooms() {
        // fetch all users in the internal representation
        List<Room> rooms = roomService.getRooms();
        List<RoomAfterGetDTO> roomAfterGetDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (Room room: rooms) {
            roomAfterGetDTOS.add(changeRoomToAfter(room));
        }
        return roomAfterGetDTOS;
    }

    @UserLoginToken
    @PutMapping("/games/leave")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leaveRoom(@RequestBody RoomPutDTO roomPutDTO) {
        roomService.leaveRoom(roomPutDTO.getUserId(),roomPutDTO.getRoomId());
    }


    public RoomAfterGetDTO changeRoomToAfter(Room roomGetDTO){
        RoomAfterGetDTO roomAfterGetDTO = new RoomAfterGetDTO();

        roomAfterGetDTO.setRoomSeats(roomGetDTO.getMode());
        roomAfterGetDTO.setId(roomGetDTO.getId());
        roomAfterGetDTO.setOwnerId(roomGetDTO.getOwnerId());
        roomAfterGetDTO.setRoomName(roomGetDTO.getRoomName());
        roomAfterGetDTO.setStatus(roomGetDTO.getStatus());

        if(roomGetDTO.getPlayers()==null) {
            roomAfterGetDTO.setPlayers(null);
        }else {
            for(Long iid: roomGetDTO.getPlayers()){
                userService.changeStatusToPlaying(iid);
                roomAfterGetDTO.getPlayers().add(DTOMapper.INSTANCE.convertEntityToUserNameDTO(userService.retrieveUser(iid)));
            }
        }

        roomAfterGetDTO.setNumberOfPlayers(roomGetDTO.getPlayers().size());

        return roomAfterGetDTO;
    }

//    public List<Long> transferStringToLong(String players){
//        String[] strArray = players.split(",");
//        List<Long> LongList = new ArrayList<>();
//
//        for (String s : strArray) {
//            LongList.add(Long.valueOf(s));
//        }
//
//        return LongList;
//    }

}

