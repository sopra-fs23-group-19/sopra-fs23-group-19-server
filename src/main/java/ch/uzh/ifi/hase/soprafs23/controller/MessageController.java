package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.annotation.UserLoginToken;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.ConfirmMessageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.GameMessagePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.MessageService;
import ch.uzh.ifi.hase.soprafs23.service.RoomService;
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

    //@UserLoginToken
    @GetMapping("/notification/game/{userid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MessageGetDTO> getGameMessagesByUser(@PathVariable long userid) { //return all messages
        List<Message> messages = messageService.getMessagesByUser(userid);
        List<MessageGetDTO> result = new ArrayList<>();

        for(Message message:messages){
            if(message.getType()== MessageType.GAME) {
                result.add(messageService.completeReturnMessage(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message)));
            }
        }

        return result;
    }

    //@UserLoginToken
    @GetMapping("/notification/game/pending/{userid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MessageGetDTO> getPendingGameMessages(@PathVariable long userid) { //return all messages
        List<Message> messages = messageService.getPendingMessages(userid);
        List<MessageGetDTO> result = new ArrayList<>();

        for(Message message:messages){
            if(message.getType()==MessageType.GAME) {
                result.add(messageService.completeReturnMessage(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message)));
            }
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
    public MessageGetDTO confirmGame(@RequestBody ConfirmMessageDTO confirmMessageDTO, @PathVariable long messageId) {

        return messageService.completeReturnMessage(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(messageService.comfirmGame(messageId,confirmMessageDTO)));
    }

}
