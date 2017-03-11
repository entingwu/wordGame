package edu.neu.madcourse.entingwu.firebase.models;

public class User {

    public String username;
    public String score;
    public String datePlayed;


    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String score){
        this.username = username;
        this.score = score;
        this.datePlayed = "2017-02-27";
    }


    public User(String username, String score, String date){
        this.username = username;
        this.score = score;
        this.datePlayed = date;
    }

}
