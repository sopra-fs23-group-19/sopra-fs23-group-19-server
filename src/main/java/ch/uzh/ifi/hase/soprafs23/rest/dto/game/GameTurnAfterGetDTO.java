package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserNameDTO;

import java.util.ArrayList;
import java.util.List;

public class GameTurnAfterGetDTO {
    private Long id;

    private Long drawingPlayerId;

    private List<UserNameDTO> players = new ArrayList<>();

    private String image;
    private String drawingPlayerName;

    private String targetWord;
    private TurnStatus status;

    private Long roomId;
    private int submitNum=0;



    public int getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(int submitNum) {
        this.submitNum = submitNum;
    }

    public TurnStatus getStatus() {
        return status;
    }

    public void setStatus(TurnStatus status) {
        this.status = status;
    }

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }


    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public String getDrawingPlayerName() {
        return drawingPlayerName;
    }

    public void setDrawingPlayerName(String name) {
        this.drawingPlayerName = name;
    }

    public void setPlayers(List<UserNameDTO> players) {
        this.players = players;
    }

    public List<UserNameDTO> getPlayers() {
        return players;
    }

}
