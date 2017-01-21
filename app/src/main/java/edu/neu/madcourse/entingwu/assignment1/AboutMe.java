package edu.neu.madcourse.entingwu.assignment1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import edu.neu.madcourse.entingwu.R;

public class AboutMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView textView = (TextView) findViewById(R.id.textView_phoneid);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String author_phoneid = telephonyManager.getDeviceId();
        textView.setText(author_phoneid);
        System.out.println(author_phoneid);
    }
}
