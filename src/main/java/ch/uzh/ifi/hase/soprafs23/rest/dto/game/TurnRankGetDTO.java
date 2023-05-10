package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TurnRankGetDTO {

    private String image;
    private int correctAnswers;
    private Long drawingPlayerId;
    private String drawingPlayerName;
    private List<User> rankedList = new ArrayList<>();

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }

    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public String getDrawingPlayerName() {
        return drawingPlayerName;
    }

    public void setDrawingPlayerName(String drawingPlayerName) {
        this.drawingPlayerName = drawingPlayerName;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<User> getRankedList() {
        return rankedList;
    }

    public void setRankedList(List<User> rankedList) {
        this.rankedList = rankedList;
    }
}
