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

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestDictionary extends AppCompatActivity {
    private static final String TAG = "TestDictionary";
    private EditText editText;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems;
    private Trie trie = new Trie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "TestDictionary.onCreate()");
        setContentView(R.layout.activity_test_dictionary);

        StringBuffer sb = new StringBuffer("");
        try {
            InputStream is = getResources().openRawResource(R.raw.wordlist_test);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line = bufferedReader.readLine();
            while (line != null) {
                trie.insert(line.trim());
                sb.append(line);
                Log.i(TAG, line);
                line = bufferedReader.readLine();
            }
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, String.valueOf(trie.search("computer")));

        // Serialization
        Gson gson = new Gson();


        listItems = new ArrayList<String>();
        list = (ListView) findViewById(R.id.list);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listItems.size() != 0) {
                    Log.i(TAG, "onTextChanged " + listItems.get(listItems.size() - 1));
                }
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
