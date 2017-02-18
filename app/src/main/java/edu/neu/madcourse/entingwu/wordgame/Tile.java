package edu.neu.madcourse.entingwu.wordgame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import edu.neu.madcourse.entingwu.R;

public class Tile {
    private Tile mSubTiles[];
    public int level = 100;
    public int status;

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
            Log.i("level is ", String.valueOf(level));
            //mView.getBackground().setLevel(level);
            mView.getBackground().setAlpha(level);
        }
        if (mView instanceof ImageButton) {
            Drawable drawable = ((ImageButton) mView).getDrawable();
            Log.i("status is ", String.valueOf(status));
            drawable.setLevel(status);
            mView.getBackground().setAlpha(level);
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
