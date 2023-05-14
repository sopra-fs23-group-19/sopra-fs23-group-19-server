package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.MessageStatus;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;

import javax.persistence.*;

@Entity
@Table(name = "MESSAGE")
public class Message {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long useridFrom;

    @Column
    private Long useridTo;

    @Column
    private Long roomId;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public Long getUseridFrom() {
        return useridFrom;
    }

    public Long getUseridTo() {
        return useridTo;
    }

    public void setUseridFrom(Long useridFrom) {
        this.useridFrom = useridFrom;
    }

    public void setUseridTo(Long useridTo) {
        this.useridTo = useridTo;
    }
}
