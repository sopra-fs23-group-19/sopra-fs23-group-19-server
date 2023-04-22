package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GAMETURN")
public class GameTurn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String image;

    @Column
    private Long drawingPlayerId;

    @Column
    private String allPlayersIds;

    @Column
    private String targetWord;

    @Column
    private long gameId;

    @Column
    private String wordsToBeChosen;

    @Column
    private Boolean DrawingPhase = false;

    @Column
    private Boolean gameTurnStatus = false;

    @Column
    private Boolean gameStatus = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDrawingPlayer(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }

    public void setAllPlayersIds(String allPlayersIds) {
        this.allPlayersIds = allPlayersIds;
    }

    public String getAllPlayersIds() {
        return allPlayersIds;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getWordsToBeChosen() {
        return wordsToBeChosen;
    }

    public void setWordsToBeChosen(String wordsToBeChosen) {
        this.wordsToBeChosen = wordsToBeChosen;
    }

    public Boolean getDrawingPhase() {
        return DrawingPhase;
    }

    public void setDrawingPhase(Boolean drawingPhase) {
        DrawingPhase = drawingPhase;
    }

    public Boolean getGameTurnStatus() {
        return gameTurnStatus;
    }

    public void setGameTurnStatus(Boolean gameTurnStatus) {
        this.gameTurnStatus = gameTurnStatus;
    }

    public Boolean getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Boolean gameStatus) {
        this.gameStatus = gameStatus;
    }

}