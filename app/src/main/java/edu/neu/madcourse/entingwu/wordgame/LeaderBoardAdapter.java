package edu.neu.madcourse.entingwu.wordgame;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.neu.madcourse.entingwu.R;

public class LeaderBoardAdapter extends ArrayAdapter<String> {

    private final Context context;
    private List<String> games;
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
        Record record = new Record();
        record.gameStr = games.get(position);
        record.textView = (TextView) rowView.findViewById(R.id.leaderBoard_text);
        rowView.setTag(record);
        record.textView.setText(record.gameStr);
        return rowView;
    }

    public static class Record {
        String gameStr;
        TextView textView;
        @Override
        public String toString() {
            return "Record{" + "gameStr='" + gameStr + '\'' + ", textView=" + textView + '}';
        }
    }
}
