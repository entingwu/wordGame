package edu.neu.madcourse.entingwu.wordgame;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.entingwu.R;

public class GameActivity extends Activity {
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private GameFragment mGameFragment;
    private ListView list;
    private ArrayAdapter<String> adapter;
    List<String> listItems = new ArrayList<>();
    Dictionary dictionary = new Dictionary();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        dictionary.setAssetManager(getAssets());

        //dictionary = new Dictionary(getAssets());

        //boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
//        if (restore) {
//            String gameData = getPreferences(MODE_PRIVATE)
//                    .getString(PREF_RESTORE, null);
//            if (gameData != null) {
                //mGameFragment.putState(gameData);
//            }
//        }
        //Log.d("UT3", "restore = " + restore);
    }

    void addWord(String str) {
        listItems.add(str);
        adapter.notifyDataSetChanged();
    }

    void updateScore(int score) {
        TextView tx = (TextView) findViewById(R.id.score);
        tx.setText("Score: " + score);
    }

    public String getRandomWord() {
        return dictionary.getRandomNineCharacterString();
    }

    public boolean isInDict(String word) {
        return dictionary.isInDict(word);
    }



}
