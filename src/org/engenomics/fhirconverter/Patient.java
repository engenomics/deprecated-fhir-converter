package org.engenomics.fhirconverter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Patient {
	private String id;
	private String mrn; //vcfUid in this case
	private String familyName;
	private String givenName;
	private String gender;
	private Date birthDate;
	
	public Patient() {
		// TODO Auto-generated constructor stub
	}
	
	//create Patient data without mrn number
	public Patient(String id, String mrn, String familyName, String givenName, String gender, Date birthDate){
		this.id = id;
		this.familyName = familyName;
		this.givenName = givenName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.mrn = mrn;
		
	}
	
	public String getId(){
		return this.id;
	}
	public void setID(String id){
		this.id = id;
	}
			
	public String getMrn(){
		return this.mrn;
	}
	public void setMrn(String mrn){
		this.mrn = mrn;
	}
	
	public JSONObject getFhirMrnObj(){
		JSONObject mrnFhir = new JSONObject();
		
		try{
    		
    		JSONArray coding = new JSONArray();
    		JSONObject type = new JSONObject();
    		coding.put(new JSONObject()
    				.put("system", "http://hl7.org/fhir/v2/0203")
    				.put("code", "MR")
    				.put("display", "Medical Record Number"));
    		type.put("coding", coding);
    		mrnFhir.put("type", type);
    		mrnFhir.put("system", "http://hospital.smarthealthit.org");
    		mrnFhir.put("value", this.mrn);
    		
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
		
		
		return mrnFhir;
	}
	
	public String getFamilyName(){
		return this.familyName;
	}
	public void setFamilyName(String familyName){
		this.familyName = familyName;
	}
	
	public String getGivenName(){
		return this.givenName;
	}
	public void setGivenName(String givenName){
		this.givenName = givenName;
	}
	
	public JSONObject getFhirNameObj(){
		JSONObject name = new JSONObject();
		
		try {
			name.put("family", this.familyName);
			name.put("given", new JSONArray()
								.put(this.givenName));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return name;
	}
	
	public String getGender(){
		return this.gender;
	}
	public void setGender(String gender){
		this.gender = gender;
	}
	
	public Date getBirthDate(){
		return birthDate;
	}
	public void setBirthDate(Date birthDate){
		this.birthDate = birthDate;
	}

	public JSONObject getReferenceFormate(String reference){
		JSONObject patientRef = new JSONObject();
		
		try {
			patientRef.put("reference", reference);
			patientRef.put("display", this.givenName + " " + this.familyName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patientRef;
	}
	
	public JSONObject getFhirPatientObj(){
		JSONObject patient = new JSONObject();
		
		try {
			patient.put("resourceType", "Patient");
			patient.put("id", this.id);
			
			//Identifier section
			JSONArray identifier = new JSONArray();
			//MRN Identifier insert
			identifier.put(this.getFhirMrnObj());	
			patient.put("identifier", identifier);
			
			//name section
			JSONArray name = new JSONArray();
			name.put(this.getFhirNameObj());
			patient.put("name", name);
			
			//gender section
			patient.put("gender", this.gender);
			
			//birthDate section
			patient.put("birthDate", new SimpleDateFormat("yyyy-MM-dd").format(this.birthDate));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patient;
	}
}
