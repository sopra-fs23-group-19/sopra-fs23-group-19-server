package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String drawingPlayerIds;
    @Column
    private String allPlayersIds;
    @Column
    public String gameTurnList;

    @Column(nullable = false)
    private int turnLength;  //game mode

    @Column
    private int currentGameTurn;  // current turn index

    @ElementCollection
    private Map<User, Integer> playersTotalScores = new HashMap<>();

    public String getDrawingPlayerIds() {
        return drawingPlayerIds;
    }

    public void setDrawingPlayerIds(String drawingPlayerIds) {
        this.drawingPlayerIds = drawingPlayerIds;
    }
    public String getAllPlayersIds() {
        return allPlayersIds;
    }

    public void setAllPlayersIds(String allPlayersIds) {
        this.allPlayersIds = allPlayersIds;
    }

    public int getTurnLength() {
        return turnLength;
    }

    public void setTurnLength(int turnLength) {
        this.turnLength = turnLength;
    }

    public int getCurrentGameTurn() {
        return currentGameTurn;
    }

    public void setCurrentGameTurn(int currentGameTurn) {
        this.currentGameTurn = currentGameTurn;
    }

    public void setPlayersTotalScores(User user, int playersTotalScores) {
        this.playersTotalScores.put(user, playersTotalScores);
    }

    public Map<User, Integer> getPlayersTotalScores() {
        return playersTotalScores;
    }

    public String getGameTurnList() {
        return gameTurnList;
    }

    public void setGameTurnList(String gameTurnList) {
        this.gameTurnList = gameTurnList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}