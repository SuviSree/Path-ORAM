package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileWriter {
    public static void writeStashSizeToFile( PrintWriter writer, int iteration, int size) throws FileNotFoundException, UnsupportedEncodingException {
        writer.println(iteration + " " + size);
        writer.close();
    }

    public static void writeLoadSizeToFile(PrintWriter writer, int level, double load) throws FileNotFoundException, UnsupportedEncodingException {
        writer.println(level + " " + load);
        writer.close();
    }
}
