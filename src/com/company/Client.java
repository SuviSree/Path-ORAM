package com.company;

import java.util.*;

public class Client {
    Server server = new Server();
    int height = server.getHeight();
    int totalNodes = server.getTotalNodes();
    int totalPaths = (int) Math.pow(2, server.getHeight());
    int totalBlocks = (int) Math.pow(2, server.getHeight()+1);
    Map<Integer, Integer> positionMap = new HashMap<>();
    List<Integer> stash = new ArrayList<>();

    public void initiate() throws Exception {
        System.out.println("INITIALIZATION");
        Random random = new Random();
        for (int i = 1; i <= totalBlocks; i++) {
            int pathNumber = random.nextInt(totalPaths) + 1;
            positionMap.put(i, pathNumber);
        }
        List<Integer> overflowedData = server.initiate(positionMap);
        for (Integer i : overflowedData) {
            stash.add(i);
        }
        server.fillEmptyBlocksWithDummy();
        //server.printTree();
    }

    public void printPositionMap() {
        System.out.println("\nPosition Map");
        System.out.print("Block:\t");
        positionMap.entrySet().forEach(entry -> {
            System.out.print(entry.getKey() + "\t");
        });
        System.out.println();
        System.out.print("Path:\t");
        positionMap.entrySet().forEach(entry -> {
            System.out.print(entry.getValue() + "\t");
        });
        System.out.println("");
    }

    public void printStash() {
        System.out.println("\n\nStash");
        for (Integer i : stash) {
            System.out.print(i + " ");
        }
        System.out.print("\n\n");
    }

    public void access(String operation, int block, Integer data) throws Exception {
        System.out.println("ACCESSING BLOCK: " + block);
        Random random = new Random();
        int path = positionMap.get(block);
        List<BlockInfo> pathItems = server.readPath(path);
        positionMap.put(block, random.nextInt(totalPaths) + 1);
        storeInStash(pathItems);
        //Collections.shuffle(stash);
        //printStash();
        //printPositionMap();
        //server.printTree();
        writeBack(path);
        //System.out.println("load: " + String.format("%.2f", server.calculateLoad(0)));
        //printStash();
        //server.printTree();
    }

    public void storeInStash(List<BlockInfo> pathItems) {
        for (BlockInfo i : pathItems) {
            if (i.blockNum != -1) {
                stash.add(i.blockNum);
            }
        }
    }

    public void writeBack(int path) throws Exception {
        //System.out.println("Writing to path: " + path);
        Random r = new Random();
        for (int level = height; level >= 0; level--) {
            for (int k = 1; k <= server.getBucketSize(); k++) {
                boolean isInserted = false;
                for (Integer i : stash) {
                    //System.out.println(nodeIndexAtLevel(path, level) + " " + nodeIndexAtLevel(i.path,level));
                    if (bucketIndexAtLevel(path, level) == bucketIndexAtLevel(positionMap.get(i), level)) {
                        //System.out.println("In stash level " + level + " " +i + " ");
                        stash.remove(i);
                        server.insertAtPath(path, i);
                        isInserted = true;
                        break;
                    }
                }
                if (isInserted == false) {
                    server.insertAtPath(path, -1);
                }
            }
        }
    }

    public Integer bucketIndexAtLevel(int path, int level) {
        int bucket = server.getBucketFromPath(path);
        int height = server.getHeight();
        for (int i = height; i > level; i--) {
            //System.out.println(i + " " + height + " " +level);
            bucket /= 2;
        }
        //System.out.println("bucket is " + bucket);
        return bucket;
    }
}
