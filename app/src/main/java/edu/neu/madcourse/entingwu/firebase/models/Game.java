package edu.neu.madcourse.entingwu.firebase.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Game {

    public String id = UUID.randomUUID().toString();
    public String userName;
    public String score = String.valueOf(0);
    public String scorePhase1 = String.valueOf(0);
    public String scorePhase2 = String.valueOf(0);
    public String longestWord;
    public String wordScore;
    public String date;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private Date currDate = new Date();

    public Game() {
        this.userName = "Anonymous";
        this.date = dateFormat.format(currDate);
    }

    public Game(String userName) {
        this.userName = userName;
        this.date = dateFormat.format(currDate);
    }

    public Game(String userName, String score) {
        this.userName = userName;
        this.score = score;
        this.date = dateFormat.format(currDate);
    }

    public Game(String userName, String score, String scorePhase1, String scorePhase2) {
        this.userName = userName;
        this.score = score;
        this.scorePhase1 = scorePhase1;
        this.scorePhase2 = scorePhase2;
        this.date = dateFormat.format(currDate);
    }

    public Game(String userName, String score, String scorePhase1,
                String scorePhase2, String longestWord, String wordScore) {
        this.userName = userName;
        this.score = score;
        this.scorePhase1 = scorePhase1;
        this.scorePhase2 = scorePhase2;
        this.longestWord = longestWord;
        this.wordScore = wordScore;
        this.date = dateFormat.format(currDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScorePhase1() {
        return scorePhase1;
    }

    public void setScorePhase1(String scorePhase1) {
        this.scorePhase1 = scorePhase1;
    }

    public String getScorePhase2() {
        return scorePhase2;
    }

    public void setScorePhase2(String scorePhase2) {
        this.scorePhase2 = scorePhase2;
    }

    public String getLongestWord() {
        return longestWord;
    }

    public void setLongestWord(String longestWord) {
        this.longestWord = longestWord;
    }

    public String getWordScore() {
        return wordScore;
    }

    public void setWordScore(String wordScore) {
        this.wordScore = wordScore;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}