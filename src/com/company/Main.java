package com.company;


public class Main {

    public static void main(String[] args) {
        Client client = new Client();
        client.initiate();
        client.printPositionMap();
        client.printStash();
        client.printDummies();
        client.access("read", 2, null);
        client.access("read", 2, null);
        client.access("read", 2, null);
    }
}
