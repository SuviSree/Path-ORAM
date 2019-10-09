package com.company;


import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Client client = new Client();
        Server server = new Server();
        //server.print();
//        server.readPath(1);
//        server.readPath(7);
//        server.readPath(8);
        client.initiate();
        client.printPositionMap();
        client.printStash();
        client.printDummies();
        client.access("read", 2, null);
        client.access("read", 2, null);
        client.access("read", 2, null);
//        client.bucketIndexAtLevel(1,2);
//        client.bucketIndexAtLevel(3,2);
//        client.bucketIndexAtLevel(6,5);
    }
}
