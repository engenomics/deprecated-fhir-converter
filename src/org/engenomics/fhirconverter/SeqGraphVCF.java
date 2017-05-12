package org.engenomics.fhirconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


public class SeqGraphVCF {
	public String filename = "test.vcf";
	public String fileHeader = "CHROM\tWinStart\tWinEnd\tPOS\tAllele\tGT\tQual\tRefBuild\tUUID";
    public String home = System.getProperty("user.dir");
    public String readFilePath = home+ File.separator + "private" + File.separator + filename;
    public String outputFilePath = home+ File.separator + "private" + File.separator + "output.txt";
    
    private BufferedWriter writer;

    public void openWriter() {
        try {
            writer = new BufferedWriter(new FileWriter(outputFilePath));
        } catch (Exception e) {
            System.out.println("unsuccessful open");
        }
    }

    public void closeWriter() {

        try {
            writer.close();
        } catch (Exception e) {
            System.out.println("unsucessful close");
        }
    }

    public void writeToFile(String ln) {

        try {
            writer.write(ln);
            writer.newLine();
        } catch (Exception e) {
            System.out.println("unsuccessful write");
        }
    }

    public void createSequenceGraphVCF(int windowSize) {

        try {
            BufferedReader buf = new BufferedReader(new FileReader(readFilePath));
            List<String> words = new ArrayList<>();
            String lineFetched = "";
            String[] wordsArray = new String[100];
            String referenceBuild = ".";
            String uuid = ".";
            String startKMerPosition = ".";
            String endKMerPosition = ".";
            String chromosome = ".";
            String GT = ".";
            
            openWriter();
            writeToFile(fileHeader);
            
            while (true) {

            	String line = "";
                lineFetched = buf.readLine();
                if (lineFetched == null){
                	if (GT.equals("0/0") || GT.equals("0|0") || GT.equals("./.") || GT.equals(".|.")){
                		endKMerPosition = wordsArray[1];
            			String allele = wordsArray[3] + ">" + wordsArray[4];
            			line += chromosome + "\t" + startKMerPosition + "\t" + "\t" + endKMerPosition + "\t" 
            					+ wordsArray[1] + "\t" + allele + "\t"
                    			+ GT + "\t" + wordsArray[5] + "\t" + referenceBuild + "\t" + uuid;
                        
                        writeToFile(line);
                    }
                	break;
                }
                else {
                    wordsArray = lineFetched.split("\t");
                    for (String each : wordsArray) {
                        if (!"".equals(each)) words.add(each);
                    }
                   
                }
                                
                if (wordsArray.length < 2 || lineFetched.contains("#")) {
            		//Get reference Build
                	//System.out.println(lineFetched);
                	if (lineFetched.contains("##") && lineFetched.contains("assembly") && referenceBuild.equals(".")){
                		String assStr = lineFetched.substring(lineFetched.indexOf("assembly"));
                		int startIndex = assStr.indexOf("=") + 1;
                		int endIndex = assStr.indexOf(">");
                		
                		referenceBuild = assStr.substring(startIndex,endIndex);
                		
                	}	
                	//Get test uuid
                	if (!lineFetched.contains("##")) {uuid = wordsArray[9];}
                } else {
                	if (!chromosome.equals(String.valueOf(wordsArray[0].charAt(wordsArray[0].length() - 1)))){
                		if (GT.equals("0/0") || GT.equals("0|0") || GT.equals("./.") || GT.equals(".|.")){
                			endKMerPosition = wordsArray[1];
                			String allele = wordsArray[3] + ">" + wordsArray[4];
                			line += chromosome + "\t" + startKMerPosition + "\t" + "\t" + endKMerPosition + "\t" 
                					+ wordsArray[1] + "\t" + allele + "\t"
                        			+ GT + "\t" + wordsArray[5] + "\t" + referenceBuild + "\t" + uuid;
    	                    
    	                    
    	                    writeToFile(line);
                		}
                		startKMerPosition = wordsArray[1];
                		
                	}
                    
                	if (wordsArray[0].length() > 1){
                		chromosome = String.valueOf(wordsArray[0].charAt(wordsArray[0].length() - 1));
                	}else{
                		chromosome = wordsArray[0];
                	}
                	
                    
            		GT = wordsArray[9].split(":")[0];                		
            		if (GT.equals("0/0") || GT.equals("0|0") || GT.equals("./.") || GT.equals(".|.")){
            			
            			
            		}else{
            			endKMerPosition = wordsArray[1];
            			String allele = wordsArray[3] + ">" + wordsArray[4];
            			line += chromosome + "\t" + startKMerPosition + "\t" + "\t" + endKMerPosition + "\t" 
            					+ wordsArray[1] + "\t" + allele + "\t"
                    			+ GT + "\t" + wordsArray[5] + "\t" + referenceBuild + "\t" + uuid;
	                    
	                    writeToFile(line);
	                    
	                    //assign the WinStart value to the 
	                    startKMerPosition = String.valueOf(Integer.parseInt(wordsArray[1]) + 1);
	                    
            		}
                		
                    

                }
            }
            

            buf.close();
            closeWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
