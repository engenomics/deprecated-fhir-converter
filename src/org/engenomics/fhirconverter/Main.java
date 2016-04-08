package org.engenomics.fhirconverter;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    private static SeqGraphVCF graph = new SeqGraphVCF();

    public static void main(String[] args) throws IOException {
//        new Main().createFile();
        new Main().runSGVtoJSON();
    }

    private void createFile() {
        System.out.println(graph.fileHeader);
        graph.createSequenceGraphVCF(10);
    }

    private void runSGVtoJSON() throws IOException {
        SGVtoJSON converter = new SGVtoJSON(graph.readFilePath);
        converter.convert("C:\\Users\\Andrew\\workspace\\PRIMES\\fhir-converter\\sampleSeqGraphOutput.txt");

        List<JSONObject> jsonFiles = converter.getObjs();

        System.out.println(jsonFiles.get(0));

        new File(System.getProperty("user.dir") + "/jsonoutput").mkdirs();

        for (int i = 0; i < jsonFiles.size(); i++) {
            JSONObject jsonObject = jsonFiles.get(i);
            Utils.writeToFile(jsonObject.toString(4), System.getProperty("user.dir") + "/jsonoutput/" + i + ".json");
        }
    }
}
