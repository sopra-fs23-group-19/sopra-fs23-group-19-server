package ch.uzh.ifi.hase.soprafs23.entity;


import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GAMETURN")
public class GameTurn implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="IMAGE", columnDefinition="CLOB")
    @Lob
    private String image;

    @Column
    private Long drawingPlayerId;

    @Column(nullable = false)
    private TurnStatus status;

    @Column
    private int submitNum = 0;

    @Column
    private int currentTurn;

    @Column
    private String targetWord;

    @Column
    private Long roomId;


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

    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
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

    public Long getRoomId() {
        return roomId;
    }

    public int getSubmitNum() {
        return submitNum;
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setSubmitNum(int submitNum) {
        this.submitNum = submitNum;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }
}