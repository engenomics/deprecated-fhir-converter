package org.engenomics.fhirconverter;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sewi {
	private String UUID;
    private int windowStart;
    private int windowEnd;
	private List<Variant> variants = new ArrayList<>();
    private String chromosome;
    private List<Quality> qualities = new ArrayList<>();
    private String genomeBuild;

    public Sewi() throws JSONException {
    }
    
    public Sewi(String[] sgoStrLine) throws JSONException {
    	if (sgoStrLine.length == 9){
	    	this.chromosome = sgoStrLine[0];
			this.windowStart = Integer.parseInt(sgoStrLine[1]);
			this.windowEnd = Integer.parseInt(sgoStrLine[2]);
			
			
			int position = Integer.parseInt(sgoStrLine[3]);
			String[] allele = sgoStrLine[4].split(">");
			String[] GTs = sgoStrLine[5].split("/|\\|");
			
			for(String GT : GTs){
				if (!GT.equals(".")){
					Variant variant = new Variant();
					variant.setStart(position);
					variant.setEnd(position);
					variant.setReferenceAllele(allele[0]);
					if (!GT.equals("0"))
						variant.setObservedAllele(allele[1].split(",")[Integer.parseInt(GT)-1]);
					else
						variant.setObservedAllele(allele[0]);
					
					this.variants.add(variant);
				}
			}
			if (!sgoStrLine[6].equals(".")){
				double qualityScore = Double.parseDouble(sgoStrLine[6]);
				Quality quality = new Quality(qualityScore, position, position);
				this.qualities.add(quality);
			}
			
			
			
			this.genomeBuild = sgoStrLine[7];
			this.UUID = sgoStrLine[8];
    	}
    }
    
    public String getUUID(){
    	return UUID;
    }
    public void setUUID(String UUID){
    	this.UUID = UUID;
    }
    
    public int getWindowStart() {
    	return windowStart;
    }
    public void setWindowStart(int windowStart) {
    	this.windowStart = windowStart;
    }
    
    public int getWindowEnd() {
    	return windowEnd;
    }
    public void setWindowEnd(int windowEnd) {
    	this.windowEnd = windowEnd;
    }
    
    public List<Variant> getVariants() {
    	return variants;
    }
    
    public void setVariants(List<Variant> variants) {
    	this.variants = variants;
    }
    
    public String getChromosome() {
    	return chromosome;
    }
    
    public void setChromosome(String chromosome) {
    	this.chromosome = chromosome;
    }
    
    private JSONObject getFhirChromJsonObj(){
    	
    	LinkedHashMap<Object, Object> chromosome = new LinkedHashMap<Object, Object>();
    	try{
    		
    		JSONArray coding = new JSONArray();
    		coding.put(new JSONObject()
    				.put("system", "http://hl7.org/fhir/chromosome-human")
    				.put("code", this.getChromosome())
    				.put("display", "chromosome "+this.getChromosome()));
    		chromosome.put("coding", coding);
    		
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return new JSONObject(chromosome);
    }
    
    public List<Quality> getQualities() {
    	return qualities;
    }
    
    public void setQualities(List<Quality> qualities) {
    	this.qualities = qualities;
    }
    
    public String getGenomeBuild() {
    	return genomeBuild;
    }
    
    public void setGenomeBuild(String genomeBuild) {
    	this.genomeBuild = genomeBuild;
    }
    
    public JSONObject getFhirSeqJsonObj(){
    	LinkedHashMap<Object, Object> sequence = new LinkedHashMap<Object, Object>();
    	try{
    			
    		JSONArray identifier = new JSONArray();		
			identifier.put(new JSONObject().put("value", this.getUUID()));
		
		
			//patient may need to add
			
			LinkedHashMap<Object, Object> referenceSeq = new LinkedHashMap<Object, Object>();
			referenceSeq.put("chromosome", this.getFhirChromJsonObj() );   		
			referenceSeq.put("genomeBuild", this.getGenomeBuild());
			referenceSeq.put("strand", 1);
			referenceSeq.put("windowStart", Integer.valueOf(this.getWindowStart()));
			referenceSeq.put("windowEnd", Integer.valueOf(this.getWindowEnd()));
			
			
			JSONArray variant = new JSONArray(); 
			for(Variant v : this.getVariants()){
				variant.put(v.getFhirVarJsonObj());
			}
			
			JSONArray quality = new JSONArray();
			for(Quality q : this.getQualities()){
				quality.put(q.getFhirQualObj());
			}
			sequence.put("resourceType", "Sequence");
					
			sequence.put("identifier", identifier);	
			sequence.put("type", "dna");
			sequence.put("coordinateSystem", Integer.valueOf(1));
			sequence.put("referenceSeq", new JSONObject(referenceSeq));
			sequence.put("variant", variant);
			sequence.put("quality", quality);
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return new JSONObject(sequence);
    }
}
