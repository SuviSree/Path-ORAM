package com.company;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import static com.company.FileWriter.writeLoadSizeToFile;
import static com.company.FileWriter.writeStashSizeToFile;

public class Main {

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.initiate();
        Long [] levelLoads = new Long[client.server.getHeight() + 1];
        Arrays.fill(levelLoads, 0L);
        int blockNumber = 1;
        Long maxIteration = 1000L;
        PrintWriter loadWriter = new PrintWriter(new FileOutputStream(new File("files/load.txt"), true));
        PrintWriter stashWriter = new PrintWriter(new FileOutputStream(new File("files/stash.txt"), true));
        for(int i=1; i<= maxIteration; i++) {
            System.out.println("iteration: " + i);
            client.access("read", blockNumber, null);
            writeStashSizeToFile(loadWriter, i, client.stash.size());
            //System.out.println("stash size " + client.stash.size());
            for(int level = 0;level<=client.server.getHeight(); level++) {
                levelLoads[level] += client.server.calculateLoad(level);
            }
            if(blockNumber == client.totalBlocks) {
                blockNumber = 0;
            }
            blockNumber++;
        }
        for(int i=0; i <=client.server.getHeight();i++) {
            writeLoadSizeToFile(stashWriter, i, client.server.calculateAverageLoad(levelLoads[i], i, maxIteration));
            //System.out.println("level " + i + ": " + client.server.calculateAverageLoad(levelLoads[i], i, maxIteration));
        }

    }
}
