package edu.neu.madcourse.entingwu;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

public class TestDictionary extends AppCompatActivity {
    private static final String TAG = "TestDictionary";
    private EditText editText;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "TestDictionary.onCreate()");
        setContentView(R.layout.activity_test_dictionary);

        listItems = new ArrayList<String>();
        list = (ListView) findViewById(R.id.list);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged " + listItems.size());
                TextView textView = (TextView) findViewById(R.id.testText);
                textView.setText(s);
                listItems.add(s.toString());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listItems);
        list.setAdapter(adapter);
    }
}
