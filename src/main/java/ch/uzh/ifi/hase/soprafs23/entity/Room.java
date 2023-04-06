package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ROOM")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String roomName;

    @Column(nullable = false)
    private RoomMode mode;

    @Column(nullable = false)
    private long ownerId;

    // string of players id
    @Column
    private String players;

    public long getId() {
        return id;
    }
    public void setId(long id){
        this.id=id;
    }

    public String getRoomName(){
        return roomName;
    }
    public void setRoomName(String roomName){
        this.roomName=roomName;
    }
    public RoomMode getMode(){
        return mode;
    }
    public void setMode(RoomMode mode){
        this.mode=mode;
    }

    public long getOwnerId(){
        return ownerId;
    }
    public void setOwnerId(long ownerId){
        this.ownerId=ownerId;
    }

    public String getPlayers(){
        return players;
    }
    public void setPlayers(String players){
        this.players=players;
    }
}