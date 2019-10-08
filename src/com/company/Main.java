package com.company;


import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("this is path oram");
        Client client = new Client();
        //server.print();
//        server.readPath(1);
//        server.readPath(7);
//        server.readPath(8);
        client.initiate();
        client.printPositionMap();
        client.printStash();
        client.access("read", 2, null);
        client.access("read", 2, null);
        client.access("read", 2, null);
//        client.nodeIndexAtLevel(1,3);
//        client.nodeIndexAtLevel(3,2);
//        client.nodeIndexAtLevel(6,1);
    }
}
