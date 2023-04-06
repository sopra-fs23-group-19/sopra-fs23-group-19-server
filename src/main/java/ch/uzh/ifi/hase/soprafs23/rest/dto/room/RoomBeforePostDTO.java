package ch.uzh.ifi.hase.soprafs23.rest.dto.room;

import ch.uzh.ifi.hase.soprafs23.constant.RoomMode;

public class RoomBeforePostDTO {
    private long id;

    private String roomName;

    private int mode;

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
    public int getMode(){
        return mode;
    }
    public void setMode(int mode){
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
