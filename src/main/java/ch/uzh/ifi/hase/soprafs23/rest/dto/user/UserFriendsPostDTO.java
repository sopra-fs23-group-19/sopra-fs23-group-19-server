package ch.uzh.ifi.hase.soprafs23.rest.dto.user;

public class UserFriendsPostDTO {

    private Long useridFrom;
    private Long useridTo;

    public Long getUseridFrom() {
        return useridFrom;
    }

    public void setUseridFrom(Long useridFrom) {
        this.useridFrom = useridFrom;
    }

    public Long getUseridTo() {
        return useridTo;
    }

    public void setUseridTo(Long useridTo) {
        this.useridTo = useridTo;
    }

}
