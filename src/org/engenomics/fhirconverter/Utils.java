package org.engenomics.fhirconverter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    
    public static JSONObject getFhirBundle(List<JSONObject> entries){
    	JSONObject bundle = new JSONObject();
    	
    	try {
			bundle.put("resourceType", "Bundle");
			bundle.put("type", "collection");
	    	
	    	JSONArray entry = new JSONArray();
	    	for(JSONObject e : entries){
	    		entry.put(new JSONObject().put("resource", e));
	    	}
	    	bundle.put("entry", entry);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	return bundle;
    }
}
