package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @ElementCollection
    private Set<Long> drawingPlayerIds = new HashSet<>();

    @Column
    @ElementCollection
    private Set<Long> allPlayersIds = new HashSet<>();
    @Column
    @ElementCollection
    private Set<Long> gameTurnList = new HashSet<>();

    @Column(nullable = false)
    private int turnLength;  //game mode

    @Column
    private int currentGameTurn;  // current turn index

    public Set<Long> getDrawingPlayerIds() {
        return drawingPlayerIds;
    }

    public void setDrawingPlayerIds(long drawingPlayerIds) {
        this.drawingPlayerIds.add(drawingPlayerIds);
    }
    public Set<Long> getAllPlayersIds() {
        return allPlayersIds;
    }

    public void setAllPlayersIds(Set<Long> allPlayersIds) {
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

    public Set<Long> getGameTurnList() {
        return gameTurnList;
    }

    public void setGameTurnList(long gameTurnId) {
        this.gameTurnList.add(gameTurnId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}