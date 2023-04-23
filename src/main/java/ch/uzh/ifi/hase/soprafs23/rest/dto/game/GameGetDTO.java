package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;

import java.util.List;
import java.util.Set;

public class GameGetDTO {
    private Long id;
    private Set<Long> drawingPlayerIds;
    private Set<Long> allPlayersIds ;
    private Set<Long> gameTurnList;
    private TurnStatus gameTurnStatus;
//    private GameStatus status;
    private int currentGameTurn;
    private Boolean gameStatus ;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Set<Long> getGameTurnList() {
        return gameTurnList;
    }

    public void setGameTurnList(Set<Long>  gameTurnIds) {
        this.gameTurnList = gameTurnIds;
    }

    public  TurnStatus getGameTurnStatus() {
        return gameTurnStatus;
    }

    public void setGameTurnStatus(TurnStatus  turnStatus) {
        this.gameTurnStatus = turnStatus;
    }
    public Set<Long> getDrawingPlayerIds() {
        return drawingPlayerIds;
    }

    public void setDrawingPlayerIds(Set<Long>  drawingPlayerIds) {
        this.drawingPlayerIds = drawingPlayerIds;
    }
    public Set<Long> getAllPlayersIds() {
        return allPlayersIds;
    }

    public void setAllPlayersIds(Set<Long> allPlayersIds) {
        this.allPlayersIds = allPlayersIds;
    }

//    public GameStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(GameStatus turnStatus) {
//        this.status = turnStatus;
//    }

    public int getCurrentGameTurn() {
        return currentGameTurn;
    }

    public void setCurrentGameTurn(int currentGameTurn) {
        this.currentGameTurn = currentGameTurn;
    }
    public Boolean getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Boolean gameStatus) {
        this.gameStatus = gameStatus;
    }
}
