package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

public class GameTurnGetDTO {
    private Long id;

    private Long drawingPlayerId;

    private String allPlayersIds;

    private String image;

    private String wordsToBeChosen;
    private String targetWord;
    private Boolean DrawingPhase;
    private Long gameId;
    private Boolean gameTurnStatus;

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }

    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public String getAllPlayersIds() {
        return allPlayersIds;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Boolean getGameTurnStatus() {
        return gameTurnStatus;
    }

    public void setGameTurnStatus(Boolean gameTurnStatus) {
        this.gameTurnStatus = gameTurnStatus;
    }

    public void setAllPlayersIds(String allPlayersIds) {
        this.allPlayersIds = allPlayersIds;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getWordsToBeChosen() {
        return wordsToBeChosen;
    }

    public void setWordsToBeChosen(String wordsToBeChosen) {
        this.wordsToBeChosen = wordsToBeChosen;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public Boolean getDrawingPhase() {
        return DrawingPhase;
    }

    public void setDrawingPhase(Boolean drawingPhase) {
        DrawingPhase = drawingPhase;
    }

}
