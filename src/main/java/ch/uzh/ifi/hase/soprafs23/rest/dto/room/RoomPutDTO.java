package ch.uzh.ifi.hase.soprafs23.rest.dto.room;

public class RoomPutDTO {
    private long userId;
    private long roomId;

    public long getRoomId(){
        return roomId;
    }
    public void setRoomId(long roomId){
        this.roomId=roomId;
    }
    public long getUserId(){
        return userId;
    }
    public void setUserId(){
        this.userId=userId;
    }
}
