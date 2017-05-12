package org.engenomics.fhirconverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    private static SeqGraphVCF graph = new SeqGraphVCF();

    public static void main(String[] args) throws IOException {
        new Main().createFile();
        new Main().runSGVtoJSON();
    }

    private void createFile() {
        //System.out.println(graph.fileHeader);
        graph.createSequenceGraphVCF(10);
    }

    private void runSGVtoJSON() throws IOException {
        SGVtoJSON converter = new SGVtoJSON(graph.readFilePath);
        String home = System.getProperty("user.dir");
        converter.convert(home+ File.separator + "private" + File.separator + "output.txt");

        List<JSONObject> jsonFiles = converter.getObjs();

        String UUID = converter.getSequences().get(0).getUUID();

        new File(home+ File.separator + "private" + File.separator + "jsonoutput").mkdirs();
        
        try {
			Utils.writeToFile(Utils.getFhirBundle(jsonFiles).toString(4), 
					home+ File.separator + "private" + File.separator + "jsonoutput" + File.separator + UUID + "-sequences.json");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println(System.getProperty("user.dir") + "/private/jsonoutput");
        /*for (int i = 0; i < jsonFiles.size(); i++) {
            JSONObject jsonObject = jsonFiles.get(i);
            try {
				Utils.writeToFile(jsonObject.toString(4), home+ File.separator + "private" + File.separator + "jsonoutput" + File.separator + i + ".json");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
    }
}
