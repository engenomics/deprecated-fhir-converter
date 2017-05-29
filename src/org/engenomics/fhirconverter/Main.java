package org.engenomics.fhirconverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class Main {
    private static SeqGraphVCF graph = new SeqGraphVCF();
    private static String[] patientInfo = new String[]
	 {"General_ID123",
	  "MRN123",
	  "FAMILY NAME",
	  "GIVEN NAME",
	  "female",
	  "01/01/1999"
	 };

    public static void main(String[] args) throws IOException {
        new Main().createFile();
        new Main().runSGVtoJSON();
    }
    
    /*
     * Calling the SeqGraphVCF class, 
     * and Loading the vcf file and convert to a csv format for later convertion use
     */
    private void createFile() {
        //System.out.println(graph.fileHeader);
        graph.createSequenceGraphVCF();
    }
    
    /*
     * Convert to JSON format and have a Bundle format and Sequences format
     */
    private void runSGVtoJSON() throws IOException {
        SGVtoJSON converter = new SGVtoJSON();
        String home = System.getProperty("user.dir");
        converter.convert(home+ File.separator + "private" + File.separator + "output.txt");

        List<JSONObject> seqJsonFiles = converter.getObjs();
        
        String UUID = converter.getSequences().get(0).getUUID();

        new File(home+ File.separator + "private" + File.separator + "bundle").mkdirs();
        new File(home+ File.separator + "private" + File.separator + "sequences").mkdirs();
        new File(home+ File.separator + "private" + File.separator + "patient").mkdirs();
        
        try {
        	//patient creation
        	Patient patient = new Patient(patientInfo[0],
					  patientInfo[1],
					  patientInfo[2],
					  patientInfo[3],
					  patientInfo[4],
					  new SimpleDateFormat("dd/MM/yyyy").parse(patientInfo[5]));
        	
        	Utils.writeToFile(patient.getFhirPatientObj().toString(4), 
					home+ File.separator + "private" + File.separator + "patient" + File.separator + patient.getId() + "-patient.json");
			
        	String patientFhirID = Utils.postFhirData("Patient", patient.getFhirPatientObj(), patient.getId());
        	/*
        	Utils.writeToFile(Utils.getFhirBundle(seqJsonFiles).toString(4), 
					home+ File.separator + "private" + File.separator + "bundle" + File.separator + UUID + "-sequences.json");
			*/
        	/*
        	for (int i = 0; i < seqJsonFiles.size(); i++) {
                JSONObject jsonObject = seqJsonFiles.get(i);
                //add patient information to sequence Object;
                jsonObject.put("patient", patient.getReferenceFormate(patientFhirID));
    			//Utils.writeToFile(jsonObject.toString(4), home+ File.separator + "private" + File.separator + "sequences" + File.separator + i + ".json");
                //Utils.postFhirData("Sequence", jsonObject, null);
            }*/
        
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println(System.getProperty("user.dir") + "/private/jsonoutput");
        
    }
}
