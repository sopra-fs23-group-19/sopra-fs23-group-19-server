package ch.uzh.ifi.hase.soprafs23.rest.dto.user;

public class UserPutDTO {

    private String username;
    private String password;
    private Long id;
    private String guessingWord;
  private Long currentGameId;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }
    public Long getCurrentGameId() {
        return currentGameId;
    }

    public void setCurrentGameId(Long id) {
        this.currentGameId = id;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuessingWord() {
        return guessingWord;
    }

    public void setGuessingWord(String guessingWord) {
        this.guessingWord = guessingWord;
    }
}
