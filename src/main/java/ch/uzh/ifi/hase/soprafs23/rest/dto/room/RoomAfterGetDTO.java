package ch.uzh.ifi.hase.soprafs23.rest.dto.room;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserNameDTO;

import java.util.ArrayList;
import java.util.List;

public class RoomAfterGetDTO {
    private Long id;
    private String roomName;

    private int roomSeats;

    private Long ownerId;
    private List<UserNameDTO> players = new ArrayList<>();

    private RoomStatus status;

    private int numberOfPlayers;

    private List<GameTurn> turns = new ArrayList<>();

    private Long currentTurnId;  //most recent turn
    private TurnStatus currentTurnStatus; //most recent turn status



    public Long getCurrentTurnId() {
        return currentTurnId;
    }

    public TurnStatus getCurrentTurnStatus() {
        return currentTurnStatus;
    }

    public void setCurrentTurnId(Long currentTurnId) {
        this.currentTurnId = currentTurnId;
    }

    public void setCurrentTurnStatus(TurnStatus currentTurnStatus) {
        this.currentTurnStatus = currentTurnStatus;
    }

    public List<GameTurn> getTurns() {
        return turns;
    }

    public void setTurns(List<GameTurn> turns) {
        this.turns = turns;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }

    public String getRoomName(){
        return roomName;
    }
    public void setRoomName(String roomName){
        this.roomName=roomName;
    }

    public int getRoomSeats() {
        return roomSeats;
    }

    public void setRoomSeats(int roomSeats) {
        this.roomSeats = roomSeats;
    }

    public Long getOwnerId(){
        return ownerId;
    }
    public void setOwnerId(Long ownerId){
        this.ownerId=ownerId;
    }

    public void setPlayers(List<UserNameDTO> players) {
        this.players = players;
    }

    public List<UserNameDTO> getPlayers() {
        return players;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
