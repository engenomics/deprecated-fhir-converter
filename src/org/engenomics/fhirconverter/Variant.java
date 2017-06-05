package org.engenomics.fhirconverter;

import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Variant {
	private int start;
    private int end;
    private String referenceAllele;
    private String observedAllele;
    
    
	public Variant() {
		// TODO Auto-generated constructor stub
	}
	public Variant(int start, int end, String referenceAllele, String observedAllele){
		this.start = start;
		this.end = end;
		this.referenceAllele = referenceAllele;
		this.observedAllele = observedAllele;
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	public String getReferenceAllele() {
		return referenceAllele;
	}
	public void setReferenceAllele(String referenceAllele) {
		this.referenceAllele = referenceAllele;
	}
	
	public String getObservedAllele() {
		return observedAllele;
	}
	public void setObservedAllele(String observedAllele) {
		this.observedAllele = observedAllele;
	}
	
	public JSONObject getFhirVarJsonObj() throws JSONException {
		LinkedHashMap<String, Object> variant = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> extension = new LinkedHashMap<String, Object>();
		extension.put("url", "allelePhase");
		JSONArray coding = new JSONArray();
		LinkedHashMap<String, Object> c = new LinkedHashMap<String, Object>();
		c.put("system", "http://www.loinc.org");
		c.put("code", "LA4489-6");
		c.put("display", "Unknown");
		coding.put(new JSONObject(c));
		JSONObject valueCodeableConcept = new JSONObject();
		valueCodeableConcept.put("coding", coding);
		extension.put("valueCodeableConcept", valueCodeableConcept);
		variant.put("extension", new JSONArray().put(new JSONObject(extension)));
		
		variant.put("start", Integer.valueOf(this.getStart()));
		variant.put("end", Integer.valueOf(this.getEnd()));
		variant.put("observedAllele", this.getObservedAllele());
		variant.put("referenceAllele", this.getReferenceAllele());
		
		return new JSONObject(variant);		
	}

}
