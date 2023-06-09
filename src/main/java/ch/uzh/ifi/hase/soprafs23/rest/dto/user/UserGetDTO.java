package ch.uzh.ifi.hase.soprafs23.rest.dto.user;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import java.util.Date;

public class UserGetDTO {

  private Long id;
  private String username;

  private String token;
  private UserStatus status;
  private Date creationDate;
  private int bestScore;
  private int totalScore;

  private int currentScore;
  private String guessingWord;

  private int currentGameScore;

  private Long roomId;

  public int getBestScore(){return bestScore;}
  public void setBestScore(int bestScore){this.bestScore=bestScore;}
  public int getTotalScore(){return totalScore;}
  public void setTotalScore(int totalScore){this.totalScore=totalScore;}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }


  public Date getCreationDate(){
      return creationDate;
  }

  public void setCreationDate(Date date){
      this.creationDate = date;
  }
  public String getToken() {
      return token;
  }
  public void setToken(String token) {
      this.token = token;
  }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public String getGuessingWord() {
        return guessingWord;
    }

    public void setGuessingWord(String guessingWord) {
        this.guessingWord = guessingWord;
    }

    public int getCurrentGameScore() {
        return currentGameScore;
    }

    public void setCurrentGameScore(int currentGameScore) {
        this.currentGameScore = currentGameScore;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
