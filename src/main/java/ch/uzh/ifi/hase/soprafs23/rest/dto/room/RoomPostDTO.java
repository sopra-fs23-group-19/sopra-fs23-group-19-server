package ch.uzh.ifi.hase.soprafs23.rest.dto.room;

import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;

public class RoomPostDTO {
    private long id;

    private String roomName;

    private RoomMode mode;

    private long ownerId;

    // string of players id

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
