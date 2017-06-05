# fhir-converter
Converts from an SGO and a VCF file to the FHIR Genomics standard (json).

##current version(1.3)
Convert Variant Call Format(.vcf) file to output.txt(simluar to csv format),
Then tranlate each csv line into Sequence FHIR json files.

The associated Patient FHIR josn file need to manually type inside the code,
will provide a convenient input way in next version.

It is able to do Http Post for your json file to your fhir server. 

The code at the moment is not very clean, but it works as expected.
