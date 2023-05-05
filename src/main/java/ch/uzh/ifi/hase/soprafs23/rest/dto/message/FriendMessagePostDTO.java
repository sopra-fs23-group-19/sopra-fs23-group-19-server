package ch.uzh.ifi.hase.soprafs23.rest.dto.message;

public class FriendMessagePostDTO {
    private long useridFrom;
    private long useridTo;

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
}
