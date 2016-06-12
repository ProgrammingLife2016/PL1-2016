package io.github.programminglife2016.pl1_2016.parser.metadata;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Tests for GFFParser. 
 */
public class GFFParserTest {
    private GFFParser parser;
    private Annotation ann;
    private static final int TESTNUMBER1 = 2627279;
    private static final int TESTNUMBER2 = 2632941;
    private static final double TESTNUMBER3 = 1E-5;

    /**
     * Set up test
     */
    @Before
    public void setUp() {
        String s = "MT_H37RV_BRD_V5\tnull\tdifference\t2627279\t2632941\t0.0\t+\t."
           + "\tcalhounClass=Gene;Name=DS9 5662 bp (4 orfs 2 IGs)  (electronically transferred);"
           + "ID=7000008662857514;displayName=DS9 5662 bp (4 orfs 2 IGs)  "
                   + "(electronically transferred)";
        InputStream is = convertStringToInputStream(s);
        parser = new GFFParser(is);
        parser.read();
        ann = parser.getAnnotations().get(0);
    }

    /**
     * Convert string to inputstream.
     * @param s string to convert
     * @return inputstream from string
     */
    private static InputStream convertStringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    /**
     * Test seq Id of annotation.
     */
    @Test
    public void testSeqid() {
        assertEquals("MT_H37RV_BRD_V5", ann.getSeqId());
    }

    /**
     * Test source of annotation.
     */
    @Test
    public void testSource() {
        assertEquals("null", ann.getSource());
    }

    /**
     * Test type of annotation.
     */
    @Test
    public void testType() {
        assertEquals("difference", ann.getType());
    }

    /**
     * Test start of annotation.
     */
    @Test
    public void testStart() {
        assertEquals(TESTNUMBER1, ann.getStart());
    }

    /**
     * Test end of annotation.
     */
    @Test
    public void testEnd() {
        assertEquals(TESTNUMBER2, ann.getEnd());
    }

    /**
     * Test score of annotation.
     */
    @Test
    public void testScore() {
        assertEquals(0.0, ann.getScore(), TESTNUMBER3);
    }

    /**
     * Test strand of annotation.
     */
    @Test
    public void testStrand() {
        assertEquals("+", ann.getStrand());
    }

    /**
     * Test phase of annotation.
     */
    @Test
    public void testPhase() {
        assertEquals(".", ann.getPhase());
    }

    /**
     * Test calhoun class of annotation.
     */
    @Test
    public void testCalhounClass() {
        assertEquals("Gene", ann.getCalhounClass());
    }

    /**
     * Test name of annotation.
     */
    @Test
    public void testName() {
        assertEquals("DS9 5662 bp (4 orfs 2 IGs)  (electronically transferred)", ann.getName());
    }

    /**
     * Test id of annotation.
     */
    @Test
    public void testId() {
        assertEquals("7000008662857514", ann.getId());
    }

    /**
     * Test display name of annotation.
     */
    @Test
    public void testDisplayName() {
        assertEquals("DS9 5662 bp (4 orfs 2 IGs)  "
                     + "(electronically transferred)", ann.getDisplayName());
    }

    /**
     * Test annotation of parser.
     */
    @Test
    public void testGetAnnotations() {
        assertEquals(1, parser.getAnnotations().size());
    }
}