package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;

import javax.persistence.*;

@Entity
@Table(name = "MESSAGE")
public class Message {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long useridFrom;

    @Column
    private long useridTo;

    @Column
    private long roomId;

    @Column
    private MessageStatus status;

    @Column
    private MessageType type;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getId() {
        return id;
    }

    public long getUseridFrom() {
        return useridFrom;
    }

    public long getUseridTo() {
        return useridTo;
    }

    public void setUseridFrom(long useridFrom) {
        this.useridFrom = useridFrom;
    }

    public void setUseridTo(long useridTo) {
        this.useridTo = useridTo;
    }
}
