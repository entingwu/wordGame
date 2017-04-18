package edu.neu.madcourse.entingwu.wordgame;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.firebase.WordGameLeaderBoardActivity;
import edu.neu.madcourse.entingwu.firebase.models.Game;

public class LeaderBoardAdapter extends ArrayAdapter<String> {

    private static final String TAG = LeaderBoardAdapter.class.getSimpleName();
    private static final String USER_NAME = "userName";
    private static final String GAMES = "games";
    private static final String DIV = "  ";
    private static final String SDIV = "  ";
    private final Context context;
    private DatabaseReference ref;
    private List<String> games;
    private Map<String, String> map = new HashMap<>();
    private int layoutResourceId;

    public LeaderBoardAdapter(Context context, int layoutResourceId, List<String> games) {
        super(context, layoutResourceId, games);
        this.context = context;
        this.games = games;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View rowView = inflater.inflate(layoutResourceId, parent, false);
        ref = FirebaseDatabase.getInstance().getReference(GAMES);
        Record record = new Record();
        record.gameStr = games.get(position);
        record.textView = (TextView) rowView.findViewById(R.id.leaderBoard_text);
        record.button = (Button) rowView.findViewById(R.id.button_congrats);

        Log.i(TAG, "Show record: " + record.gameStr);
        String[] strs = (record.gameStr.split(DIV).length > 1) ?
                record.gameStr.split(DIV) : record.gameStr.split(SDIV);
        // If strs[0] is a number, which means that it is from ScoreBoard
        // If strs[0] is a string, which means that it is from LeaderBoard
        final String winner = isNumeric(strs[0])? GameActivity.userName : strs[0];
        final String score = isNumeric(strs[0])? strs[0] : strs[1];
        getUserToken(winner);
        record.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Send congrats to " + winner, Toast.LENGTH_SHORT).show();
                GameActivity.pushNotification(winner, score, map.get(winner));
                Log.i(TAG, "userToken: " + map.get(winner));
            }
        });

        rowView.setTag(record);
        record.textView.setText(record.gameStr);
        return rowView;
    }

    public void getUserToken(final String userName) {
        Query query = ref.orderByChild(USER_NAME).equalTo(userName).limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Game game = childDataSnapshot.getValue(Game.class);
                        map.put(userName, game.userToken);
                        Log.i(TAG, "userName: " + userName + "getUserToken: " + game.userToken);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public static class Record {
        String gameStr;
        TextView textView;
        Button button;
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }
}