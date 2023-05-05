package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ROOM")
public class Room implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomName;

    @Column(nullable = false)
    private int mode;

    @Column(nullable = false)
    private Long ownerId;

    @Column
    private RoomStatus status;

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getRoomName(){
        return roomName;
    }
    public void setRoomName(String roomName){
        this.roomName=roomName;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Long getOwnerId(){
        return ownerId;
    }
    public void setOwnerId(Long ownerId){
        this.ownerId=ownerId;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}