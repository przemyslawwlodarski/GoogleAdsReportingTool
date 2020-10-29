package com.pfx.demo.workplace;

import java.io.File;
import java.io.PrintWriter;

public class CsvTest {
    public static void main( String[] args ) {
        try{
            File file = new File("fileName.csv");
            PrintWriter pw = new PrintWriter(file);

            StringBuilder sb = new StringBuilder();
            sb.append("test"+ ","+ "1000");
            sb.append("test");
            sb.append("test");
            sb.append("test");
            sb.append("\n");
            sb.append(1000);

            pw.write(sb.toString());
            pw.close();
        } catch (Exception e) {

        }
    }
}
