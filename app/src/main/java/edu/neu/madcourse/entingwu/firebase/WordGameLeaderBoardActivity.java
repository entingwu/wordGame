package edu.neu.madcourse.entingwu.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.firebase.models.Game;
import edu.neu.madcourse.entingwu.firebase.models.User;

public class WordGameLeaderBoardActivity extends AppCompatActivity {

    private static final String TAG = WordGameLeaderBoardActivity.class.getSimpleName();
    private DatabaseReference ref;
    private TextView userName;
    private TextView score;
    private TextView longestWord;
    private TextView wordScore;
    private TextView date;
    private List<Game> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game_leaderboard);

        userName = (TextView) findViewById(R.id.leader_board_username);
        score = (TextView) findViewById(R.id.leader_board_score);
        longestWord = (TextView) findViewById(R.id.leader_board_longest_word);
        wordScore = (TextView) findViewById(R.id.leader_board_word_score);
        date = (TextView) findViewById(R.id.leader_board_date);
        recordList = new ArrayList<Game>();

        ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        User user = childDataSnapshot.getValue(User.class);
                        System.out.println("@@" + user.userName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Query queryRef = mDatabase.orderByChild("score").limitToLast(10);


    }
}
