package ch.uzh.ifi.hase.soprafs23.rest.dto.game;

public class GameTurnPutDTO {

    private Long id;
    private String image;
//    private Long userId;
    private String targetWord;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long id) {
//        this.userId = id;
//    }

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

}
