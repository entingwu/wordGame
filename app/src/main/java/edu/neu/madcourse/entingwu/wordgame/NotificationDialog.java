package edu.neu.madcourse.entingwu.wordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.firebase.WordGameMessagingService;

public class NotificationDialog extends AppCompatActivity {
    TextView messageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_dialog);
        messageText = (TextView) findViewById(R.id.notification_textView);
        messageText.setText(WordGameMessagingService.message);
    }
}
