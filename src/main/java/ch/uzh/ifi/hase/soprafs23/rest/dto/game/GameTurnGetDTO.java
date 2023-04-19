package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

public class GameTurnGetDTO {
    private long id;

    private Long drawingPlayerId;

    private String allPlayersIds;

    private String image;
    private String wordsToBeChosen;
    private String targetWord;

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }

    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public String getAllPlayersIds() {
        return allPlayersIds;
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

    public void setId(long id) {
        this.id = id;
    }

}
