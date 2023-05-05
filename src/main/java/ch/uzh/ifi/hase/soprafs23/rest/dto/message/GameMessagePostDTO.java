package ch.uzh.ifi.hase.soprafs23.rest.dto.message;

public class GameMessagePostDTO {
    private long useridFrom;
    private long useridTo;
    private long roomId;

    public long getUseridFrom() {
        return useridFrom;
    }

    public void setUseridFrom(long useridFrom) {
        this.useridFrom = useridFrom;
    }

    public void setUseridTo(long useridTo) {
        this.useridTo = useridTo;
    }

    public long getUseridTo() {
        return useridTo;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
}
