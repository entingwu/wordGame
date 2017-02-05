package edu.neu.madcourse.entingwu;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class TestDictionaryActivity extends AppCompatActivity {
    private static final String TAG = "TestDictionaryActivity";
    private static final String FILE_PATH_PREFIX = "dictionary/";
    private static final String FILE_NAME_PREFIX = "dict";
    private static final String KEY_STRING = "KEY_STRING";
    private static final String DICT_PREFS = "DICT_PREFS";
    private static final String DIV = "_";
    private static final String EMPTY_STRING = "";
    private static final int MODE = Activity.MODE_PRIVATE;
    private static final Gson gson = new Gson();

    private AlertDialog mDialog;
    private EditText editText;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems;
    private Set<String> set;
    private SharedPreferences.Editor editor;
    private Trie[][][] tries = new Trie[26][26][26];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "TestDictionaryActivity.onCreate()");
        setContentView(R.layout.activity_test_dictionary);

        listItems = new ArrayList<String>();
        loadSharePreferences();
        list = (ListView) findViewById(R.id.list);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currWord = s.toString();
                Pattern p = Pattern.compile("[^a-zA-Z]");
                if (p.matcher(currWord).find()) {
                    Log.e(TAG, String.format("Invalid Input, %s", currWord));
                    return;
                }

                if (s.length() >= 3) {
                    char ch1 = s.charAt(0);
                    char ch2 = s.charAt(1);
                    char ch3 = s.charAt(2);
                    int c1 = ch1 - 'a';
                    int c2 = ch2 - 'a';
                    int c3 = ch3 - 'a';
                    if (tries[c1][c2][c3] == null) {
                        // Deserialization: json->Trie
                        String fileName = FILE_PATH_PREFIX + FILE_NAME_PREFIX + DIV + ch1 + ch2 + ch3;
                        Log.i(TAG, String.format("%s already exists.", fileName));

                        // Build a particular Trie
                        String fileJson = readDeserialize(fileName);
                        Log.i(TAG, String.format("JsonFile size is %d", fileJson.length()));
                        tries[c1][c2][c3] = gson.fromJson(fileJson, Trie.class);
                    }

                    if (tries[c1][c2][c3].search(currWord)) {
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                        if (!listItems.contains(currWord)) {
                            listItems.add(currWord);
                            adapter.notifyDataSetChanged();
                            set.add(currWord);
                            editor.clear();
                            editor.putStringSet(KEY_STRING, set).commit();
                        }
                    }
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

    /** Display the acknowledgement as an alertDialog */
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

    /** Return to app home screen */
    public void returnToMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    /** Clear button removes all letters from the EditText and ListView */
    public void clearText(View view) {
        editText.setText(EMPTY_STRING);
        listItems.clear();
        adapter.notifyDataSetChanged();
        getApplicationContext().getSharedPreferences(DICT_PREFS, 0).edit().clear().apply();
    }

    /** Read the json String from the file in ./assets */
    private String readDeserialize(String fileName) {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = getAssets().open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
            isr.close();
            is.close();
        } catch (FileNotFoundException e) {
            Log.e("login activity", String.format("File not found: %s", e.toString()));
        } catch (IOException e) {
            Log.e("login activity", String.format("Can not read file:  %s", e.toString()));
        }
        return sb.toString();
    }

    /** Use sharePreferences to avoid the words in listView get lost when the orientation changed. */
    private void loadSharePreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(DICT_PREFS, MODE);
        editor = sharedPreferences.edit();
        set = sharedPreferences.getStringSet(KEY_STRING, new HashSet<String>());
        Log.i(TAG, "Get Data from SharedPreferences.");
        listItems.addAll(sharedPreferences.getStringSet(KEY_STRING, new HashSet<String>()));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}