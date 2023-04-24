package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserNameDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameTurnAfterGetDTO {
    private long id;

    private Long drawingPlayerId;

    private List<UserNameDTO> players = new ArrayList<>();

    private String image;
    private String drawingPlayerName;
    public String getDrawingPlayerName() {
        return drawingPlayerName;
    }

    public void setDrawingPlayerName(String name) {
        this.drawingPlayerName = name;
    }

    private Set<String> wordsToBeChosen;

//    private Boolean DrawingPhase;

    private long roomId;
//    private Boolean gameTurnStatus;
//    private Boolean gameStatus;

    private int submitNum;

    public int getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(int submitNum) {
        this.submitNum = submitNum;
    }

    private TurnStatus status;
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

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getRoomId() {
        return roomId;
    }

    private String targetWord;
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

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Set<String> getWordsToBeChosen() {
        return wordsToBeChosen;
    }

    public void setWordsToBeChosen(Set<String> wordsToBeChosen) {
        this.wordsToBeChosen = wordsToBeChosen;
    }

    public void setPlayers(List<UserNameDTO> players) {
        this.players = players;
    }

    public List<UserNameDTO> getPlayers() {
        return players;
    }

//    public String getTargetWord() {
//        return targetWord;
//    }
//
//    public void setTargetWord(String targetWord) {
//        this.targetWord = targetWord;
//    }
}
