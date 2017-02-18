package edu.neu.madcourse.entingwu.wordgame;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.neu.madcourse.entingwu.R;

public class GameActivity extends Activity {
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game_frag);
        mGameFragment = (GameFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_wordgame);
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

}
