package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

import ch.uzh.ifi.hase.soprafs23.rest.dto.user.UserNameDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameTurnAfterGetDTO {
    private long id;

    private Long drawingPlayerId;

    private List<UserNameDTO> players = new ArrayList<>();

    private String image;

    private Set<String> wordsToBeChosen;
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

    public Set<String> getWordsToBeChosen() {
        return wordsToBeChosen;
    }

    public void setWordsToBeChosen(Set<String> wordsToBeChosen) {
        this.wordsToBeChosen = wordsToBeChosen;
    }

    public Boolean getDrawingPhase() {
        return DrawingPhase;
    }

    public void setDrawingPhase(Boolean drawingPhase) {
        DrawingPhase = drawingPhase;
    }

    public Boolean getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Boolean gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setPlayers(List<UserNameDTO> players) {
        this.players = players;
    }

    public List<UserNameDTO> getPlayers() {
        return players;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }
}
