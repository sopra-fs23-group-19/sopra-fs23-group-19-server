package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

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

    @ElementCollection
    private Map<User, Integer> playersScores;

    @Column
    private String targetWord;

    @Column
    private long gameId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addPlayersScores(User user, Integer playersScores) {

        this.playersScores.put(user, playersScores);
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

    public void setPlayersScores(Map<User, Integer> playersScores) {
        this.playersScores = playersScores;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
}