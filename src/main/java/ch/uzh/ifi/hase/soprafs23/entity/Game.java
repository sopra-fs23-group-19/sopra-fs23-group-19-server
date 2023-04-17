package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long gameId;

    @Column
    public String gameTurnList;

    @Column(nullable = false)
    private int turnLength;  //game mode

    @Column
    private int currentGameTurn;  // current turn index

    @ElementCollection
    private Map<User, Integer> playersTotalScores;

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

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long roomId) {
        this.gameId = gameId;
    }

    public void setPlayersTotalScores(User user, Integer playersTotalScores) {
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


}