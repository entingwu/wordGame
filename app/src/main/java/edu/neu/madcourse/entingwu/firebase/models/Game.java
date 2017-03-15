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
    public String userToken;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private Date currDate = new Date();

    public Game() {
        this.userName = "Anonymous";
        this.date = dateFormat.format(currDate);
    }

    public Game(String userName, String score) {
        this.userName = userName;
        this.score = score;
        this.date = dateFormat.format(currDate);
    }

    public Game(String userName, String score, String scorePhase1,
                String scorePhase2, String longestWord, String wordScore, String userToken) {
        this.userName = userName;
        this.score = score;
        this.scorePhase1 = scorePhase1;
        this.scorePhase2 = scorePhase2;
        this.longestWord = longestWord;
        this.wordScore = wordScore;
        this.date = dateFormat.format(currDate);
        this.userToken = userToken;
    }
}