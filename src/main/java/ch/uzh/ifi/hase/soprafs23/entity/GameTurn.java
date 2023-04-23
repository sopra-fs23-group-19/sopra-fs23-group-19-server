package ch.uzh.ifi.hase.soprafs23.entity;


import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "GAMETURN")
public class GameTurn implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column
//    private String image;
    @Column(name="IMAGE", columnDefinition="CLOB")
    @Lob
    private String image;

    @Column
    private Long drawingPlayerId;

    @Column(nullable = false)
    private TurnStatus status;

    @Column
    @ElementCollection
    private Set<Long> allPlayersIds = new HashSet<>();

    @Column
    @ElementCollection
    private List<Long> submittedAnswerIds = new ArrayList<>();

//    @Column(nullable = false)
//    private int turnLength;  //game mode

    @Column
    private String targetWord;

    @Column
    private Long gameId;

    @Column
    @ElementCollection
    private Set<String> wordsToBeChosen = new HashSet<>();

//    @Column
//    private Boolean DrawingPhase = false;

//    @Column
//    private Boolean gameTurnStatus = false;

//    @Column
//    private Boolean gameStatus = false;

    public TurnStatus getStatus() {
        return status;
    }

    public void setStatus(TurnStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long turnId) {
        this.id = turnId;
    }

    public List<Long> getSubmittedAnswerIds() {
        return submittedAnswerIds;
    }

    public void setSubmittedAnswerIds(Long userId) {
        this.submittedAnswerIds.add(userId);
    }
//    public int getTurnLength() {
//        return turnLength;
//    }

//    public void setTurnLength(int turnLen) {
//        this.turnLength = turnLen;
//    }

    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }

    public void setAllPlayersIds(Set<Long> allPlayersIds) {
        this.allPlayersIds = allPlayersIds;
    }

    public Set<Long> getAllPlayersIds() {
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

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Set<String> getWordsToBeChosen() {
        return wordsToBeChosen;
    }

    public void setWordsToBeChosen(Set<String> wordsToBeChosen) {
        this.wordsToBeChosen = wordsToBeChosen;
    }

//    public Boolean getDrawingPhase() {
//        return DrawingPhase;
//    }

//    public void setDrawingPhase(Boolean drawingPhase) {
//        DrawingPhase = drawingPhase;
//    }

//    public Boolean getGameTurnStatus() {
//        return gameTurnStatus;
//    }

//    public void setGameTurnStatus(Boolean gameTurnStatus) {
//        this.gameTurnStatus = gameTurnStatus;
//    }

//    public Boolean getGameStatus() {
//        return gameStatus;
//    }
//
//    public void setGameStatus(Boolean gameStatus) {
//        this.gameStatus = gameStatus;
//    }

}