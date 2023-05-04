package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.repository.MessageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.ConfirmMessageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.MessageGetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(@Qualifier("messageRepository") MessageRepository messageRepository,
                          @Qualifier("userRepository") UserRepository userPository,
                          @Qualifier("roomRepository")RoomRepository roomRepository) {
        this.userRepository=userPository;
        this.roomRepository=roomRepository;
        this.messageRepository = messageRepository;
    }

    public Message inviteGame(Message message){
        message.setStatus(MessageStatus.PENDING);
        message.setType(MessageType.GAME);
        return messageRepository.saveAndFlush(message);
    }

    public List<Message> getMessagesByUser(long userid){
        return messageRepository.findByUseridTo(userid);
    }

    public List<Message> getPendingMessages(long userid){
        List<Message> result = new ArrayList<>();

        List<Message> allMesssages = getMessagesByUser(userid);

        for(Message message:allMesssages){
            if(message.getStatus()==MessageStatus.PENDING){
                result.add(message);
            }
        }

        return result;
    }

    public Message getMessageInfo(long id){
        return messageRepository.findById(id);
    }

    public Message comfirmGame(long id, ConfirmMessageDTO confirmMessageDTO){
        Message message = messageRepository.findById(id);

        message.setStatus(MessageStatus.valueOf(confirmMessageDTO.getAction()));

        messageRepository.flush();

        return message;
    }

    public MessageGetDTO completeReturnMessage(MessageGetDTO messageGetDTO){
        messageGetDTO.setRoomName(roomRepository.findById(messageGetDTO.getRoomId()).getRoomName());
        messageGetDTO.setUsernameFrom(userRepository.findById(messageGetDTO.getUseridFrom()).getUsername());
        messageGetDTO.setUsernameTo(userRepository.findById(messageGetDTO.getUseridTo()).getUsername());

        return messageGetDTO;
    }
}
