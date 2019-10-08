package com.company;


import java.util.*;

public class Server {

    private int height = 3;
    int totalNodes = (int) Math.pow(2, height+1) - 1;
    public Integer tree[] = new Integer[totalNodes+2];

    public int getHeight() {
        return height;
    }

    public List<Integer> initiate(Map<Integer, Integer> positionMap) {
        Random r = new Random();
        List<Integer> stash = new ArrayList<>();
        for(int i=0;i<=totalNodes;i++) {
            tree[i] = null;
        }
        positionMap.entrySet().forEach(entry->{
            boolean isInserted = insertAtPath(entry.getValue(), entry.getKey());
            if(isInserted == false) {
                stash.add(entry.getKey());
            }
        });
        printTree();
        return stash;
    }

    public HashMap<Integer, Integer> fillEmptyBlocksWithDummy() {
        Random r = new Random();
        HashMap<Integer, Integer> dummies = new HashMap<>();
        for(int i=1;i<=totalNodes;i++) {
            if(tree[i] == null) {
                tree[i] = r.nextInt(totalNodes) + 1;
                dummies.put(i, 1);
            }
        }
        return dummies;
    }

    public void printTree() {
        for(int i=0; i <= height; i++) {
            int nodes_of_level = (int) Math.pow(2, i);
            for(int space = 0; space<2*height-2*i-1; space++) {
                System.out.print(" ");
            }
            for (int j = 0; j < nodes_of_level; j++) {
                System.out.print(tree[nodes_of_level + j] + "  ");
            }
            System.out.println();
        }
    }

    // take a path number and read all the nodes of that path
    public List<BlockInfo> readPath(int path) {
//        for(int i=1;i<=totalNodes;i++)
//            System.out.print(tree[i] + " ");
        // First get the leaf position in tree array from path number and
        // then continue dividing the position number until you get the root.
        int leaf = getLeafFromPath(path);
        List<BlockInfo> pathItems = new ArrayList<>();
        int nodeNumber = leaf;
        while(nodeNumber >= 1) {
            pathItems.add(new BlockInfo(tree[nodeNumber], nodeNumber));
            tree[nodeNumber]=null;
            //System.out.print(nodeNumber + " " + tree[nodeNumber]);
            nodeNumber/=2;
        }
        System.out.println("Path: " + path);
        for(int i =0;i<pathItems.size();i++) {
            System.out.print(pathItems.get(i).blockNum + " ");
        }
        System.out.println("\n\n");
        return pathItems;
    }

    // return false if path is full and element not inserted
    public boolean insertAtPath(int path, int value) {
        int leaf = getLeafFromPath(path);
        int nodeNumber = leaf;
        while(nodeNumber >= 1) {
            if(tree[nodeNumber]==null) {
                tree[nodeNumber] = value;
                return true;
            }
            nodeNumber/=2;
        }
        return false;
    }

    public int getLeafFromPath(int path) {
        return (int) Math.pow(2, height) - 1 + path;
    }
}


