package edu.neu.madcourse.entingwu.wordgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomPathGenerator {
    public static HashMap<Integer, List<Integer>> adjs = new HashMap<Integer, List<Integer>>();
    public static  List<List<Integer>> result = new ArrayList<>();

    // 0 1 2
    // 3 4 5
    // 6 7 8
   static {
        List<Integer> adj0 = new ArrayList<Integer>();adj0.add(1);adj0.add(3);adj0.add(4);
        List<Integer> adj1 = new ArrayList<Integer>();adj1.add(0);adj1.add(2);adj1.add(3);adj1.add(4);adj1.add(5);
        List<Integer> adj2 = new ArrayList<Integer>();adj2.add(1);adj2.add(4);adj2.add(5);
        List<Integer> adj3 = new ArrayList<Integer>();adj3.add(0);adj3.add(1);adj3.add(4);adj3.add(6);adj3.add(7);
        List<Integer> adj4 = new ArrayList<Integer>();adj4.add(0);adj4.add(1);adj4.add(2);adj4.add(3);adj4.add(5);adj4.add(6);adj4.add(7);adj4.add(8);
        List<Integer> adj5 = new ArrayList<Integer>();adj5.add(1);adj5.add(2);adj5.add(4);adj5.add(7);adj5.add(8);
        List<Integer> adj6 = new ArrayList<Integer>();adj6.add(3);adj6.add(4);adj6.add(7);
        List<Integer> adj7 = new ArrayList<Integer>();adj7.add(3);adj7.add(4);adj7.add(5);adj7.add(6);adj7.add(8);
        List<Integer> adj8 = new ArrayList<Integer>();adj8.add(4);adj8.add(5);adj8.add(7);
        adjs.put(0, adj0);adjs.put(1, adj1);adjs.put(2, adj2);adjs.put(3, adj3);adjs.put(4, adj4);adjs.put(5, adj5);
        adjs.put(6, adj6); adjs.put(7, adj7); adjs.put(8, adj8);
       List<Integer> path = new ArrayList<Integer>();
       for (int i = 0; i < 9; ++ i) {
           path.add(i);
           recFind(path, i, adjs, result);
           path.remove(path.size() -1 );
       }
    }

    public static List<Integer> randomAllocation(){
        return result.get(new Random().nextInt(result.size()));
    }

    public static void recFind(List<Integer> path, int lastStep, Map<Integer, List<Integer>> adj, List<List<Integer>> result) {
        if (path.size() == 9) {
            result.add(new ArrayList<Integer>(path));
            return;
        }
        for (Integer adjInt: adj.get(lastStep)) {
            if (path.contains(adjInt)) {
                continue;
            } else {
                path.add(adjInt);
                recFind(path, adjInt, adj, result);
                path.remove(path.size() -1 );
            }
        }
    }
}
