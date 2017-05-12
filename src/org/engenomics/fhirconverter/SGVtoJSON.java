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

    public SGVtoJSON(String vcfFilePath) throws IOException {
        // Process VCF file to get reference genome and build
        try (Stream<String> lines = Files.lines(Paths.get(vcfFilePath), Charset.defaultCharset())) {
        	//System.out.println(lines);
            //lines.forEachOrdered(this::process);
        }

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
    		//System.out.println(sequence.getFhirChromJsonObj());
    		objs.add(sequence.getFhirSeqJsonObj());
    		atLine++;
    		
    		/*objs.add(new JSONObject());

            objs.get(atLine).put("resourceType", "Sequence");
            objs.get(atLine).put("id", "t" + atLine);
            objs.get(atLine).put("type", "DNA");

            JSONObject species = new JSONObject();
            species.put("text", "Homo sapiens");
            objs.get(atLine).put("species", species);

            //TODO:
            JSONObject referenceSeq = new JSONObject();
            referenceSeq.put("chromosome", chromosomeNumber);
            referenceSeq.put("genomebuild", this.referenceGenomeBuild);
            referenceSeq.put("referenceSeqId", this.referenceGenome);

            String[] goodSeqGraphSections = getSectionsOfLine();
            String windowStart = goodSeqGraphSections[0];
            String windowEnd = goodSeqGraphSections[1];
            
            //TODO:
            String variationString = goodSeqGraphSections[2];

            referenceSeq.put("windowStart", windowStart);
            referenceSeq.put("windowEnd", windowEnd);

            objs.get(atLine).put("referenceSeq", referenceSeq);


            JSONObject variation = new JSONObject();
            Sewi variationSewi = getSewi(variationString);
            variation.put("start", variationSewi.getStart());
            variation.put("end", variationSewi.getEnd());
            variation.put("observedAllele", variationSewi.getObservedAllele());
            variation.put("referenceAllele", variationSewi.getReferenceAllele());

            objs.get(atLine).put("variation", variation);


            atLine++;*/
    	}
    	catch(JSONException e){
    		e.printStackTrace();
    	}
        
    }

    /**
     * Parses a variation and returns its start, end, was, and is
     *
     * @param variationString - the variation part of the line
     * @return a Sewi with the start, end, was, and is parsed from the variation string
     */
    /*private Sewi getSewi(String variationString) {
        String[] varSections = variationString.split(":");

        int chromosome = Integer.parseInt(varSections[0]);

        String locationAndReplacement = varSections[1];

        String replacement = locationAndReplacement.replaceAll("[0-9]", "");

        String[] replacementSections = replacement.split(">");

        String was = "", is = "";

        if (replacementSections.length == 2) { // All good
            was = replacementSections[0];
            is = replacementSections[1];
        } else if (replacementSections.length == 1) { // One is empty
            if (replacement.indexOf(">") == 0) { // Original is empty (plain insertion)
                was = "";
                is = replacementSections[0];
            } else if (replacement.indexOf(">") == 1) { // Variation is empty (plain deletion)
                was = replacementSections[0];
                is = "";
            } else {
                // Should never be here
                System.err.println("Something about the contents of the variation string broke! replacement=" + replacement + ", replacementSections=" + Arrays.toString(replacementSections) + ", was=" + was + ", is=" + is + " (Line " + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")");
            }
        } else {
            // Should never be here
            System.err.println("Something about the contents of the variation string broke! replacement=" + replacement + ", replacementSections=" + Arrays.toString(replacementSections) + ", was=" + was + ", is=" + is + " (Line " + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")");
        }

        String location = locationAndReplacement.replaceAll("[^\\d.]", "");

        int start = Integer.parseInt(location);
        int end = start + (is.length() - was.length()) + 1;

        return new Sewi(start, end, was, is);
    }*/

    // Returns an array of window start, window end, and the variation, based on the current line
    private String[] getSectionsOfLine() {
        String[] seqGraphSections = seqGraph.get(atLine).split("\t");
        List<String> seqGraphSectionsNoSpaces = new ArrayList<>(Arrays.asList(seqGraphSections));
        seqGraphSectionsNoSpaces.removeAll(Collections.singleton(""));
        System.out.println(seqGraphSectionsNoSpaces);
        return seqGraphSectionsNoSpaces.toArray(new String[seqGraphSectionsNoSpaces.size()]);
    }
}
