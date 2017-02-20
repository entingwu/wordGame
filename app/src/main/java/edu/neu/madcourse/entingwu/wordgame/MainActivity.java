package edu.neu.madcourse.entingwu.wordgame;


import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.entingwu.R;

public class MainActivity extends Activity {
    MediaPlayer mMediaPlayer;
    boolean mute = false;
    // ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game);

        Button muteButton = (Button)findViewById(R.id.mute_button);
        muteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mute) {
                            mute = true;
                            view.setBackgroundResource(R.drawable.onmusic);
                            pause();

                        } else {
                            view.setBackgroundResource(R.drawable.mute);
                            mute = false;
                           play();
                        }
                    }
                });
    }

    void play() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.wordgame_bgm);
        mMediaPlayer.setVolume(0.5f, 0.5f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    void pause() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }
    @Override
    protected void onResume() {
        super.onResume();
        play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }
}
