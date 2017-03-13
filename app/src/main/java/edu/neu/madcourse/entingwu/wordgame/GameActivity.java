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
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.firebase.models.Game;

public class GameActivity extends Activity {

    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAyUeo0PE:APA91bEf8-5uFFKVxuP7RsqI-zZhUZAoAIyY9eU5myfNq4nLrBTU7e8ECauhk8Iu6IFLCC3nWmiSqn6snWOnsTzS7LicevQpa2QAWw5IMeL5IIODcXNS0W7m4pziaWj3EdG7H5t0U73-";
    private static final String TAG = GameActivity.class.getSimpleName();
    private static final String SCORE = "score";
    private static final String GAMES = "games";
    private DatabaseReference mDatabase;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private GameFragment mGameFragment;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private String userName;
    private Game game;
    public List<String> listItems = new ArrayList<>();
    public Dictionary dictionary = new Dictionary();
    public TextView tx;
    public TextView timerText;
    public CountDownTimer timer;
    public long totalSec;

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
        mDatabase.child(GAMES).child(game.id).setValue(game);
        Log.i(TAG, "onAddScore: " + game.score);

        /** 3. Query the highest score and send notification */
        queryHighScore();
    }

    private void queryHighScore() {
        Query query = mDatabase.child(GAMES).orderByChild(SCORE).limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Game game = childDataSnapshot.getValue(Game.class);
                        pushNotification(game.userName, game.score);
                        Log.i(TAG, "Old high score: " + game.score +
                                " ,New high score: " + mGameFragment.score);
                        //if (mGameFragment.score >= Integer.valueOf(game.score)) {}
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void pushNotification(final String userName, final String score) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotificationTask(userName, score);
            }
        }).start();
    }

    public void pushNotificationTask(final String userName, String score) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", userName + " gets " + score + ". Say Congratulations!");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            // If sending to a single client
            //jPayload.put("to", CLIENT_REGISTRATION_TOKEN);
            jPayload.put("to", "/topics/news");
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + resp);
                    Toast.makeText(GameActivity.this, "New high score!", Toast.LENGTH_LONG).show();
                }
            });

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}