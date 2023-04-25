package ch.uzh.ifi.hase.soprafs23.rest.dto.room;

public class RoomPostDTO {
    // private Long id;

    private String roomName;

    private int mode;

    private Long ownerId;


    public String getRoomName(){
        return roomName;
    }
    public void setRoomName(String roomName){
        this.roomName=roomName;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public Long getOwnerId(){
        return ownerId;
    }
    public void setOwnerId(Long ownerId){
        this.ownerId=ownerId;
    }

}
