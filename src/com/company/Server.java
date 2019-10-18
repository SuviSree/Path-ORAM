package com.company;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {

    private int height = 4;
    private int bucketSize = 4;
    private int totalBuckets = (int) Math.pow(2, height + 1) - 1;
    private int totalNodes = totalBuckets * bucketSize;
    public String tree[] = new String[totalNodes + 2];
    AES aes = AES.getInstance();

    public int getHeight() {
        return height;
    }

    public int getBucketSize() {
        return bucketSize;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public List<Integer> initiate(Map<Integer, Integer> positionMap) {
        List<Integer> stash = new ArrayList<>();
        for (int i = 0; i <= totalNodes; i++) {
            tree[i] = null;
        }
        positionMap.entrySet().forEach(entry -> {
            boolean isInserted = false;
            try {
                isInserted = insertAtPath(entry.getValue(), entry.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isInserted == false) {
                stash.add(entry.getKey());
            }
        });
        //printTree();
        return stash;
    }

    public void fillEmptyBlocksWithDummy() throws Exception {
        for (int i = 1; i <= totalNodes; i++) {
            if (tree[i] == null) {
                tree[i] = aes.encryptText("-1", aes.secretKey).toString();
            }
        }
    }

    public void printTree() {
        int prevLevelBlocks = 0;
        for (int i = 0; i <= height; i++) {
            int curBuckets = (int) Math.pow(2, i);
            int curBlocks = curBuckets * bucketSize;
//            for(int space = 0; space<2*height-2*i-1; space++) {
//                System.out.print(" ");
//            }
            for (int j = 1; j <= curBlocks; j++) {
                System.out.print(tree[prevLevelBlocks + j] + " ");
                if (j % bucketSize == 0) {
                    System.out.print("\t");
                }
            }
            prevLevelBlocks += curBlocks;
            System.out.println("");
        }
        System.out.print("\n");
    }

    // take a path number and read all the nodes of that path
    public List<BlockInfo> readPath(int path) throws Exception {
//        for(int i=1;i<=totalNodes;i++)
//            System.out.print(tree[i] + " ");
        // First get the leaf position in tree array from path number and
        // then continue dividing the position number until you get the root.
        //System.out.println("Reading path: " + path);
        int leaf = getLeafFromPath(path);
        int bucketNumber = getBucketFromPath(path);
        int nodeNumber = leaf;
        List<BlockInfo> pathItems = new ArrayList<>();
        while (bucketNumber >= 1) {
            for (int i = nodeNumber; i < nodeNumber + bucketSize; i++) {
                //String decryptedText = aes.decryptText(tree[i], aes.secretKey);
                pathItems.add(new BlockInfo(Integer.parseInt(aes.decryptText(tree[i], aes.secretKey)), i));
                tree[i] = null;
            }
            //System.out.print(nodeNumber + " " + tree[nodeNumber]);
            bucketNumber /= 2;
            nodeNumber = (bucketNumber - 1) * bucketSize + 1;
        }
//        System.out.println("");
//        System.out.println("Path: " + path);
//        for (int i = 0; i < pathItems.size(); i++) {
//            System.out.print(pathItems.get(i).blockNum + " ");
//        }
//        System.out.println("");
        return pathItems;
    }

    // return false if path is full and element not inserted
    public boolean insertAtPath(int path, int value) throws Exception {
        int leaf = getLeafFromPath(path);
        int bucketNumber = getBucketFromPath(path);
        int nodeNumber = leaf;
        while (bucketNumber >= 1) {
            for (int i = nodeNumber; i < nodeNumber + bucketSize; i++) {
                if (tree[i] == null) {
                    tree[i] = aes.encryptText(Integer.toString(value), aes.secretKey);
                    return true;
                }
            }
            bucketNumber /= 2;
            nodeNumber = (bucketNumber - 1) * bucketSize + 1;
        }
        return false;
    }

    public int getLeafFromPath(int path) {
        return (getBucketFromPath(path) - 1) * bucketSize + 1;
    }

    public int getBucketFromPath(int path) {
        return (int) Math.pow(2, height) - 1 + path;
    }

    public int calculateLoad(int level) throws Exception {
        int usedBlocks = 0;
        int blockStart = ((int) Math.pow(2, level) - 1) * bucketSize + 1;
        int blockEnd = blockStart + (int) Math.pow(2, level) * bucketSize - 1;
        for(int i=blockStart; i<=blockEnd;i++) {
            if(!aes.decryptText(tree[i], aes.secretKey).equals("-1")) {
                usedBlocks++;
            }
        }
        //System.out.println(usedBlocks);
        return usedBlocks;
    }

    public double calculateAverageLoad(Long load, int level, Long maxIteration) {
        int blocksInLevel = (int) Math.pow(2, level) * bucketSize;
        return load * 100.0/(blocksInLevel * maxIteration);
    }
    
}


