package edu.neu.madcourse.entingwu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MainMeun.onCreate() - create ");
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the About button */
    public void displayAbout(View view) {
        Intent intent = new Intent(this, AboutMeActivity.class);
        startActivity(intent);
    }

    /** Test Runtime Error */
    public void generateError(View view) {
        char[] str = new char[1];
        Log.e(TAG, "MainMeun.generateError() - generate error ");
        Button button = (Button) findViewById(R.id.button2);
        button.setText(str[2]);
    }

    /** Called when the user clicks the Dictionary button */
    public void testDictionary(View view) {
        Intent intent = new Intent(this, TestDictionaryActivity.class);
        startActivity(intent);
    }
}
