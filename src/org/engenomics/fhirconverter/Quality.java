package org.engenomics.fhirconverter;

import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

class Quality {
	private double score;
	private int start;
	private int end;

	public Quality() {
		// TODO Auto-generated constructor stub
	}
	public Quality(double score, int start, int end){
		this.score = score;
		this.start = start;
		this.end = end;
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
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	public JSONObject getFhirQualObj(){
		LinkedHashMap<Object, Object> quality = new LinkedHashMap<Object, Object>();
		try {
			quality.put("type", "unknown");
			quality.put("start", Integer.valueOf(this.getStart()));
			quality.put("end", Integer.valueOf(this.getEnd()));
			quality.put("score", new JSONObject().put("value", this.getScore()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return new JSONObject(quality);
		
	}

}
