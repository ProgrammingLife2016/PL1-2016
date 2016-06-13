package io.github.programminglife2016.pl1_2016.parser.metadata;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
//CHECKSTYLE.OFF: MagicNumber

/**
 * Tests for SpecimenParser.
 */
public class SpecimenParserTest {

    /**
     * Parser to be used in each test.
     */
    private SpecimenParser specimenParser;

    /**
     * Test string.
     */
    String file = "Specimen ID,Age,Sex,hivStatus Status,Cohort,Date of Coltion,Study"
            + " Geographic District,Specimen Type,Microscopy Smear Status,DNA isolation: "
            + "single colony or non-single colony,Phenotypic DST Pattern,Capmycin (10ug/mL),"
            + "Ethambutol (7.5ug/mL),Ethide (10ug/mL),Isiazid (0.2ug/mL or 1ug/mL),cin "
            + "(6ug/mL),Pyrazinamide (Nicotinamide 500.0ug/mL or PZA-MGIT),Ofloxacin (2ug/mL),"
            + "Rifampin (1ug/mL),Streptomycin (2ug/mL),Digital Spoligotype,Lineage,Genotypic DST "
            + "pattern,Tugela Ferry vs. non-Tugela Ferry XDR\n"
            + "TKK-01-0001,unknown,Female,Positive,KZNSUR,1/16/2008,"
            + "eThekwini,sputum,Negative,single "
            + "colony,MDR,S,R,R,R,S,R,S,R,R,LAM4,LIN 4,MDR,n/a\n"
            + "TKK-01-0006,70,Male,Negative,KZNSUR,2/15/2008,eThekwini,sputum,Positive,single "
            + "colony,MDR,S,S,S,R,S,S,S,R,S,LAM4,LIN 4,MDR,n/a\n"
            + "TKK-01-0009,27,Female,unknown,KZNSUR,2/19/2008,eThekwini,sputum,Positive,single "
            + "colony,mono,S,S,S,S,S,S,S,R,S,T1,LIN 4,MDR,n/a\n"
            + "TKK-01-0011,32,Male,unknown,KZNSUR,2/20/2008,eThekwini,sputum,Positive,single colony"
            + ",MDR,S,S,S,R,S,S,S,R,S,LAM11-ZWE,LAM4,LIN 4,MDR,n/a\n"
            + "TKK-01-0012,23,Female,Negative,KZNSUR,2/25/2008,eThekwini,sputum,unknown,"
            + "single,MDR,S,R,S,R,S,R,S,R,R,LAM4,LIN 4,MDR,n/a\n"
            + "Specimen ID,35,Male,Positive,KZNSUR,2/26/2008,uThungulu,"
            + "sputum,Positive,single colony"
            + ",MDR,S,R,S,R,S,R,S,R,R,T1,LIN 4,MDR,n/a";

    /**
     * Setup the parser before each test.
     */
    @Before
    public void setup() {
        specimenParser = new SpecimenParser();
    }

    /**
     * Test the methods of the parser of specimen.
     *
     * @throws IOException thrown if test string contains wrong encoding.
     */
    @Test
    public void testSpecimenParsedCorrect() throws IOException {
        InputStream stream = IOUtils.toInputStream(file, "UTF-8");
        Map<String, Subject> specimenCollection = specimenParser.parse(stream);
        assertEquals(70, specimenCollection.get("TKK-01-0006").getAge());
    }

    /**
     * Test all getters for specimen.
     *
     * @throws IOException thrown if test string contains wrong encoding.
     */
    @Test
    public void testSpecimen() throws IOException {
        InputStream stream = IOUtils.toInputStream(file, "UTF-8");
        Map<String, Subject> specimenCollection = specimenParser.parse(stream);
        assertEquals("TKK-01-0006", specimenCollection.get("TKK-01-0006").getNameId());
        assertEquals(70, specimenCollection.get("TKK-01-0006").getAge());
        assertEquals(true, specimenCollection.get("TKK-01-0006").isMale());
        assertEquals(-1, specimenCollection.get("TKK-01-0006").getHivStatus());
        assertEquals("KZNSUR", specimenCollection.get("TKK-01-0006").getCohort());
        assertEquals("2/15/2008", specimenCollection.get("TKK-01-0006").getDate());
        assertEquals("eThekwini", specimenCollection.get("TKK-01-0006").getDistrict());
        assertEquals("sputum", specimenCollection.get("TKK-01-0006").getType());
        assertEquals(1, specimenCollection.get("TKK-01-0006").getSmear());
        assertEquals(true, specimenCollection.get("TKK-01-0006").isSingleColony());
        assertEquals("MDR", specimenCollection.get("TKK-01-0006").getPdstpattern());
        assertEquals("S", specimenCollection.get("TKK-01-0006").getCapreomycin());
        assertEquals("S", specimenCollection.get("TKK-01-0006").getEthambutol());
        assertEquals("S", specimenCollection.get("TKK-01-0006").getEthionamide());
        assertEquals("R", specimenCollection.get("TKK-01-0006").getIsoniazid());
        assertEquals("S", specimenCollection.get("TKK-01-0006").getKanamycin());
        assertEquals("S", specimenCollection.get("TKK-01-0006").getPyrazinamide());
        assertEquals("S", specimenCollection.get("TKK-01-0006").getOfloxacin());
        assertEquals("R", specimenCollection.get("TKK-01-0006").getRifampin());
        assertEquals("S", specimenCollection.get("TKK-01-0006").getStreptomycin());
        assertEquals("LAM4", specimenCollection.get("TKK-01-0006").getSpoligotype());
        assertEquals("LIN 4", specimenCollection.get("TKK-01-0006").getLineage());
        assertEquals("MDR", specimenCollection.get("TKK-01-0006").getGdstPattern());
        assertEquals("n/a", specimenCollection.get("TKK-01-0006").getXdr());
    }
}
