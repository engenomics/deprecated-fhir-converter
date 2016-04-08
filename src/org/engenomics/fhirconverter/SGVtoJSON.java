package org.engenomics.fhirconverter;

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
    List<String> seqGraph = new ArrayList<>(Files.readAllLines(new File("variations.sgo").toPath(), Charset.defaultCharset()));

    // The JSON object itself
    private List<JSONObject> objs = new ArrayList<>();

    private int chromosomeNumber = 22; //TODO: Read in automatically

    private String referenceGenome;
    private String referenceGenomeBuild;

    private int atLine = 0;

    public SGVtoJSON(String vcfFilePath) throws IOException {
        // Process VCF file to get reference genome and build
        try (Stream<String> lines = Files.lines(Paths.get(vcfFilePath), Charset.defaultCharset())) {
            lines.forEachOrdered(this::process);
        }

        // Remove header of sequence graph file
        seqGraph.remove(0);
    }

    private void process(String line) {
        //Reference genome information
        if (line.contains("##reference")) {
            String referenceGenomeAndBuild = Utils.getBetween(line, "=", ".");
            this.referenceGenome = referenceGenomeAndBuild.replaceAll("\\P{L}+", "");
            this.referenceGenomeBuild = referenceGenomeAndBuild.replaceAll("[^\\d.]", "");
        }
    }

    protected void convert(String inputFilePath) throws IOException {
        seqGraph.forEach(this::make);
    }

    public List<JSONObject> getObjs() {
        return this.objs;
    }

    private void make(String line) {
        //Add a new JSON object to the list
        objs.add(new JSONObject());

        objs.get(atLine).put("resourceType", "Sequence");
        objs.get(atLine).put("id", "t" + atLine);
        objs.get(atLine).put("type", "DNA");

        JSONObject species = new JSONObject();
        species.put("text", "Homo sapiens");
        objs.get(atLine).put("species", species);


        JSONObject referenceSeq = new JSONObject();
        referenceSeq.put("chromosome", chromosomeNumber);
        referenceSeq.put("genomebuild", this.referenceGenomeBuild);
        referenceSeq.put("referenceSeqId", this.referenceGenome);

        String[] goodSeqGraphSections = getSectionsOfLine();
        String windowStart = goodSeqGraphSections[0];
        String windowEnd = goodSeqGraphSections[1];
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


        atLine++;
    }

    /**
     * Parses a variation and returns its start, end, was, and is
     *
     * @param variationString - the variation part of the line
     * @return a Sewi with the start, end, was, and is parsed from the variation string
     */
    private Sewi getSewi(String variationString) {
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
