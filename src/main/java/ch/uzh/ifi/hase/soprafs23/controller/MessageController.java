package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.ConfirmMessageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.FriendMessagePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.GameMessagePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;
    MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //@UserLoginToken
    @PostMapping("/notification/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MessageGetDTO inviteGame(@RequestBody GameMessagePostDTO gameMessagePostDTO) {
        Message message = DTOMapper.INSTANCE.convertGameMessagePostDTOTOEntity(gameMessagePostDTO);

        MessageGetDTO messageGetDTO =  DTOMapper.INSTANCE.convertEntityToMessageGetDTO(messageService.inviteGame(message));
        return messageService.completeReturnMessage(messageGetDTO);
    }

    @PostMapping("/notification/friend")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MessageGetDTO addFriendMessage(@RequestBody FriendMessagePostDTO friendMessagePostDTO) {

        MessageGetDTO messageGetDTO =  messageService.completeFriendsMessages(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(messageService.addFriend(friendMessagePostDTO)));

        return messageService.completeReturnMessage(messageGetDTO);
    }

    //@UserLoginToken
    @GetMapping("/notification/game/{userid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MessageGetDTO> getMessagesByUser(@PathVariable long userid) { //return all messages
        List<Message> messages = messageService.getMessagesByUser(userid);
        List<MessageGetDTO> result = new ArrayList<>();

        for(Message message:messages){
            result.add(messageService.completeReturnMessage(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message)));

        }

        return result;
    }

    //@UserLoginToken
    @GetMapping("/notification/game/pending/{userid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MessageGetDTO> getPendingMessages(@PathVariable long userid) { //return all messages
        List<Message> messages = messageService.getPendingMessages(userid);
        List<MessageGetDTO> result = new ArrayList<>();

        for(Message message:messages){
            result.add(messageService.completeReturnMessage(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message)));

        }

        return result;
    }

    //@UserLoginToken
    @GetMapping("/notification/game/information/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MessageGetDTO getMessageInfo(@PathVariable long messageId) { //return all messages
        return messageService.completeReturnMessage(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(messageService.getMessageInfo(messageId)));
    }

    //@UserLoginToken
    @PostMapping("/notification/game/{messageId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MessageGetDTO confirmMessage(@RequestBody ConfirmMessageDTO confirmMessageDTO, @PathVariable long messageId) {

        return messageService.completeReturnMessage(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(messageService.comfirmGame(messageId,confirmMessageDTO)));
    }

    // get all messages
    //@UserLoginToken
    @GetMapping("/notification/friends/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MessageGetDTO> confirmMessage(@PathVariable long userId) {
        List<Message> messages = messageService.getMessagesByUser(userId);
        List<MessageGetDTO> result = new ArrayList<>();

        for(Message message:messages){
            if(message.getStatus().equals(MessageStatus.PENDING)) {
                result.add(messageService.completeFriendsMessages(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message)));
            }
        }

        return result;
    }

    // agree or reject friends invitations
    //@UserLoginToken
    @PostMapping("/friends/{messageId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MessageGetDTO refreshFriends(@RequestBody ConfirmMessageDTO confirmMessageDTO, @PathVariable long messageId){
        Message message = messageService.comfirmGame(messageId,confirmMessageDTO);
        MessageGetDTO messageGetDTO = DTOMapper.INSTANCE.convertEntityToMessageGetDTO(messageService.refreshFriends(message));

        return messageService.completeFriendsMessages(messageGetDTO);
    }

}
