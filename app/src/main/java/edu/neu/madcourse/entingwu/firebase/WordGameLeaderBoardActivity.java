package edu.neu.madcourse.entingwu.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.entingwu.R;

public class WordGameLeaderBoardActivity extends AppCompatActivity {

    private static final String TAG = WordGameLeaderBoardActivity.class.getSimpleName();
    private DatabaseReference mDatabase;
    private TextView userName;
    private TextView score;
    private TextView longestWord;
    private TextView wordScore;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game_leaderboard);

        userName = (TextView) findViewById(R.id.leader_board_username);
        score = (TextView) findViewById(R.id.leader_board_score);
        longestWord = (TextView) findViewById(R.id.leader_board_longest_word);
        wordScore = (TextView) findViewById(R.id.leader_board_word_score);
        date = (TextView) findViewById(R.id.leader_board_date);
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
}
