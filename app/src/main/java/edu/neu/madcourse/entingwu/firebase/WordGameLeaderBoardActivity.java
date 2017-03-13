package edu.neu.madcourse.entingwu.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
import edu.neu.madcourse.entingwu.wordgame.LeaderBoardAdapter;

public class WordGameLeaderBoardActivity extends AppCompatActivity {

    private static final String TAG = WordGameLeaderBoardActivity.class.getSimpleName();
    private static final String DIV = "\t\t\t\t";
    private DatabaseReference ref;
    private List<String> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game_leaderboard);
        ref = FirebaseDatabase.getInstance().getReference("games");

        gameList = new ArrayList<>();
        getRecordList("score", 10);
    }

    public void getRecordList(final String orderBy, final int num) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // query
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference.child("games").orderByChild(orderBy).limitToLast(num);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            gameList.clear();
                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                Game game = childDataSnapshot.getValue(Game.class);
                                Log.i(TAG, "current user score: " + game.score);
                                String line = game.userName + DIV + game.score + DIV +
                                        game.longestWord + DIV + game.wordScore + DIV + game.date;
                                gameList.add(0, line);
                            }
                        }
                        LeaderBoardAdapter adapter = new LeaderBoardAdapter(
                                WordGameLeaderBoardActivity.this,
                                R.layout.leaderboard_listitem,
                                gameList);
                        ListView leaderListView = (ListView) findViewById(R.id.leaderBoard_list);
                        leaderListView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Failed to read value.", databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    /** Load Game Record List By Total Score */
    public void orderByTotalScore(View view) {
        getRecordList("score", 10);
    }

    /** Load Game Record List By Word Score */
    public void orderByWordScore(View view) {
        getRecordList("wordScore", 10);
    }
}
