package ch.uzh.ifi.hase.soprafs23.rest.dto.room;

public class RoomPostDTO {
    // private Long id;

    private String roomName;

    private int mode;

    private Long ownerId;

    // string of players id

//    private List<Long> players = new ArrayList<>();

//    private RoomStatus status;

//    public Long getTurnId() {
//        return id;
//    }
//    public void setTurnId(Long id){
//        this.id=id;
//    }

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


//    public List<Long> getPlayers() {
//        return players;
//    }
//
//    public void setPlayers(List<Long> players) {
//        this.players = players;
//    }

//    public void setStatus(RoomStatus status) {
//        this.status = status;
//    }
//
//    public RoomStatus getStatus() {
//        return status;
//    }
}
