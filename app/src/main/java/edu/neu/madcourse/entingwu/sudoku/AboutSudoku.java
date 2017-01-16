package edu.neu.madcourse.entingwu.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import edu.neu.madcourse.entingwu.R;

public class AboutSudoku extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_sudoku);
        Toolbar toolbar = (Toolbar) findViewById(R.id.title_bar_sudoku);
        toolbar.setTitle(R.string.title_sudoku);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryLight));
        setSupportActionBar(toolbar);
    }
}
