package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

//
//    @Column
//    private Long gameId;

//    @Column
//    private Long gameTurnId;

//    @Column
//    @ElementCollection
//    private Set<Long> players = new HashSet<>();

    @Column
    private RoomStatus status;

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }

//    public Long getGameId() {
//        return gameId;
//    }
//    public void setGameId(Long id){
//        this.gameId=id;
//    }
//    public Long getGameTurnId() {
//        return gameTurnId;
//    }
//    public void setGameTurnId(Long id){
//        this.gameTurnId=id;
//    }
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

//    public void setPlayers(Set<Long> players) {
//        this.players = players;
//    }
//
//    public Set<Long> getPlayers() {
//        return players;
//    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}