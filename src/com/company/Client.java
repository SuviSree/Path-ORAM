package com.company;
import java.util.*;

public class Client {
    Server server = new Server();
    int height = server.getHeight();
    int totalNodes = (int) Math.pow(2,height+1) - 1;
    int totalPaths = (totalNodes + 1)/2;
    Map<Integer, Integer> positionMap = new HashMap<>();
    List<StashedItem> stash = new ArrayList<>();

    public void initiate() {
        Random random = new Random();
        for(int i=1; i<=totalNodes;i++) {
            int pathNumber = random.nextInt(totalPaths) + 1;
            positionMap.put(i, pathNumber);
        }
        List <Integer> overflowedData = server.initiate(positionMap);
        for(Integer i : overflowedData) {
            System.out.println("stashed : " + i);
            stash.add(new StashedItem(i, positionMap.get(i)));
            positionMap.put(i, null);
        }
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
        for(StashedItem i : stash) {
            System.out.print(i.block + "," + i.path + " ");
        }
        System.out.print("\n\n");
    }

    public void access(String operation, int block, Integer data) {
        Random random = new Random();
        int path = positionMap.get(block);
        List <Integer> pathItems = server.readPath(path);
        positionMap.put(block,random.nextInt(totalPaths) + 1);
        storeInStash(pathItems);
        Collections.shuffle(stash);
        printStash();
        printPositionMap();
        server.printTree();
        writeBack(path);
        printStash();
        printPositionMap();
        server.printTree();
    }

    public void storeInStash(List <Integer> pathItems) {
        for(Integer i : pathItems) {
            if(positionMap.get(i)!= null) {
                stash.add(new StashedItem(i, positionMap.get(i)));
                positionMap.put(i, null);
            }
        }
    }

    public void writeBack(int path) {
        for(int level = height; level>=0; level--) {
            for(StashedItem i : stash) {
                System.out.println(nodeIndexAtLevel(path, level) + " " + nodeIndexAtLevel(i.path,level));
                if(nodeIndexAtLevel(path, level) == nodeIndexAtLevel(i.path,level)) {
                    System.out.println("In stash " + level + " " +i.block + " " + i.path);
                    stash.remove(i);
                    server.insertAtPath(path, i.block);
                    break;
                }
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
