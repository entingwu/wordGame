package edu.neu.madcourse.entingwu.wordgame;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.madcourse.entingwu.R;


public class ControlFragment extends Fragment {
    Button quit;
    Button pause;
    boolean paused = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_wordgame_control, container, false);
        quit = (Button) rootView.findViewById(R.id.wg_button_quit);
        pause = (Button)rootView.findViewById(R.id.wg_button_pause);


        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                getActivity().onBackPressed();

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!paused) {
                    // pause game
                    paused = true;
                    pause.setText("RESUME");
                    ((GameActivity) getActivity()).pauseGame();
                    // (GameActivity) getActivity()).restartGame();
                } else {
                    // resume game
                    paused = false;
                    pause.setText("PAUSE");
                    ((GameActivity) getActivity()).resumeGame();
                }
            }
        });
        return rootView;
    }
}
