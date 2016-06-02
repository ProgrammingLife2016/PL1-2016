package io.github.programminglife2016.pl1_2016.parser.metadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser class for the metadata.
 */
public class SpecimenParser {
    /**
     * Parse method for Specimen.
     * @param inputStream input data
     * @return JsonSerializable version of the specimenmap.
     */
    public Map<String, Subject> parse(InputStream inputStream) {
        return read(inputStream);
    }

    /**
     * Parse data from inputStream.
     * @param inputStream stream of data.
     */
    private Map<String, Subject> read(InputStream inputStream) {
        Map<String, Subject> specimens = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                parseLine(specimens, line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return specimens;
    }

    /**
     * Line parser that parses individual lines.
     * @param line line to be parsed.
     */
    private void parseLine(Map<String, Subject> specimens, String line) {
        String[] string = line.split(",");
        if (string[0].equals("Specimen ID")) {
            return;
        }
        Subject specimen = new Specimen();
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
    private void parseBasicInfo(String[] string, Subject specimen) {
        specimen.setNameId(string[0]);
        if (string[1].equals("unknown")) {
            specimen.setAge(0);
        }
        else {
            specimen.setAge(Integer.parseInt(string[1]));
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
    @SuppressWarnings("checkstyle:magicnumber")
    private void parseSecondaryInfo(String[] string, Subject specimen) {
        switch (string[3]) {
            case "Positive" : specimen.setHivStatus(1);
                break;
            case "Negative" : specimen.setHivStatus(-1);
                break;
            case "unknown" : specimen.setHivStatus(0);
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
    @SuppressWarnings("checkstyle:magicnumber")
    private void parseFirstSpecs(String[] string, Subject specimen) {
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
            specimen.setSingleColony(true);
        }
        else {
            specimen.setSingleColony(false);
        }
    }

    /**
     * Parse method to parse the rest of the data. This mostly indicate the amount of which the
     * substance was in de body.
     *
     * @param string Array containing data belonging to the specimen.
     * @param specimen The specimen for whom the data belongs to.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private void parseSecondSpecs(String[] string, Subject specimen) {
        specimen.setPdstpattern(string[10]);
        specimen.setCapreomycin(string[11]);
        specimen.setEthambutol(string[12]);
        specimen.setEthionamide(string[13]);
        specimen.setIsoniazid(string[14]);
        specimen.setKanamycin(string[15]);
        specimen.setPyrazinamide(string[16]);
        specimen.setOfloxacin(string[17]);
        specimen.setRifampin(string[18]);
        specimen.setStreptomycin(string[19]);
        specimen.setSpoligotype(string[20]);
        specimen.setLineage(string[21]);
        specimen.setGdstPattern(string[22]);
        specimen.setXdr(string[23]);
    }
}
