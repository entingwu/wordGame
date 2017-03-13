package edu.neu.madcourse.entingwu.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.firebase.models.Game;
import edu.neu.madcourse.entingwu.wordgame.LeaderBoardAdapter;

public class WordGameLeaderBoardActivity extends AppCompatActivity {

    private static final String TAG = WordGameLeaderBoardActivity.class.getSimpleName();
    private static final String DIV = "\t\t\t\t";
    private static final String SDIV = "\t\t\t";
    private static final String SCORE = "score";
    private static final String WORD_SCORE = "wordScore";
    private static final String USER_NAME = "userName";
    private static final String GAMES = "games";
    private static final int NUM = 10;
    private DatabaseReference ref;
    private List<String> gameList;
    private List<String> scoreList;
    private String userName = "Anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game_leaderboard);
        ref = FirebaseDatabase.getInstance().getReference(GAMES);
        gameList = new ArrayList<>();
        scoreList = new ArrayList<>();
        getLeaderBoardRecords(SCORE, NUM);
        Log.i(TAG, "@@" + userName);
        getScoreBoardRecords(userName, NUM);

        ListView leaderListView = (ListView) findViewById(R.id.leaderBoard_list);
        leaderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = (String) parent.getItemAtPosition(position);
                userName = selectedValue.split(DIV)[0];
                getScoreBoardRecords(userName, NUM);
                TextView scoreBoardTitle = (TextView) findViewById(R.id.textView_scoreboard);
                String title = userName + " scores";
                scoreBoardTitle.setText(title);
            }
        });
    }

    public void getScoreBoardRecords(final String userName, final int num) {
        Query query = ref.orderByChild(USER_NAME).equalTo(userName).limitToLast(num);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    scoreList.clear();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Game game = childDataSnapshot.getValue(Game.class);
                        String line = game.score + SDIV + game.scorePhase1 + SDIV + game.scorePhase2 + SDIV +
                                game.longestWord + SDIV + game.wordScore + SDIV + game.date;
                        scoreList.add(0, line);
                    }
                }
                updateScoreList();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void getLeaderBoardRecords(final String orderBy, final int num) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // query
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference.child(GAMES).orderByChild(orderBy).limitToLast(num);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            gameList.clear();
                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                Game game = childDataSnapshot.getValue(Game.class);
                                String line = game.userName + DIV + game.score + DIV +
                                        game.longestWord + DIV + game.wordScore + DIV + game.date;
                                gameList.add(0, line);
                                userName = game.userName;
                            }
                        }
                        ListView leaderListView = (ListView) findViewById(R.id.leaderBoard_list);
                        LeaderBoardAdapter adapter = new LeaderBoardAdapter(
                                WordGameLeaderBoardActivity.this,
                                R.layout.leaderboard_listitem,
                                gameList);
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

    /** Load Game Records By Total Score in Leader Board */
    public void orderByTotalScoreLeaderBoard(View view) {
        getLeaderBoardRecords(SCORE, NUM);
    }

    /** Load Game Records By Word Score in Leader Board */
    public void orderByWordScoreLeaderBoard(View view) {
        getLeaderBoardRecords(WORD_SCORE, NUM);
    }

    /** Load Personal Game Records Ordered By Total Score */
    public void orderByTotalScorePerson(View view) {
        Collections.sort(scoreList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                String[] str1 = s1.split(SDIV);
                String[] str2 = s2.split(SDIV);
                return Integer.valueOf(str2[1]) - Integer.valueOf(str1[1]);
            }
        });
        updateScoreList();
    }

    /** Load Personal Game Records Ordered By Word Score */
    public void orderByWordScorePerson(View view) {
        Collections.sort(scoreList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                String[] str1 = s1.split(SDIV);
                String[] str2 = s2.split(SDIV);
                return Integer.valueOf(str2[4]) - Integer.valueOf(str1[4]);
            }
        });
        updateScoreList();
    }

    private void updateScoreList() {
        ListView scoreListView = (ListView) findViewById(R.id.scoreBoard_list);
        LeaderBoardAdapter adapter = new LeaderBoardAdapter(
                WordGameLeaderBoardActivity.this,
                R.layout.leaderboard_listitem,
                scoreList);
        scoreListView.setAdapter(adapter);
    }
}