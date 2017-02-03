package edu.neu.madcourse.entingwu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    private static final String TAG = "MainMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MainMeun.onCreate() - create ");
        setContentView(R.layout.activity_main);
        Button quitButton = (Button) findViewById(R.id.button10);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /** Called when the user clicks the About button */
    public void displayAbout(View view) {
        Intent intent = new Intent(this, AboutMe.class);
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
        Intent intent = new Intent(this, TestDictionary.class);
        startActivity(intent);
    }
}
