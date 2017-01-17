package edu.neu.madcourse.entingwu.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.neu.madcourse.entingwu.R;
import edu.neu.madcourse.entingwu.sudoku.AboutSudoku;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quitButton = (Button) findViewById(R.id.button10);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /** Called when the user clicks the AboutSudoku button */
    public void displayAbout(View view) {
        Intent intent = new Intent(this, AboutMe.class);
        startActivity(intent);
    }

    /** Test Runtime Error */
    public void generateError(View view) {
        char[] chars = new char[1];
        Button button = (Button) findViewById(R.id.button2);
        button.setText(chars[2]);
    }

    public void startSudoku(View view) {
        Intent intent = new Intent(this, AboutSudoku.class);
        startActivity(intent);
    }
}
