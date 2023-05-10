package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.MessageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.ConfirmMessageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.FriendMessagePostDTO;
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
                          @Qualifier("userRepository") UserRepository userRepository,
                          @Qualifier("roomRepository")RoomRepository roomRepository) {
        this.userRepository=userRepository;
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
        return messageRepository.findByid(id);
    }

    public Message comfirmGame(long id, ConfirmMessageDTO confirmMessageDTO){
        Message message = messageRepository.findByid(id);

        message.setStatus(MessageStatus.valueOf(confirmMessageDTO.getAction()));

        messageRepository.flush();

        return message;
    }

    public Message addFriend(FriendMessagePostDTO friendMessagePostDTO){
        Message message = new Message();
        message.setStatus(MessageStatus.PENDING);
        message.setType(MessageType.FRIEND);
        message.setUseridFrom(friendMessagePostDTO.getUseridFrom());
        message.setUseridTo(friendMessagePostDTO.getUseridTo());

        return messageRepository.saveAndFlush(message);
    }

    public MessageGetDTO completeReturnMessage(MessageGetDTO messageGetDTO){
        System.out.println(messageGetDTO.getRoomId());
        System.out.println(roomRepository.findByid(messageGetDTO.getRoomId()).getMode());
        if(roomRepository.findByid(messageGetDTO.getRoomId())!=null) {
            messageGetDTO.setRoomName(roomRepository.findByid(messageGetDTO.getRoomId()).getRoomName());
            messageGetDTO.setUsernameFrom(userRepository.findByid(messageGetDTO.getUseridFrom()).getUsername());
            messageGetDTO.setUsernameTo(userRepository.findByid(messageGetDTO.getUseridTo()).getUsername());
        }
        return messageGetDTO;
    }

    public Message refreshFriends(Message message) {
        User userFrom = userRepository.findByid(message.getUseridFrom());
        User userTo = userRepository.findByid(message.getUseridTo());
        if (message.getStatus().equals(MessageStatus.AGREE)){
            userFrom.getFriends().add(userTo);
            userTo.getFriends().add(userFrom);
        }

        userRepository.flush();

        return message;
    }

    public MessageGetDTO completeFriendsMessages(MessageGetDTO messageGetDTO) {

        messageGetDTO.setUsernameFrom(userRepository.findById(messageGetDTO.getUseridFrom()).get().getUsername());
        messageGetDTO.setUsernameTo(userRepository.findById(messageGetDTO.getUseridTo()).get().getUsername());

        return messageGetDTO;
    }
}
