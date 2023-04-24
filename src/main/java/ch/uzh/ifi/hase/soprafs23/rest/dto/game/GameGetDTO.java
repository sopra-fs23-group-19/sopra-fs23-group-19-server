package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

import ch.uzh.ifi.hase.soprafs23.constant.RoomStatus;
import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;

import java.util.List;
import java.util.Set;

public class GameGetDTO {
    private Long id;
    private List<Long> drawingPlayerIds;
    private List<Long> allPlayersIds ;
    private List<Long> gameTurnList;
    private TurnStatus gameTurnStatus;
    private RoomStatus status;
    private int currentGameTurn;

    // private Boolean gameStatus ;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getGameTurnList() {
        return gameTurnList;
    }

    public void setAllPlayersIds(List<Long> allPlayersIds) {
        this.allPlayersIds = allPlayersIds;
    }

    public void setGameTurnList(List<Long> gameTurnList) {
        this.gameTurnList = gameTurnList;
    }

    public void setDrawingPlayerIds(List<Long> drawingPlayerIds) {
        this.drawingPlayerIds = drawingPlayerIds;
    }


    public  TurnStatus getGameTurnStatus() {
        return gameTurnStatus;
    }

    public void setGameTurnStatus(TurnStatus  turnStatus) {
        this.gameTurnStatus = turnStatus;
    }

    public List<Long> getAllPlayersIds() {
        return allPlayersIds;
    }

    public List<Long> getDrawingPlayerIds() {
        return drawingPlayerIds;
    }

    //    public Set<Long> getDrawingPlayerIds() {
//        return drawingPlayerIds;
//    }
//
//    public void setDrawingPlayerIds(Set<Long>  drawingPlayerIds) {
//        this.drawingPlayerIds = drawingPlayerIds;
//    }
//    public Set<Long> getAllPlayersIds() {
//        return allPlayersIds;
//    }
//
//    public void setAllPlayersIds(Set<Long> allPlayersIds) {
//        this.allPlayersIds = allPlayersIds;
//    }

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

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public RoomStatus getStatus() {
        return status;
    }

//    public void setGameStatus(Boolean gameStatus) {
//        this.gameStatus = gameStatus;
//    }
//
//    public Boolean getGameStatus() {
//        return gameStatus;
//    }

    public void setCurrentGameTurn(int currentGameTurn) {
        this.currentGameTurn = currentGameTurn;
    }
}
