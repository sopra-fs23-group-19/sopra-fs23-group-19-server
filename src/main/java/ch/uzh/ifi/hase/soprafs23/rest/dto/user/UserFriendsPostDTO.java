package ch.uzh.ifi.hase.soprafs23.rest.dto.user;

public class UserFriendsPostDTO {

    private Long useridFrom;

    private String useridNameTo;

    public Long getUseridFrom() {
        return useridFrom;
    }

    public void setUseridFrom(Long useridFrom) {
        this.useridFrom = useridFrom;
    }
    public String getUseridNameTo() {
        return useridNameTo;
    }

    public void setUseridNameTo(String useridNameTo) {
        this.useridNameTo = useridNameTo;
    }


}
