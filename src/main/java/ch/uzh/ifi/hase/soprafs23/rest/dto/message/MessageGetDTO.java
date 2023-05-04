package ch.uzh.ifi.hase.soprafs23.rest.dto.message;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;

public class MessageGetDTO {
    private long messageId;
    private long useridFrom;
    private String usernameFrom;
    private String usernameTo;
    private long useridTo;
    private long roomId;
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

    public void setUseridFrom(long useridFrom) {
        this.useridFrom = useridFrom;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public long getUseridFrom() {
        return useridFrom;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getRoomId() {
        return roomId;
    }

    public long getUseridTo() {
        return useridTo;
    }

    public void setUseridTo(long useridTo) {
        this.useridTo = useridTo;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }
}
