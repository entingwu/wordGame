package edu.neu.madcourse.entingwu.firebase.models;

public class User {
    public String userName;
    public Game game;

    public User() {
        this.userName = "Anonymous";
        this.game = new Game();
    }

    public User(String userName) {
        this.userName = userName;
        this.game = new Game(userName);
    }

    public User(String userName, Game game) {
        this.userName = userName;
        this.game = game;
    }
}
