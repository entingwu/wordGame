package edu.neu.madcourse.entingwu;


import java.util.ArrayList;

class N {
    //TrieNode[] arr;
    ArrayList<N> a;
    boolean e;
    char ch;
    // Initialize your data structure here.
    public N() {
        a = new ArrayList<>();
        //this.arr = new TrieNode[26];
    }

}

public class Trie {

    private N root;

    public Trie() {
        root = new N();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        N p = root;
        for(int i=0; i<word.length(); i++){
            char c = word.charAt(i);
            //int index = c-'a';
            boolean isHas = false;
            for (N n : p.a) {
                if (n.ch == c) {
                    p = n;
                    isHas = true;
                    break;
                }
            }
            if (!isHas) {
                N temp = new N();
                temp.ch = c;
                p.a.add(temp);
                p = temp;
            }

//            if(p.arr[index]==null){
//                TrieNode temp = new TrieNode();
//                p.arr[index]=temp;
//                p = temp;
//            }else{
//                p=p.arr[index];
//            }
        }
        p.e=true;
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        N p = searchNode(word);
        if(p==null){
            return false;
        }else{
            if(p.e)
                return true;
        }

        return false;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        N p = searchNode(prefix);
        if(p==null){
            return false;
        }else{
            return true;
        }
    }

    public N searchNode(String s){
        N p = root;
        for(int i=0; i<s.length(); i++){
            char c= s.charAt(i);
            boolean isHas = false;
            for (N n : p.a) {
                if (n.ch == c) {
                    p = n;
                    isHas = true;
                    break;
                }
            }
            if (!isHas) {
                return null;
            }
        }

        if(p==root)
            return null;

        return p;
    }
}