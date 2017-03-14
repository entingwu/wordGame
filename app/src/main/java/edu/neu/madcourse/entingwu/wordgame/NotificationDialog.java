package edu.neu.madcourse.entingwu.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.firebase.WordGameLeaderBoardActivity;
import edu.neu.madcourse.entingwu.firebase.WordGameMessagingService;

public class NotificationDialog extends Activity {

    private static final String TAG = NotificationDialog.class.getSimpleName();
    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_dialog);
        messageText = (TextView) findViewById(R.id.notification_textView);
        messageText.setText(WordGameMessagingService.message);
    }

    public void endDialog(View view) {
        finish();
        Intent intent = new Intent(this, WordGameLeaderBoardActivity.class);
        startActivity(intent);
    }
}
