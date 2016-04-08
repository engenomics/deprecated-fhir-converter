package org.engenomics.fhirconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class SeqGraphVCF {
    public String fileHeader = "WinStart\tWinEnd\tVar";

    public String readFilePath = "C:\\Users\\Andrew\\workspace\\PRIMES\\data\\vcf\\chr22\\test2.vcf";
    public String outputFilePath = "C:\\Users\\Andrew\\workspace\\PRIMES\\fhir-converter\\output.txt";

    private BufferedWriter writer;

    public void openWriter() {

        try {
            writer = new BufferedWriter(new FileWriter(outputFilePath));
        } catch (Exception e) {
            System.out.println("unsuccessful open");
        }
    }

    public void closeWriter() {

        try {
            writer.close();
        } catch (Exception e) {
            System.out.println("unsucessful close");
        }
    }

    public void writeToFile(String ln) {

        try {
            writer.write(ln);
            writer.newLine();
        } catch (Exception e) {
            System.out.println("unsuccessful write");
        }
    }

    public void createSequenceGraphVCF(int windowSize) {

        try {
            BufferedReader buf = new BufferedReader(new FileReader(readFilePath));
            List<String> words = new ArrayList<>();
            String lineFetched = "";
            String[] wordsArray;
            openWriter();
            writeToFile(fileHeader);

            while (true) {

                String line = "";
                lineFetched = buf.readLine();
                if (lineFetched == null) break;
                else {
                    wordsArray = lineFetched.split("\t");
                    for (String each : wordsArray) {
                        if (!"".equals(each)) words.add(each);
                    }
                }

                if (wordsArray.length < 2 || lineFetched.contains("#")) {
                } else {
                    int startKMerPosition = Integer.parseInt(wordsArray[1]) - Integer.parseInt(wordsArray[1]) % windowSize;
                    int endKMerPosition = startKMerPosition + windowSize;

                    if (wordsArray[2].contains("rs"))
                        wordsArray[2] = wordsArray[0] + ":" + wordsArray[1] + wordsArray[3] + ">" + wordsArray[4];
                    line += startKMerPosition + "\t" + "\t" + endKMerPosition + "\t" + wordsArray[2];

                    writeToFile(line);

                }
            }

            buf.close();
            closeWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
