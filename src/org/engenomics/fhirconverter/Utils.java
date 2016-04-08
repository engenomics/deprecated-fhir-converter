package org.engenomics.fhirconverter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Utils {
    public static String getBetween(String s, String start, String end) {
        return s.substring(s.indexOf(start) + 1, s.indexOf(end));
    }

    public static String getAfter(String s, String start) {
        return s.substring(s.indexOf(start) + 1, s.length());
    }

    public static void writeToFile(String toWrite, String fileName) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(fileName);
        out.println(toWrite);
        out.close();
    }
}
