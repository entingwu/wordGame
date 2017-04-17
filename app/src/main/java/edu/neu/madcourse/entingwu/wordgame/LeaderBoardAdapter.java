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

import java.util.List;

import edu.neu.madcourse.entingwu.R;
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
    private String userToken;
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
        final String winner = strs[0];
        final String score = strs[1];
        getUserToken(winner);
        record.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Send congrats to " + winner, Toast.LENGTH_SHORT).show();
                GameActivity.pushNotification(winner, score, userToken);
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
                        userToken = game.userToken;
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
}