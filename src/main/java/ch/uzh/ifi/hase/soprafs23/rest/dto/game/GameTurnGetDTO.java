package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

public class GameTurnGetDTO {

    private Long drawingPlayerId;
    private String image;

    public Long getDrawingPlayerId() {
        return drawingPlayerId;
    }

    public void setDrawingPlayerId(Long drawingPlayerId) {
        this.drawingPlayerId = drawingPlayerId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
