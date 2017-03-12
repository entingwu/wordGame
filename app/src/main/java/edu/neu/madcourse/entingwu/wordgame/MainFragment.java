package edu.neu.madcourse.entingwu.wordgame;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.firebase.models.Game;
import edu.neu.madcourse.entingwu.firebase.models.User;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private AlertDialog mDialog;
    private User user;
    private Game game;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_word_game_main, container, false);
        // Handle buttons here...
        View newButton = rootView.findViewById(R.id.new_wg_button);
        View instructionButton = rootView.findViewById(R.id.instruction_wg_button);
        View acknowledgementButton = rootView.findViewById(R.id.acknowledgement_wg_button);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. get prompt_username.xml view
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.prompt_username, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // 2. set prompt_username.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserName);

                // 3. set dialog message
                alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String userName = userInput.getText().toString();
                            user = new User(userName);
                            game = user.game;
                            Log.i(TAG, "current username is: " + user.game.userName);
                            Intent intent = new Intent(getActivity(), GameActivity.class);
                            getActivity().startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            user = new User();
                            game = user.game;
                            Intent intent = new Intent(getActivity(), GameActivity.class);
                            getActivity().startActivity(intent);
                        }
                });

                // 4. create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        instructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Select letters to form words in dictionary, once get a valid word, click again to submit. Click the non back to back letter to cancel the letters");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }
        });
        acknowledgementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.about_wg_text);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Get rid of the about dialog if it's still up
        if (mDialog != null)
            mDialog.dismiss();
    }

}
