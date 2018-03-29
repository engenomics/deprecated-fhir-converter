package org.engenomics.fhirconverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class Main {
    private static SeqGraphVCF graph = new SeqGraphVCF();
    private static String[] patientInfo = new String[]
	 {"General_IDxxx", 
	  "MRNxxx",
	  "LastName", 	//Last name
	  "FirstName",	//First name
	  "Gender",
	  "MM/DD/YYYY" //date of birth
	 };

    public static void main(String[] args) throws IOException {
    	new File(System.getProperty("user.dir") + File.separator + "private").mkdirs();
        new Main().createFile();
        new Main().runSGVtoJSON();
    	//new Main().getFHIRData();
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
    private void getFHIRData() throws FileNotFoundException{
    	String home = System.getProperty("user.dir");
    	JSONObject jsonObject = Utils.getFhirData("Sequence", "identifier", "43481510240374");
    	
		new File(home+ File.separator + "out" + File.separator + "sequences").mkdirs();
    	
            //add patient information to sequence Object;
           // jsonObject.put("patient", patient.getReferenceFormate(patientFhirID));
		try {
			Utils.writeToFile(jsonObject.toString(4), home+ File.separator + "out" + File.separator + "sequences" + File.separator + "sequence.json");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            //Utils.postFhirData("Sequence", jsonObject, null);
	    
    }
    
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
        	Patient patient = new Patient(
            patientInfo[0],
					  patientInfo[1],
					  patientInfo[2],
					  patientInfo[3],
					  patientInfo[4],
					  new SimpleDateFormat("dd/MM/yyyy").parse(patientInfo[5]));

        	Utils.writeToFile(patient.getFhirPatientObj().toString(4),
					home+ File.separator + "private" + File.separator + "patient" + File.separator + patient.getId() + "-patient.json");
        	//String patientFhirID = "aa";
        	String patientFhirID = Utils.postFhirData("Patient", patient.getFhirPatientObj(), patient.getId());
        	
        	Utils.writeToFile(Utils.getFhirBundle(seqJsonFiles, patient).toString(4),
					home+ File.separator + "private" + File.separator + "bundle" + File.separator + UUID + "-sequences.json");
			
        	//
        	if (!patientFhirID.isEmpty() || !patientFhirID.equals("")){
	        	for (int i = 0; i < seqJsonFiles.size(); i++) {
	                JSONObject jsonObject = seqJsonFiles.get(i);
	                //add patient information to sequence Object;
	                jsonObject.put("patient", patient.getReferenceFormate(patientFhirID));
	    			Utils.writeToFile(jsonObject.toString(4), home+ File.separator + "private" + File.separator + "sequences" + File.separator + i + ".json");
	                Utils.postFhirData("Sequence", jsonObject, null);
	    			System.out.println(i);
	            }
	        	System.out.println("FILES POSTED SUCCESSFULLY!");
        	}else{
        		System.out.println("Build FAILE!");
        	}

        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }
}
