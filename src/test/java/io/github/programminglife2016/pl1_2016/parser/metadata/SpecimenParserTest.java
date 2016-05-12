package io.github.programminglife2016.pl1_2016.parser.metadata;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.nio.charset.StandardCharsets;

//CHECKSTYLE.OFF: MagicNumber

/** Tests for SpecimenParser.
 * Created by ravishivam on 12-5-16.
 */
public class SpecimenParserTest {

    /**
     * Parser to be used in each test.
     */
    SpecimenParser specimenParser;

    /**
     * Test string.
     */
    String file = "Specimen ID,Age,Sex,hivStatus Status,Cohort,Date of Collection,Study" +
            " Geographic District,Specimen Type,Microscopy Smear Status,DNA isolation: " +
            "single colony or non-single colony,Phenotypic DST Pattern,Capreomycin (10ug/mL)," +
            "Ethambutol (7.5ug/mL),Ethionamide (10ug/mL),Isoniazid (0.2ug/mL or 1ug/mL),Kanamycin " +
            "(6ug/mL),Pyrazinamide (Nicotinamide 500.0ug/mL or PZA-MGIT),Ofloxacin (2ug/mL)," +
            "Rifampin (1ug/mL),Streptomycin (2ug/mL),Digital Spoligotype,Lineage,Genotypic DST " +
            "pattern,Tugela Ferry vs. non-Tugela Ferry XDR\n" +
            "TKK-01-0001,unknown,Female,Positive,KZNSUR,1/16/2008,eThekwini,sputum,Negative,single " +
            "colony,MDR,S,R,R,R,S,R,S,R,R,LAM4,LIN 4,MDR,n/a\n" +
            "TKK-01-0006,70,Male,Negative,KZNSUR,2/15/2008,eThekwini,sputum,Positive,single " +
            "colony,MDR,S,S,S,R,S,S,S,R,S,LAM4,LIN 4,MDR,n/a\n" +
            "TKK-01-0009,27,Female,unknown,KZNSUR,2/19/2008,eThekwini,sputum,Positive,single " +
            "colony,mono,S,S,S,S,S,S,S,R,S,T1,LIN 4,MDR,n/a\n" +
            "TKK-01-0011,32,Male,unknown,KZNSUR,2/20/2008,eThekwini,sputum,Positive,single colony" +
            ",MDR,S,S,S,R,S,S,S,R,S,LAM11-ZWE,LAM4,LIN 4,MDR,n/a\n" +
            "TKK-01-0012,23,Female,Negative,KZNSUR,2/25/2008,eThekwini,sputum,unknown," +
            "single,MDR,S,R,S,R,S,R,S,R,R,LAM4,LIN 4,MDR,n/a\n" +
            "Specimen ID,35,Male,Positive,KZNSUR,2/26/2008,uThungulu,sputum,Positive,single colony" +
            ",MDR,S,R,S,R,S,R,S,R,R,T1,LIN 4,MDR,n/a";

    /**
     * Setup the parser before each test.
     */
    @Before
    public void setup() {
        specimenParser = new SpecimenParser();
    }

    @Test
    public void testSpecimenParsedCorrect() {
        InputStream stream = stringToInputStream(file);
        specimenParser.parse(stream);
        SpecimenCollection specimenCollection = specimenParser.getSpecimenCollection();
        assertEquals(70, specimenCollection.get("TKK-01-0006").getAge());
    }

    @Test (expected = NullPointerException.class)
    public void testEmptyStringInput() throws FileNotFoundException {
        InputStream in = SpecimenParserTest.class.getResourceAsStream("/webapp/statictestfil");
        specimenParser.parse(in);
    }
    /**
     * Converts a String to an InputStream
     * @param s String
     * @return InputStream of that String.
     */
    public InputStream stringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }
}
