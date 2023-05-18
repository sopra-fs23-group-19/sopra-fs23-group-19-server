package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.MessageRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.ConfirmMessageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.FriendMessagePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.message.MessageGetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        List<Message> messageList = messageRepository.findAll();

        for(Message m: messageList){
            if(m.getType().equals(MessageType.GAME) && m.getUseridTo().equals(message.getUseridTo())
                    && m.getUseridFrom().equals(message.getUseridFrom())
                    && m.getStatus().equals(MessageStatus.PENDING)){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Message already exists!");
            }

            if(m.getType() == MessageType.GAME && m.getUseridTo().equals(message.getUseridTo())
                    && m.getUseridFrom().equals(message.getUseridFrom())
                    && m.getStatus()== MessageStatus.AGREE){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Your request was already approved.");
            }
        }

        if(roomRepository.findByid(message.getRoomId()).getStatus() == RoomStatus.WAITING) {
            message.setStatus(MessageStatus.PENDING);
            message.setType(MessageType.GAME);
            return messageRepository.saveAndFlush(message);
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Game already started!");
        }
    }

    public List<Message> getMessagesByUser(Long userid){
        if(userid == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist!");
        }
        List<Message> messageList =  messageRepository.findByUseridTo(userid);
        if(messageList==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message doesn't exist!");
        }
        return messageList;
    }

    public List<Message> getPendingMessages(Long userid){
        List<Message> result = new ArrayList<>();

        List<Message> allMesssages = getMessagesByUser(userid);

        for(Message message:allMesssages){
            if(message.getStatus()==MessageStatus.PENDING){
                result.add(message);
            }
        }

        return result;
    }


    public Message getMessageInfo(Long id){
        Message message = messageRepository.findByid(id);
        if(message==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message doesn't exist!");
        }

        return message;
    }

    public Message comfirmGame(Long id, ConfirmMessageDTO confirmMessageDTO){
        Message message = messageRepository.findByid(id);

        if(message == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Messages not found!");
        }

        Room room  = roomRepository.findByid(message.getRoomId());
        if(room == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room doesn't exist!");
        }

        if(room.getStatus() !=RoomStatus.WAITING){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game already started!");
        }

        if(message.getStatus() == MessageStatus.PENDING) {
            message.setStatus(MessageStatus.valueOf(confirmMessageDTO.getAction()));
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already reacted to this message!");
        }
        messageRepository.flush();

        return message;
    }

    public Message addFriend(FriendMessagePostDTO friendMessagePostDTO){
        Message message = new Message();
        message.setStatus(MessageStatus.PENDING);
        message.setType(MessageType.FRIEND);
        message.setUseridFrom(friendMessagePostDTO.getUseridFrom());
        message.setUseridTo(friendMessagePostDTO.getUseridTo());

        List<Message> messageList = messageRepository.findAll();
        User u = userRepository.findByid(friendMessagePostDTO.getUseridFrom());

        if(u==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        List<Long> firendsList = u.getFriends();

        for(Long f:firendsList){
            if (f.equals(friendMessagePostDTO.getUseridTo())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "He/She is already your friend.");
            }
        }

        for(Message m: messageList){

            if(m.getType() == message.getType() && m.getUseridTo().equals(message.getUseridTo())
                    && m.getUseridFrom().equals(message.getUseridFrom())
                    && m.getStatus().equals(message.getStatus())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Message already exists!");
            }

            if(m.getType() == message.getType() && m.getUseridTo().equals(message.getUseridFrom())
                    && m.getUseridFrom().equals(message.getUseridTo())
                    && m.getStatus()== message.getStatus()){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Message already exists!");
            }

        }

        return messageRepository.saveAndFlush(message);
    }

    public MessageGetDTO completeReturnMessage(MessageGetDTO messageGetDTO){
        if(roomRepository.findByid(messageGetDTO.getRoomId())!=null) {
            messageGetDTO.setRoomName(roomRepository.findByid(messageGetDTO.getRoomId()).getRoomName());
        }

        messageGetDTO.setUsernameFrom(userRepository.findByid(messageGetDTO.getUseridFrom()).getUsername());
        messageGetDTO.setUsernameTo(userRepository.findByid(messageGetDTO.getUseridTo()).getUsername());
        return messageGetDTO;
    }

    public Message refreshFriends(Message message) {
        User userFrom = userRepository.findByid(message.getUseridFrom());
        User userTo = userRepository.findByid(message.getUseridTo());

        if(userFrom==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        if(userTo==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        if (message.getStatus().equals(MessageStatus.AGREE)){
            userFrom.getFriends().add(userTo.getId());
            userTo.getFriends().add(userFrom.getId());
        }

        userRepository.flush();

        return message;
    }

    public MessageGetDTO completeFriendsMessages(MessageGetDTO messageGetDTO) {

        messageGetDTO.setUsernameFrom(userRepository.findByid(messageGetDTO.getUseridFrom()).getUsername());
        messageGetDTO.setUsernameTo(userRepository.findByid(messageGetDTO.getUseridTo()).getUsername());

        return messageGetDTO;
    }
}
