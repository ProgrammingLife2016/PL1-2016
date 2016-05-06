package io.github.programminglife2016.pl1_2016.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * Parser class for the metadata.
 * Created by ravishivam on 4-5-16.
 */
public class SpecimenParser implements Parser {
    /**
     * Map containing all the specimen.
     */
    private SpecimenMap specimens;

    /**
     * Constructor for the parser.
     */
    public SpecimenParser() {
        specimens = new SpecimenMap();
    }

    /**
     * Parse method for Specimen.
     * @param inputStream input data
     * @return JsonSerializable version of the specimenmap.
     */
    @Override
    public JsonSerializable parse(InputStream inputStream) {
        read(inputStream);
        return specimens;
    }

    /**
     * Parse data from inputStream.
     * @param inputStream stream of data.
     */
    private void read(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Line parser that parses individual lines.
     * @param line line to be parsed.
     */
    private void parseLine(String line) {
        String[] string = line.split(",");
        if (string[0].equals("Specimen ID")) {
            return;
        }
        Specimen specimen = new Specimen();
        parseBasicInfo(string, specimen);
        parseSecondaryInfo(string, specimen);
        parseFirstSpecs(string, specimen);
        parseSecondSpecs(string, specimen);
        specimens.put(specimen.getNameId(), specimen);
    }

    /**
     * Parse info of specimen of name, age and sex.
     * @param string Array containing data belonging to the specimen.
     * @param specimen The specimen for whom the data belongs to.
     */
    private void parseBasicInfo(String[] string, Specimen specimen) {
        specimen.setNameId(string[0]);
        if(string[1].equals("unknown")) {
            specimen.setAge(0);
        }
        else {
            specimen.setAge((int) Double.parseDouble(string[1]));
        }
        if (string[2].equals("Male")) {
            specimen.setMale(true);
        }
        else {
            specimen.setMale(false);
        }
    }

    /**
     * Parse data for hiv, cohort, data, district and type.
     * @param string Array containing data belonging to the specimen.
     * @param specimen The specimen for whom the data belongs to.
     */
    private void parseSecondaryInfo(String[] string, Specimen specimen) {
        switch (string[3]) {
            case "Positive" : specimen.setHIV(1);
                break;
            case "Negative" : specimen.setHIV(-1);
                break;
            case "unknown" : specimen.setHIV(0);
                break;
            default : break;
        }
        specimen.setCohort(string[4]);
        specimen.setDate(string[5]);
        specimen.setDistrict(string[6]);
        specimen.setType(string[7]);
    }

    /**
     * Parse specs for smear and number of colonies.
     * @param string Array containing data belonging to the specimen.
     * @param specimen The specimen for whom the data belongs to.
     */
    private void parseFirstSpecs(String[] string, Specimen specimen) {
        switch (string[8]) {
            case "Positive" : specimen.setSmear(1);
                break;
            case "Negative" : specimen.setSmear(-1);
                break;
            case "unknown" : specimen.setSmear(0);
                break;
            default : break;
        }
        if (string[9].equals("single colony")) {
            specimen.setSinglecolony(true);
        }
        else {
            specimen.setSinglecolony(false);
        }
    }

    /**
     * Parse method to parse the rest of the data. This mostly indicate de amount of which the substance was in de body.*
     * @param string Array containing data belonging to the specimen.
     * @param specimen The specimen for whom the data belongs to.
     */
    private void parseSecondSpecs(String[] string, Specimen specimen) {
        specimen.setpDSTPattern(string[10]);
        specimen.setCapreomycin(string[11].charAt(0));
        specimen.setEthamButol(string[12].charAt(0));
        specimen.setEthionamide(string[13].charAt(0));
        specimen.setIsoniazid(string[14].charAt(0));
        specimen.setKanamycin(string[15].charAt(0));
        specimen.setPyrazinamide(string[16].charAt(0));
        specimen.setOfloxacin(string[17].charAt(0));
        specimen.setRifampin(string[18].charAt(0));
        specimen.setStreptomycin(string[19].charAt(0));
        specimen.setSpoligotype(string[20].charAt(0));
        specimen.setLineage(string[21].charAt(0));
        specimen.setgDSTPattern(string[22]);
        specimen.setXdr(string[23]);
    }
}