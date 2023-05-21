package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final Long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

 @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private UserStatus status;

  @Column
  private int bestScore = 0;

  @Column
  private int totalScore = 0;

  @GeneratedValue
  private Date creationDate;

  @Column
  private int currentScore = 0; //turn

  @Column
  private String guessingWord;

  @Column
  private int currentGameScore = 0;

    @Column
    private Long roomId;

    @Column
    private boolean confirmRank = false;

    @Column
    @ElementCollection
    private List<Long> friends = new ArrayList<>();

    public boolean isConfirmRank() {
        return confirmRank;
    }

    public void setConfirmRank(boolean confirmRank) {
        this.confirmRank = confirmRank;
    }

    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long id) {
        this.roomId = id;
    }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public int getBestScore(){return bestScore;}

  public void setBestScore(int bestScore){this.bestScore=bestScore;}

  public int getTotalScore(){return totalScore;}

  public void setTotalScore(int totalScore){this.totalScore=totalScore;}

  public Date getCreationDate() {
      return creationDate;
  }

  public void setCreationDate(Date creationDate) {
      this.creationDate = creationDate;
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

    public List<Long> getFriends() {
        return friends;
    }
}
