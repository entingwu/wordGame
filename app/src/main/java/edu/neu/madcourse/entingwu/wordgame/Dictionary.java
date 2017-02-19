package edu.neu.madcourse.entingwu.wordgame;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.neu.madcourse.entingwu.Trie;

public class Dictionary {

    private static final String FILE_PATH_PREFIX = "dictionary/";
    private static final String FILE_NAME_PREFIX = "dict";
    private static final String DIV = "_";
    private AssetManager assetManager;

    private Trie[][][] tries = new Trie[26][26][26];
    private static final Gson gson = new Gson();

    public Dictionary() {
        this.assetManager = assetManager;
    }

    void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    String getRandomNineCharacterString() {
        return "abcdefghij";
    }

    /** Read the json String from the file in ./assets */
    private String readDeserialize(String fileName, AssetManager assetManager) {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = assetManager.open(fileName);
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

    boolean isInDict(String s) {
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
                Log.i("FILE OPER ", String.format("%s already exists.", fileName));

                // Build a particular Trie
                String fileJson = readDeserialize(fileName, assetManager);
                Log.i("FILE OPER ", String.format("JsonFile size is %d", fileJson.length()));
                tries[c1][c2][c3] = gson.fromJson(fileJson, Trie.class);
            }
            return tries[c1][c2][c3].search(s);
        } else {
            return false;
        }
    }
}
