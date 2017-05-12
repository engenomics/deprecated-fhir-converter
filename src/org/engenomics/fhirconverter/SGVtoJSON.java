package org.engenomics.fhirconverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class SGVtoJSON {
    // The Sequence Graph Output
    List<String> seqGraph = new ArrayList<>(Files.readAllLines(new File("private/output.txt").toPath(), Charset.defaultCharset()));

    // The JSON object itself
    private List<JSONObject> objs = new ArrayList<>();

    private int atLine = 0;
    
    private List<Sewi> sequences = new ArrayList<>();

    public SGVtoJSON() throws IOException {
        // Remove header of sequence graph file
        seqGraph.remove(0);
    }

    protected void convert(String inputFilePath) throws IOException {
        seqGraph.forEach(this::make);
    }

    public List<JSONObject> getObjs() {
        return this.objs;
    }
    
    public List<Sewi> getSequences() {
    	return sequences;
    }

    private void make(String line) {
        //Add a new JSON object to the list
    	try{
    		String[] goodSeqGraphSections = getSectionsOfLine();
    		Sewi sequence = new Sewi(goodSeqGraphSections);
    		this.sequences.add(sequence);
    		objs.add(sequence.getFhirSeqJsonObj());
    		atLine++;
    	}
    	catch(JSONException e){
    		e.printStackTrace();
    	}
        
    }

    // Returns an array of window start, window end, and the variation, based on the current line
    private String[] getSectionsOfLine() {
        String[] seqGraphSections = seqGraph.get(atLine).split("\t");
        List<String> seqGraphSectionsNoSpaces = new ArrayList<>(Arrays.asList(seqGraphSections));
        seqGraphSectionsNoSpaces.removeAll(Collections.singleton(""));
        System.out.println(seqGraphSectionsNoSpaces);
        return seqGraphSectionsNoSpaces.toArray(new String[seqGraphSectionsNoSpaces.size()]);
    }
}
