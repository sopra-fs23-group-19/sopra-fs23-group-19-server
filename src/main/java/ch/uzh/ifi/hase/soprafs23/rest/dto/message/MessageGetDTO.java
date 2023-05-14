package ch.uzh.ifi.hase.soprafs23.rest.dto.message;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;

public class MessageGetDTO {
    private Long messageId;
    private Long useridFrom;
    private String usernameFrom;
    private String usernameTo;
    private Long useridTo;
    private Long roomId;
    private MessageStatus status;
    private String roomName;
    private MessageType type;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setUseridFrom(Long useridFrom) {
        this.useridFrom = useridFrom;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Long getUseridFrom() {
        return useridFrom;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getUseridTo() {
        return useridTo;
    }

    public void setUseridTo(Long useridTo) {
        this.useridTo = useridTo;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }
}
