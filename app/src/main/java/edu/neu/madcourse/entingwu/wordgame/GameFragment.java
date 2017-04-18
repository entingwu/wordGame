package edu.neu.madcourse.entingwu.wordgame;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.neu.madcourse.entingwu.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GameFragment extends Fragment {
    static private int mLargeIds[] = {R.id.wg_large1, R.id.wg_large2, R.id.wg_large3,
            R.id.wg_large4, R.id.wg_large5, R.id.wg_large6, R.id.wg_large7, R.id.wg_large8,
            R.id.wg_large9,};
    static private int mSmallIds[] = {R.id.wg_small1, R.id.wg_small2, R.id.wg_small3,
            R.id.wg_small4, R.id.wg_small5, R.id.wg_small6, R.id.wg_small7, R.id.wg_small8,
            R.id.wg_small9,};
    private static final int PHASE_1_PER_WORD_SCORE = 10;
    private static final int PHASE_2_PER_WORD_SCORE = 30;

    private Handler mHandler = new Handler();
    private Tile mEntireBoard = new Tile(this);
    private Tile mLargeTiles[] = new Tile[9];
    private Tile mSmallTiles[][] = new Tile[9][9];
    private int mSoundX, mSoundO, mSoundMiss, mSoundRewind;
    private SoundPool mSoundPool;
    private float mVolume = 1f;
    private int mLastLarge;
    private int mLastSmall;

    public boolean isPhaseTwo;
    public int scorePhase1 = 0;
    public int scorePhase2 = 0;
    public int score = 0;
    public HashSet<String> choosenWords = new HashSet<>();
    public String longestWord;
    public int maxWordLen = 0;
    public int wordScore = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        Log.i("init it it ", "GameFragment");
        setRetainInstance(true);
        initGame();
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundX = mSoundPool.load(getActivity(), R.raw.sergenious_movex, 1);
        mSoundO = mSoundPool.load(getActivity(), R.raw.sergenious_moveo, 1);
        mSoundMiss = mSoundPool.load(getActivity(), R.raw.erkanozan_miss, 1);
        mSoundRewind = mSoundPool.load(getActivity(), R.raw.joanne_rewind, 1);
    }

    public void playTimerSound() {
        mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
    }

    public void pauseGame() {
        for (Tile t : mLargeTiles) {
            t.setLocked(true);
            for (Tile sub : t.getSubTiles()) {
                sub.setLevel(26);
            }
        }
        updateAllTiles();
    }

    public void resumeGame() {
        for (Tile t : mLargeTiles) {
            t.recoverLocked();
            // reset all levels;
            for (Tile sub : t.getSubTiles()) {
                sub.recoverLevel();
            }
        }
        updateAllTiles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.large_board_wordgame, container, false);
        initViews(rootView);
        updateAllTiles();
        return rootView;
    }

    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);

        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);
            for (int small = 0; small < 9; small++) {
                ImageButton inner = (ImageButton) outer.findViewById
                        (mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final Tile largeTile = mLargeTiles[large];
                final Tile smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);
                // ...
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (largeTile.getLocked()) {
                            // if locked nothing to do;
                            return;
                        }
                        smallTile.animate();
                        // ...
                        //if (isAvailable(smallTile)) {
                           // ((GameActivity)getActivity()).startThinking();
                        mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);
                        int currentOne = fSmall;

                        if (smallTile.choosen) {
                            // submit the word;
                            Log.i("the choosen word is: ", largeTile.choosenWord);
                            if ( ((GameActivity)getActivity()).isInDict(largeTile.choosenWord)
                                    && !choosenWords.contains(largeTile.choosenWord)) {
                                // if the the choosen word exists,
                                // 1. play a sound,
                                // 2. change the level of the choosen word.
                                // 3. clear the unchoosen word.
                                // 4. submit the word to the game board, earn points.
                                // 5. Lock the large board.
                                mSoundPool.play(mSoundO, mVolume, mVolume, 1, 0, 1f);
                                largeTile.finishIt();
                                ((GameActivity)getActivity()).addWord(largeTile.choosenWord);

                                // Count Score in PhaseOne and PhaseTwo
                                int diff = 0;
                                int currWordLen = largeTile.choosenWord.length();
                                if (!isPhaseTwo) {
                                    diff = currWordLen * PHASE_1_PER_WORD_SCORE;
                                    scorePhase1 += diff;
                                } else {
                                    diff = currWordLen * PHASE_2_PER_WORD_SCORE;
                                    scorePhase2 += diff;
                                }
                                score += diff;
                                ((GameActivity)getActivity()).updateScore(score);

                                // Find longest Word and Score
                                if (currWordLen >= maxWordLen) {
                                    maxWordLen = currWordLen;
                                    longestWord = largeTile.choosenWord;
                                    wordScore = diff;
                                }
                                choosenWords.add(largeTile.choosenWord);
                            } else {
                                largeTile.clearLargeBoard();
                            }
                        } else {
                            // continue editing...
                            if (largeTile.isChoosable(currentOne)) {
                                largeTile.choosenWord += String.valueOf(smallTile.character);
                                largeTile.lastPosition = currentOne;
                                smallTile.choosen = true;
                                if (((GameActivity)getActivity()).isInDict(largeTile.choosenWord)
                                 && !choosenWords.contains(largeTile.choosenWord)) {
                                    // match
                                    mSoundPool.play(mSoundO, mVolume, mVolume, 1, 0, 1f);
                                    // 2: Choosen
                                    // 3: is in dict
                                    // 1: default
                                    // 0: not seen.
                                    largeTile.painMatch(3);
                                } else {
                                    largeTile.painMatch(2);
                                }

                                //setTile(fLarge, fSmall);
                            } else {
                                // clear the board:
                                largeTile.clearLargeBoard();
                            }
                        }
                        updateAllTiles();
                    }
                });
                // ...
            }
        }
    }

    public void initGame() {
        Log.d("UT3", "init game");
        mEntireBoard = new Tile(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new Tile(this);
            List<Integer> allocation = RandomPathGenerator.randomAllocation();
            String ddd = "";
            for (Integer d : allocation) {
                ddd += String.valueOf(d);
            }
            Log.i("ni men na ", ddd);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new Tile(this);
            }
            // assign characters to tiles
            String word = ((GameActivity)getActivity()).getRandomWord();
            for (int i = 0; i < 9; ++i) {
                mSmallTiles[large][allocation.get(i)].character = word.charAt(i);
            }

            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        // If the player moves first, set which spots are available
        mLastSmall = -1;
        mLastLarge = -1;
    }

    private void updateAllTiles() {
        mEntireBoard.updateDrawableState();
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large].updateDrawableState();
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].updateDrawableState();
            }
        }

        Tile phaseTwoPanel = mLargeTiles[4];
        if(phaseOneEnds() && !isPhaseTwo) {
            isPhaseTwo = true;
            ((GameActivity)getActivity()).setPhaseTwoText();
            // Use the middle one as the bonus points;
            List<Character> characters = new ArrayList<>();
            for (Tile largeTile : mLargeTiles) {
                Character randomCharacter = largeTile.choosenWord
                        .charAt(new Random().nextInt(largeTile.choosenWord.length()));
                characters.add(randomCharacter);
            }

            // Reset the middle tile;
            phaseTwoPanel.choosenWord = "";
            phaseTwoPanel.setLocked(false);
            phaseTwoPanel.submitted = false;
            phaseTwoPanel.lastPosition = -1;
            int i = 0;
            for (Tile smallTile: phaseTwoPanel.getSubTiles()) {
                smallTile.choosen = false;
                smallTile.character = characters.get(i);
                smallTile.setLevel(1);
                ++i;
            }
            updateAllTiles();
        }

        if (isPhaseTwo && phaseTwoPanel.submitted) {
            // if already phase 2 and the phase two panel has been submitted as well, just win()!
            ((GameActivity)getActivity()).win();
        }
    }

    private boolean phaseOneEnds() {
        for (Tile tile: mLargeTiles) {
            if (!tile.submitted) {
                return false;
            }
        }
        return true;
    }
}