package ch.uzh.ifi.hase.soprafs23.rest.dto.room;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;

import java.util.Set;

public class RoomGetDTO {
    private Long id;
//    private Long gameId;
//    private Long gameTurnId;
    private String roomName;

    private int mode;

    private Long ownerId;

    // string of players id

    private Set<Long> players;

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

    public Set<Long> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Long> players) {
        this.players = players;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
