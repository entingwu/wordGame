package edu.neu.madcourse.entingwu.wordgame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import edu.neu.madcourse.entingwu.R;

public class Tile {
    private Tile mSubTiles[];
    public int level = 1;
    public String choosenWord = "";
    public int lastPosition = -1;
    public char character;
    public boolean choosen;
    public boolean locked;

    private final GameFragment mGame;
    private View mView;

    public Tile(GameFragment game) {
        this.mGame = game;
    }

    public Tile deepCopy() {
        Tile tile = new Tile(mGame);
        //tile.setOwner(getOwner());
        if (getSubTiles() != null) {
            Tile newTiles[] = new Tile[9];
            Tile oldTiles[] = getSubTiles();
            for (int child = 0; child < 9; child++) {
                newTiles[child] = oldTiles[child].deepCopy();
            }
            tile.setSubTiles(newTiles);
        }
        return tile;
    }

    public Tile[] getSubTiles() {
        return mSubTiles;
    }

    public boolean isChoosable(int choosen) {
        Log.i("choosable ", String.valueOf(choosen) + ", " + String.valueOf(lastPosition));
        if (lastPosition == - 1) {
            return true;
        }
        if (RandomPathGenerator.adjs.get(lastPosition).contains(choosen)) {
            return true;
        } else {
            return false;
        }

    }

    public void clearLargeBoard() {
        lastPosition = -1;
        choosenWord = "";
        // small tiles;
        for (Tile elem : getSubTiles()) {
            elem.choosen = false;
            elem.level = 1;
        }
        Log.i("all things cleared", "");
    }

    public void finishIt() {
        // 2. change the level of the choosen word.
        // 3. clear the unchoosen word.
        // 4. submit the word to the game board, earn points.
        // 5. Lock the large board.
        for (Tile elem : getSubTiles()) {
            if (elem.choosen) {
                elem.level = 3;
            } else {
                elem.character = '-';
            }
        }
        locked = true;
    }

    public void painMatch(int level) {
        for (Tile elem : getSubTiles()) {
            if (elem.choosen) {
                elem.level = level;
            }
        }
    }


    public void setSubTiles(Tile[] subTiles) {
        this.mSubTiles = subTiles;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public void updateDrawableState() {
        if (mView == null) return;
        //int level = level
        if (mView.getBackground() != null) {
            //Log.i("level is ", String.valueOf(level));
            //mView.getBackground().setLevel(level);
            //Log.i("Id is ", String.valueOf(mView.getId()));
            //mView.getBackground().setAlpha(0);
        }
        if (mView instanceof ImageButton) {
            Drawable drawable = ((ImageButton) mView).getDrawable();
            if ((character - 'a') >=0  && (character - 'a') < 26) {
                //Log.i("character is ", String.valueOf(character));
                drawable.setLevel(character - 'a');
            } else {
                // invalid character.
                drawable.setLevel(26);
            }
            //mView.getBackground().setAlpha(level);
            mView.getBackground().setLevel(level);
            //Log.i("bg Id is ", String.valueOf(mView.getId()));
            //Log.i("level is ", String.valueOf(level));
        }
    }

    public int getLevel() {
        return 1;
    }

    public void animate() {
        Animator anim = AnimatorInflater.loadAnimator(mGame.getActivity(),
                R.animator.tictactoe);
        if (getView() != null) {
            anim.setTarget(getView());
            anim.start();
        }
    }



}
