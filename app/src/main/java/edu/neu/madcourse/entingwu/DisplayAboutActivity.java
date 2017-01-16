package edu.neu.madcourse.entingwu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class DisplayAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.title_bar_about);
      /*  toolbar.setTitle(R.string.about_title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryLight));*/
        setSupportActionBar(toolbar);
    }
}
