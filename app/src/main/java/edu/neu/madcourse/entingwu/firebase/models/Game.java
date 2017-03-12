package edu.neu.madcourse.entingwu.firebase.models;

import java.util.UUID;

public class Game {

    public String id;
    public String userName;
    public String date;
    public String score;
    public String scorePhase1;
    public String scorePhase2;
    public String longestWord;
    public String wordScore;

    public Game() {
        this.id = UUID.randomUUID().toString();
        this.userName = "Anonymous";
        this.score = String.valueOf(0);
    }

    public Game(String userName) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.score = String.valueOf(0);
    }

    public Game(String userName, String date, String score) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.date = date;
        this.score = score;
    }

    public Game(String userName, String date, String score, String scorePhase1, String scorePhase2) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.date = date;
        this.score = score;
        this.scorePhase1 = scorePhase1;
        this.scorePhase2 = scorePhase2;
    }

    public Game(String userName, String date, String score, String scorePhase1,
                String scorePhase2, String longestWord, String wordScore) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.date = date;
        this.score = score;
        this.scorePhase1 = scorePhase1;
        this.scorePhase2 = scorePhase2;
        this.longestWord = longestWord;
        this.wordScore = wordScore;
    }
}
