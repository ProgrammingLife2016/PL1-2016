package io.github.programminglife2016.pl1_2016.parser.metadata;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class GFFParserTest {
    private GFFParser parser;
    private Annotation ann;

    @Before
    public void setUp() throws Exception {
        String s = "MT_H37RV_BRD_V5\tnull\tdifference\t2627279\t2632941\t0.0\t+\t.\tcalhounClass=Gene;" +
                "Name=DS9 5662 bp (4 orfs 2 IGs)  (electronically transferred);" +
                "ID=7000008662857514;displayName=DS9 5662 bp (4 orfs 2 IGs)  (electronically transferred)";
        InputStream is = convertStringToInputStream(s);
        parser = new GFFParser(is);
        parser.read();
        ann = parser.getAnnotations().get(0);
    }

    private static InputStream convertStringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testParseFirstGFFLineCorrectly() throws Exception {
    }

    @Test
    public void testSeqid() throws Exception {
        assertEquals("MT_H37RV_BRD_V5", ann.getSeqId());
    }

    @Test
    public void testSource() throws Exception {
        assertEquals("null", ann.getSource());
    }

    @Test
    public void testType() throws Exception {
        assertEquals("difference", ann.getType());
    }

    @Test
    public void testStart() throws Exception {
        assertEquals(2627279, ann.getStart());
    }

    @Test
    public void testEnd() throws Exception {
        assertEquals(2632941, ann.getEnd());
    }

    @Test
    public void testScore() throws Exception {
        assertEquals(0.0, ann.getScore(), 1E-5);
    }

    @Test
    public void testStrand() throws Exception {
        assertEquals("+", ann.getStrand());
    }

    @Test
    public void testPhase() throws Exception {
        assertEquals(".", ann.getPhase());
    }

    @Test
    public void testCalhounClass() throws Exception {
        assertEquals("Gene", ann.getCalhounClass());
    }

    @Test
    public void testName() throws Exception {
        assertEquals("DS9 5662 bp (4 orfs 2 IGs)  (electronically transferred)", ann.getName());
    }

    @Test
    public void testId() throws Exception {
        assertEquals("7000008662857514", ann.getId());
    }

    @Test
    public void testDisplayName() throws Exception {
        assertEquals("DS9 5662 bp (4 orfs 2 IGs)  (electronically transferred)", ann.getDisplayName());
    }

    @Test
    public void testGetAnnotations() throws Exception {
        assertEquals(1, parser.getAnnotations().size());
    }
}