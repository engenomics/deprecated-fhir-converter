package org.engenomics.fhirconverter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
    
    
    public static JSONObject getFhirBundle(List<JSONObject> entries, Patient patient){
    	JSONObject bundle = new JSONObject();
    	
    	try {
			bundle.put("resourceType", "Bundle");
			bundle.put("type", "transaction");
	    	
	    	JSONArray entry = new JSONArray();
	    	entry.put(patient.getFhirPatientObj());
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
    
    public static String postFhirData(String resource, JSONObject data, String id){
    	String resourceID = "";
    	String serverUrl = "http://genomic_fhir_server_url/baseDstu3/"+ resource;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(serverUrl);
		CloseableHttpResponse response = null;
		Scanner in = null;
    	try{
    		post.setEntity(new StringEntity(data.toString(), "UTF8"));
    		post.setHeader("Content-type", "application/json");
    		//post.setEntity(new UrlEncodedFormEntity(params));
    		response = httpClient.execute(post);
    		HttpEntity entity = response.getEntity();
    		in = new Scanner(entity.getContent());
    		while(in.hasNext()){
    			String line = in.next();
    			System.out.println(line);
    			if (line.contains(resource)){
    				line = line.replaceAll("\"", "");
    				resourceID = line.replaceAll("\\\\", "");
    			}
    		}
    		EntityUtils.consume(entity);
    		
    	}catch(ClientProtocolException e){
    		e.printStackTrace();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	return resourceID;
    }
    
    public static JSONObject getFhirData(String resource, String type, String value){
    	JSONObject data = null;
    	StringBuilder sb = new StringBuilder();
    	String url = "http://genomic_fhir_server_url/baseDstu3/"+resource+"?"+type+"="+value;
    	//String url = "http://genomics-advisor.smartplatforms.org:7080/baseDstu3/"+resource+"?"+type+"="+value;
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	HttpGet get = new HttpGet(url);
    	
    	get.addHeader("accept", "application/json");
    	
    	HttpResponse response;
		try {
			response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() != 200){
	    		throw new RuntimeException("Failed : HTTP error code : " 
	    				+ response.getStatusLine().getStatusCode());
	    	}
			
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
			String output;
			System.out.println("Output from Server ....\n");
			while((output = br.readLine())!= null){
				System.out.println(output);
				sb.append(output+ "\n");
				//data.add(new JSONObject(br));
				//System.out.println("*****");
			}
			 data = new JSONObject(sb.toString());	
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	return data;
    }
}
