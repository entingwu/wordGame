package edu.neu.madcourse.entingwu;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class TestDictionaryActivity extends AppCompatActivity {
    private static final String TAG = "TestDictionaryActivity";
    private static final String FILE_NAME = "data.json";
    private static final String EMPTY_STRING = "";
    private AlertDialog mDialog;
    private EditText editText;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems;
    private Trie trie = new Trie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "TestDictionaryActivity.onCreate()");
        setContentView(R.layout.activity_test_dictionary);

        // 1. Read File from Internal Storage, txt->Trie
        readFromFile();

        // 2. Serialization
        Gson gson = new Gson();
        String json = gson.toJson(trie);

        // 3. Save File in Internal Storage
        writeToFile(FILE_NAME, json);

        // 4. Deserialization: json->Trie
        String fileJson = readDeserialize(FILE_NAME);
        trie = gson.fromJson(fileJson, Trie.class);
        Log.i(TAG, "Deserialize: " + String.valueOf(trie.search("compute")));
        Log.i(TAG, "Deserialize" + String.valueOf(trie.search("a")));

        listItems = new ArrayList<String>();
        list = (ListView) findViewById(R.id.list);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currWord = s.toString();
                Pattern p = Pattern.compile("[^a-zA-Z]");
                if (p.matcher(currWord).find()) {
                    if (trie.search(currWord)) {
                        listItems.add(currWord);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "Invalid Input." + currWord);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };
        list.setAdapter(adapter);
    }

    /** Called when the user clicks the Back to Menu button */
    public void displayAcknowledgement(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestDictionaryActivity.this);
        builder.setTitle(R.string.title_acknowledgement);
        builder.setMessage(R.string.text_acknowledgement);
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDialog.cancel();
            }
        });
        mDialog = builder.show();
    }

    /** Called when the user clicks the Back to Menu button */
    public void backToMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Clear button */
    public void clearText(View view) {
        editText.setText(EMPTY_STRING);
        listItems.clear();
    }

    private String readDeserialize(String fileName) {
        StringBuffer sb = new StringBuffer(EMPTY_STRING);
        try {
            InputStream is = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                Log.i(TAG, line);
                line = bufferedReader.readLine();
            }
            is.close();
            isr.close();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return sb.toString();
    }

    private void readFromFile() {
        StringBuffer sb = new StringBuffer(EMPTY_STRING);
        try {
            InputStream is = getResources().openRawResource(R.raw.wordlist_test);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line = bufferedReader.readLine();
            while (line != null) {
                trie.insert(line.trim());
                sb.append(line);
                line = bufferedReader.readLine();
            }
            is.close();
            isr.close();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    private void writeToFile(String filename, String json) {
        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osr = new OutputStreamWriter(fos);
            osr.write(json);
            osr.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
