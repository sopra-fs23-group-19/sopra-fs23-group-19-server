package ch.uzh.ifi.hase.soprafs23.rest.dto.message;

public class FriendMessagePostDTO {
    private Long useridFrom;
    private Long useridTo;

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
}
