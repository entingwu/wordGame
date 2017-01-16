package edu.neu.madcourse.entingwu.assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import edu.neu.madcourse.entingwu.R;

public class AboutMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Toolbar toolbar = (Toolbar) findViewById(R.id.title_bar_about);
        toolbar.setTitle(R.string.title_about);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryLight));
        setSupportActionBar(toolbar);
    }
}
