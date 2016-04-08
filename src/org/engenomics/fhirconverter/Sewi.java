package org.engenomics.fhirconverter;

public class Sewi {
    private int start;
    private int end;
    private String referenceAllele;
    private String observedAllele;

    public Sewi(int start, int end, String referenceAllele, String observedAllele) {
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
}
