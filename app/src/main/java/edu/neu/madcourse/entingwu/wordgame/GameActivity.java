package edu.neu.madcourse.entingwu.wordgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.firebase.models.Game;

public class GameActivity extends Activity {

    private static final String TAG = GameActivity.class.getSimpleName();
    private DatabaseReference mDatabase;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private GameFragment mGameFragment;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private String userName;
    private Game game;
    List<String> listItems = new ArrayList<>();
    Dictionary dictionary = new Dictionary();
    TextView tx;
    TextView timerText;
    CountDownTimer timer;
    long totalSec;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "GameActivity");
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName") == null ? "Anonymous" : bundle.getString("userName");

        dictionary.setAssetManager(getAssets());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game_frag);
        mGameFragment = (GameFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_wordgame);
        list = (ListView) findViewById(R.id.matchedWord);
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_text,
                listItems) {
        };
        list.setAdapter(adapter);
        tx = (TextView) findViewById(R.id.score);
        tx.setText("Score: 0");

        timerText = (TextView) findViewById(R.id.timer);
        initTimer(180000);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    void addWord(String str) {
        listItems.add(str);
        adapter.notifyDataSetChanged();
    }

    void updateScore(int score) {
        tx.setText("Score: " + score);
    }

    void initTimer(long leftTime) {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
        timer =  new CountDownTimer(leftTime, 1000) {
            public void onTick(long millisUntilFinished) {
                totalSec =  millisUntilFinished / 1000;
                long mins = totalSec / 60;
                long secs = totalSec % 60;
                timerText.setText(String.format("Time Remaining: %d:%d", mins, secs));

                if (mins < 1) {
                    mGameFragment.playTimerSound();
                    timerText.setTextColor(Color.RED);
                }
            }

            public void onFinish() {
                win();
                timerText.setText("Game Over");
            }
        }.start();
    }

    public String getRandomWord() {
        return dictionary.getRandomNineCharacterString();
    }

    public boolean isInDict(String word) {
        return dictionary.isInDict(word);
    }

    public void pauseGame() {
        timer.cancel();
        mGameFragment.pauseGame();
    }

    public void finish() {
        super.finish();
        timer.cancel();
    }

    public void resumeGame() {
        initTimer(totalSec * 1000);
        mGameFragment.resumeGame();
    }

    public void win() {
        /** 1. Display Dialog */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int score = this.mGameFragment.score;
        builder.setMessage("Great job! Total score is: " + score);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.main_menu_label,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        onBackPressed();
                    }
                });
        builder.show();
        timer.cancel();

        /** 2. Send score to Firebase database */
        game = new Game(userName, String.valueOf(score), String.valueOf(mGameFragment.scorePhase1),
                String.valueOf(mGameFragment.scorePhase2), mGameFragment.longestWord,
                String.valueOf(mGameFragment.wordScore));
        mDatabase.child("games").child(game.id).setValue(game);
        Log.i(TAG, "onAddScore: " + game.score);

        /** 3. Send high score notification */

    }

}
