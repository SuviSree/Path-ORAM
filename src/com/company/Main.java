package com.company;


import java.io.*;
import java.util.Arrays;


public class Main {

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.initiate();
        Long [] levelLoads = new Long[client.server.getHeight() + 1];
        Arrays.fill(levelLoads, 0L);
        int blockNumber = 1;
        BufferedWriter loadWriter = new BufferedWriter(new FileWriter(new File("files/load.txt"), false));
        // warm up
        int warmUpIteration = 10000;
        System.out.println("warming up");
        for(int i=1; i<=warmUpIteration; i++) {
            client.access("read", blockNumber, null);
            if(blockNumber == client.totalBlocks) {
                blockNumber = 0;
            }
            blockNumber++;
        }
        System.out.println("warm up done");

        blockNumber = 1;
        Long maxIteration = 10000L;
        int maxStashSize = 0;

        double iterationStartTime = System.currentTimeMillis();
        double totalElapsedTime = 0;
        for(int i=1; i<= maxIteration; i++) {
            //System.out.println("iteration: " + i);

            // accessing block

            long start = System.currentTimeMillis();
            client.access("read", blockNumber, null);
            long end = System.currentTimeMillis();
            totalElapsedTime += (end - start);

            // check stash size
            int sz = client.stash.size();
            if(sz > maxStashSize) {
                maxStashSize = sz;
            }

            // calculate load of each level
            for(int level = 0;level<=client.server.getHeight(); level++) {
                levelLoads[level] += client.server.calculateLoad(level);;
            }

            if(blockNumber == client.totalBlocks) {
                blockNumber = 0;
            }
            blockNumber++;
        }
        double iterationEndTime = System.currentTimeMillis();

        for(int i=0; i <=client.server.getHeight();i++) {
            loadWriter.write(i + " " + client.server.calculateAverageLoad(levelLoads[i], i, maxIteration) + "\n");
        }
        loadWriter.close();

        System.out.println("max stash size: " + maxStashSize);
        System.out.println("Total elapsed time for " + maxIteration + " iterations: " + (iterationEndTime-iterationStartTime)/1000.0 + " seconds");
        System.out.println("Average execution time: " + totalElapsedTime/(maxIteration * 1000.0) + " seconds");
    }

}
