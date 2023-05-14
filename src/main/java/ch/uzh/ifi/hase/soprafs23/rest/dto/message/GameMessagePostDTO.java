package ch.uzh.ifi.hase.soprafs23.rest.dto.message;

public class GameMessagePostDTO {
    private Long useridFrom;
    private Long useridTo;
    private Long roomId;

    public Long getUseridFrom() {
        return useridFrom;
    }

    public void setUseridFrom(Long useridFrom) {
        this.useridFrom = useridFrom;
    }

    public void setUseridTo(Long useridTo) {
        this.useridTo = useridTo;
    }

    public Long getUseridTo() {
        return useridTo;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
