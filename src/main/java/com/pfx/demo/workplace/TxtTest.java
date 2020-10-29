package com.pfx.demo.workplace;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class TxtTest {
    public static void main( String[] args ) {
        try {
            File file = new File("fileName.txt");
             if (!file.exists()) {
                 file.createNewFile();
             }

             PrintWriter pw = new PrintWriter(file);
             pw.println("Test");
             pw.println(1000);
             pw.close();
             pw.flush();
             System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NotDone");
        }
    }
}
