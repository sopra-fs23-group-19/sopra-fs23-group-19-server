package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.util.List;

public class GameTurnAfterGetDTO {
    private long id;

    private Long drawingPlayerId;

    private List<User> allPlayers;

    private String image;

    private String wordsToBeChosen;
    private String targetWord;
    private Boolean DrawingPhase;
    private long gameId;
    private Boolean gameTurnStatus;
    private Boolean gameStatus;

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }

    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public Boolean getGameTurnStatus() {
        return gameTurnStatus;
    }

    public void setGameTurnStatus(Boolean gameTurnStatus) {
        this.gameTurnStatus = gameTurnStatus;
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

    public String getWordsToBeChosen() {
        return wordsToBeChosen;
    }

    public void setWordsToBeChosen(String wordsToBeChosen) {
        this.wordsToBeChosen = wordsToBeChosen;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public Boolean getDrawingPhase() {
        return DrawingPhase;
    }

    public void setDrawingPhase(Boolean drawingPhase) {
        DrawingPhase = drawingPhase;
    }

    public List<User> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(List<User> allPlayers) {
        this.allPlayers = allPlayers;
    }

    public Boolean getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Boolean gameStatus) {
        this.gameStatus = gameStatus;
    }
}
