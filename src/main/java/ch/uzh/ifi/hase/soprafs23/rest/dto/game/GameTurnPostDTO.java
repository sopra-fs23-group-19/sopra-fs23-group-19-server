package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

public class GameTurnPostDTO {

    private long gameId;
    private String image;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
