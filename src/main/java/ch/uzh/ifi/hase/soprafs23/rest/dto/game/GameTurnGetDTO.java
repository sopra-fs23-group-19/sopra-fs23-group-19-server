package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserNameDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameTurnGetDTO {
    private Long id;

    private TurnStatus status;

    private Long drawingPlayerId;

    //private List<Long> allPlayersIds;

    private String image;

 //   private Set<String> wordsToBeChosen;

//    private Boolean DrawingPhase;

    private Long roomId;
//    private Boolean gameTurnStatus;

//    private Boolean gameStatus;
    private int submitNum;
    private String targetWord;


    public void setSubmitNum(int submitNum) {
        this.submitNum = submitNum;
    }

    public int getSubmitNum() {
        return submitNum;
    }



    public TurnStatus getStatus() {
        return status;
    }


    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public void setStatus(TurnStatus status) {
        this.status = status;
    }

    private List<UserNameDTO> players = new ArrayList<>();

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }

    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public List<UserNameDTO> getPlayers() {
        return players;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setPlayers(List<UserNameDTO> players) {
        this.players = players;
    }
    //    public Set<Long> getAllPlayersIds() {
//        return allPlayersIds;
//    }
//
//    public Long getGameId() {
//        return gameId;
//    }
//
//    public void setGameId(Long gameId) {
//        this.gameId = gameId;
//    }

//    public Boolean getGameTurnStatus() {
//        return gameTurnStatus;
//    }

//    public void setGameTurnStatus(Boolean gameTurnStatus) {
//        this.gameTurnStatus = gameTurnStatus;
//    }

//    public void setAllPlayersIds(List<Long> allPlayersIds) {
//        this.allPlayersIds = allPlayersIds;
//    }

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

//    public Set<String> getWordsToBeChosen() {
//        return wordsToBeChosen;
//    }
//
//    public void setWordsToBeChosen(Set<String> wordsToBeChosen) {
//        this.wordsToBeChosen = wordsToBeChosen;
//    }

//    public Boolean getDrawingPhase() {
//        return DrawingPhase;
//    }
//
//    public void setDrawingPhase(Boolean drawingPhase) {
//        DrawingPhase = drawingPhase;
//    }

//    public Boolean getGameStatus() {
//        return gameStatus;
//    }
//
//    public void setGameStatus(Boolean gameStatus) {
//        this.gameStatus = gameStatus;
//    }

//    public String getTargetWord() {
//        return targetWord;
//    }
//
//    public void setTargetWord(String targetWord) {
//        this.targetWord = targetWord;
//    }
}
