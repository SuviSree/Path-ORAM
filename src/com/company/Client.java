package com.company;
import java.util.*;

public class Client {
    Server server = new Server();
    int height = server.getHeight();
    int totalNodes = (int) Math.pow(2,height+1) - 1;
    int totalPaths = (totalNodes + 1)/2;
    Map<Integer, Integer> positionMap = new HashMap<>();
    List<Integer> stash = new ArrayList<>();
    HashMap<Integer, Integer> dummyBlocks = new HashMap();

    public void initiate() {
        Random random = new Random();
        for(int i=1; i<=totalNodes;i++) {
            int pathNumber = random.nextInt(totalPaths) + 1;
            positionMap.put(i, pathNumber);
        }
        List <Integer> overflowedData = server.initiate(positionMap);
        for(Integer i : overflowedData) {
            System.out.println("stashed : " + i);
            stash.add(i);
        }
        dummyBlocks = server.fillEmptyBlocksWithDummy();
    }

    public void printPositionMap() {
        System.out.println("\nPosition Map");
        System.out.print("Block:\t");
        positionMap.entrySet().forEach(entry->{
            System.out.print(entry.getKey() + "\t");
        });
        System.out.println();
        System.out.print("Path:\t");
        positionMap.entrySet().forEach(entry->{
            System.out.print(entry.getValue() + "\t");
        });
        System.out.println("");
    }

    public void printStash() {
        System.out.println("\n\nStash");
        for(Integer i : stash) {
            System.out.print(i + " ");
        }
        System.out.print("\n\n");
    }

    public void printDummies() {
        System.out.println("\n\nDummy block positions");
        dummyBlocks.entrySet().forEach(entry->{
            System.out.print(entry.getKey() + "\t");
        });
        System.out.print("\n\n");
    }

    public void access(String operation, int block, Integer data) {
        Random random = new Random();
        int path = positionMap.get(block);
        List <BlockInfo> pathItems = server.readPath(path);
        positionMap.put(block,random.nextInt(totalPaths) + 1);
        storeInStash(pathItems);
        Collections.shuffle(stash);
        printStash();
        //printPositionMap();
        //printDummies();
        server.printTree();
        writeBack(path);
        printStash();
        //printPositionMap();
        server.printTree();
    }

    public void storeInStash(List <BlockInfo> pathItems) {
        for(BlockInfo i : pathItems) {
            if(dummyBlocks.get(i.blockPos) == null) {
                stash.add(i.blockNum);
            }
        }
    }

    public void writeBack(int path) {
        Random r = new Random();
        for(int level = height; level>=0; level--) {
            boolean isInserted = false;
            for(Integer i : stash) {
                //System.out.println(nodeIndexAtLevel(path, level) + " " + nodeIndexAtLevel(i.path,level));
                if(nodeIndexAtLevel(path, level) == nodeIndexAtLevel(positionMap.get(i),level)) {
                    //System.out.println("In stash level " + level + " " +i + " ");
                    stash.remove(i);
                    server.insertAtPath(path, i);
                    isInserted = true;
                    break;
                }
            }
            if(isInserted == false) {
                server.insertAtPath(path, r.nextInt(totalNodes) + 1);
                dummyBlocks.put(nodeIndexAtLevel(path, level),1);
            }
        }
    }

    public Integer nodeIndexAtLevel (int path, int level) {
        int leaf = server.getLeafFromPath(path);
        int height = server.getHeight();
        for(int i = height; i > level; i--) {
            //System.out.println(i + " " + height + " " +level);
            leaf /= 2;
        }
        //System.out.println("node is " + leaf);
        return leaf;
    }
}
