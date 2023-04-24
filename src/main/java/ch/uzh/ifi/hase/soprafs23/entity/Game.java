//package ch.uzh.ifi.hase.soprafs23.entity;
//
//import ch.uzh.ifi.hase.soprafs23.constant.TurnStatus;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Entity
//@Table(name = "GAME")
//public class Game implements Serializable {
//
//    private static final Long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column
//    @ElementCollection
//    private Set<Long> drawingPlayerIds = new HashSet<>();
//
//    @Column
//    @ElementCollection
//    private Set<Long> allPlayersIds = new HashSet<>();
//    @Column
//    @ElementCollection
//    private List<Long> gameTurnList = new ArrayList<>();
//
//    @Column(nullable = false)
//    private int turnLength;  //game mode
//
//    @Column
//    private TurnStatus gameTurnStatus;
////    @Column
////    private GameStatus status;  //game status
//
//    @Column
//    private int currentGameTurn;  // current turn index
//
//    @Column
//    private Boolean gameStatus = false;

//    public Set<Long> getDrawingPlayerIds() {
//        return drawingPlayerIds;
//    }
//
//    public void setDrawingPlayerIds(long drawingPlayerIds) {
//        this.drawingPlayerIds.add(drawingPlayerIds);
//    }
//    public Set<Long> getAllPlayersIds() {
//        return allPlayersIds;
//    }
//
//    public void setAllPlayersIds(Set<Long> allPlayersIds) {
//        this.allPlayersIds = allPlayersIds;
//    }
//
//    public  TurnStatus getGameTurnStatus() {
//        return gameTurnStatus;
//    }
//
//    public void setGameTurnStatus(TurnStatus turnStatus) {
//            this.gameTurnStatus = turnStatus;
//    }
//
//    public int getTurnLength() {
//        return turnLength;
//    }
//
//    public void setTurnLength(int turnLength) {
//        this.turnLength = turnLength;
//    }
//
//    public int getCurrentGameTurn() {
//        return currentGameTurn;
//    }
//
//    public void setCurrentGameTurn(int currentGameTurn) {
//        this.currentGameTurn = currentGameTurn;
//    }
//
//    public List<Long> getGameTurnList() {
//        return gameTurnList;
//    }
//
//    public void setGameTurnList(long gameTurnId) {
//        this.gameTurnList.add(gameTurnId);
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//    public Boolean getGameStatus() {
//        return gameStatus;
//    }
//
//    public void setGameStatus(Boolean gameStatus) {
//        this.gameStatus = gameStatus;
//    }
//}