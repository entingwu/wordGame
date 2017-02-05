package edu.neu.madcourse.entingwu;


import java.util.ArrayList;

/** TrieNode */
class N {
    ArrayList<N> a;
    int e;// isEnd, 0 false, 1 true
    char c;// character
    public N() {
        a = new ArrayList<>();
    }
}

/** Trie */
public class Trie {

    private N root;

    public Trie() {
        root = new N();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        N p = root;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            int h = 0;// isHas, 0 is false, 1 is true
            for (N n : p.a) {
                if (n.c == c) {
                    p = n;
                    h = 1;
                    break;
                }
            }
            if (h == 0) {
                N temp = new N();
                temp.c = c;
                p.a.add(temp);
                p = temp;
            }
        }
        p.e = 1;
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        N p = searchNode(word);
        if(p == null){
            return false;
        }else{
            if(p.e == 1) return true;
        }
        return false;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        N p = searchNode(prefix);
        if(p == null){
            return false;
        }else{
            return true;
        }
    }

    public N searchNode(String s){
        N p = root;
        for(int i = 0; i < s.length(); i++){
            char c= s.charAt(i);
            int h = 0;// isHas, 0 is false, 1 is true
            for (N n : p.a) {
                if (n.c == c) {
                    p = n;
                    h = 1;
                    break;
                }
            }
            if (h == 0) {
                return null;
            }
        }

        if(p == root) {
            return null;
        }
        return p;
    }
}