package edu.neu.madcourse.entingwu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.title_bar);
        toolbar.setTitle(R.string.author_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryLight));
        setSupportActionBar(toolbar);
    }

    /** Called when the user clicks the About button */
    public void displayAbout(View view) {
        Intent intent = new Intent(this, DisplayAboutActivity.class);
        startActivity(intent);
    }
}
