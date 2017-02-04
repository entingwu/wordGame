package edu.neu.madcourse.entingwu;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Serialization {

    private static final String FILE_NAME_PREFIX = "dict";
    private static final String DIV = "_";
    private static final String READ_FILE =
            "/Users/entingwu/AndroidStudioProjects/resources/wordlist.txt";
    private static final String WRITE_PATH_PREFIX =
            "/Users/entingwu/AndroidStudioProjects/NUMAD17S-EntingWu/app/src/main/assets/";
    private static final Gson gson = new Gson();
    private static Trie[][] tries = new Trie[26][26];

    public static void main(String[] args) {
        // 0. Initialize Trie[][]
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                tries[i][j] = new Trie();
            }
        }
        // 1. Read File from Internal Storage, txt -> Trie[][]
        readFile(READ_FILE);

        String[][] jsons = new String[26][26];
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                // 2. Serialization, Trie[][] -> String[][]
                jsons[i][j] = gson.toJson(tries[i][j]);

                // 3. Save File in assets
                String fileName = WRITE_PATH_PREFIX + FILE_NAME_PREFIX + DIV + (char)('a'+ i) + (char)('a'+ j);
                writeFile(jsons[i][j], fileName);
            }
        }
    }

    /** Read txt file from local file system */
    private static void readFile(String fileName) {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            String line = br.readLine();
            int c1, c2;// word length >= 3
            while (line != null) {
                c1 = line.charAt(0) - 'a';
                c2 = line.charAt(1) - 'a';
                System.out.println(line);
                tries[c1][c2].insert(line.trim());
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeFile(String json, String fileName) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
