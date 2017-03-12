package edu.neu.madcourse.entingwu.firebase.models;

public class Users {

    public String username;
    public String score;
    public String datePlayed;


    public Users(){
        // Default constructor required for calls to DataSnapshot.getValue(Users.class)
    }

    public Users(String username, String score){
        this.username = username;
        this.score = score;
        this.datePlayed = "2017-02-27";
    }


    public Users(String username, String score, String date){
        this.username = username;
        this.score = score;
        this.datePlayed = date;
    }

}
