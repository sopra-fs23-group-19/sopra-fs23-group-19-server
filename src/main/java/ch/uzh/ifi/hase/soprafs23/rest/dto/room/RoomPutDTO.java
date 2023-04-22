package ch.uzh.ifi.hase.soprafs23.rest.dto.room;

public class RoomPutDTO {
    private Long userId;
    private Long roomId;

    public Long getRoomId(){
        return roomId;
    }
    public void setRoomId(Long roomId){
        this.roomId=roomId;
    }
    public Long getUserId(){
        return userId;
    }
    public void setUserId(){
        this.userId=userId;
    }
}
