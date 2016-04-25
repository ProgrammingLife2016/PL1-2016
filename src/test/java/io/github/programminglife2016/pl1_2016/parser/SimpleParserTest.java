package io.github.programminglife2016.pl1_2016.server.parser;

import io.github.programminglife2016.pl1_2016.parser.Segment;
import io.github.programminglife2016.pl1_2016.parser.SegmentMap;
import io.github.programminglife2016.pl1_2016.parser.SimpleParser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//CHECKSTYLE.OFF: MagicNumber

/**
 * Tests for the BasicServer class
 */
public class SimpleParserTest {
    private SimpleParser parser;

    /**
     * Create parser object.
     */
    @Before
    public void setUp() {
        parser = new SimpleParser();
    }

    /**
     * Test parsing a segment line to produce the correct segment.
     */
    @Test
    public void testSegmentLine() {
        parser.parse(stringToInputStream("S	1	TTGA	*	ORI:Z:MT_H37RV_BRD_V5.ref.fasta;"));
        SegmentMap segmentMap = parser.getSegmentMap();
        Segment segment = segmentMap.get(1);
        assertEquals(1, segment.getId());
        assertEquals("TTGA", segment.getData());
    }

    /**
     * Test parsing a link line to produce the correct links.
     */
    @Test
    public void testLinkLine() {
        final String gfaFile = "H	VN:Z:1.0\n"
                               + "S 1 AGAT\n"
                               + "S 2 TTGC\n"
                               + "S 3 ATGC\n"
                               + "L	1	+	2	+	0M";
        parser.parse(stringToInputStream(gfaFile));
        SegmentMap segmentMap = parser.getSegmentMap();
        Segment segment1 = segmentMap.get(1);
        Segment segment3 = segmentMap.get(3);
        assertEquals(2, segment1.getLinks().get(0).getId());
        assertTrue(segment3.getLinks().isEmpty());
    }

    /**
     * Test if the z-index of a segment is parsed correctly.
     */
    @Test
    public void testParseZIndex() {
        final String gfaFile = "H	VN:Z:1.0\n"
                + "S 1 AGAT\n"
                + "S 2 TTGC\n"
                + "S	3	C	*	ORI:Z:TKK_02_0008.fasta	CRD:Z:TKK_02_0008.fasta	CRDCTG:Z:NZ_KK"
                + "327777.1	CTG:Z:NZ_KK327777.1	START:Z:1451\n"
                + "L	1	+	2	+	0M";
        parser.parse(stringToInputStream(gfaFile));
        SegmentMap segmentMap = parser.getSegmentMap();
        Segment segment3 = segmentMap.get(3);
        assertEquals(1451, segment3.getColumn());
    }
    /**
     * Test if position of multiple nodes in column are calculated correctly.
     */
    @Test
    public void testCalculateMultipleNode() {
        final String gfaFile = "H	VN:Z:1.0\n"
                + "S    1   T   *   CTG:Z:NZ_KK327777.1;NZ_KK327775.1;MT_H37RV_BRD_V5;NZ_KK350895.1 START:Z:0 \n"
                + "S    2   T   *   CTG:Z:NZ_KK327777.1;NZ_KK327775.1;MT_H37RV_BRD_V5;NZ_KK350895.1 START:Z:0 \n"
                + "S	3	C	*	ORI:Z:TKK_02_0008.fasta	CRD:Z:TKK_02_0008.fasta	CRDCTG:Z:NZ_KK \n"
                + "327777.1	CTG:Z:NZ_KK327777.1	START:Z:1451\n"
                + "L	1	+	3	+	0M \n"
                + "L	2	+	3	+	0M";
        parser.parse(stringToInputStream(gfaFile));
        SegmentMap segmentMap = parser.getSegmentMap();
        assertEquals(5, segmentMap.get(1).getY());
    }

    /**
     * Test if position of multiple nodes is calculated correctly on the other axis.
     */
    @Test
    public void testCalculateMultipleNodeOnOtherAxis() {
        final String gfaFile = "H	VN:Z:1.0\n"
                + "S    1   T   *   CTG:Z:NZ_KK327777.1;NZ_KK327775.1;"
                + "MT_H37RV_BRD_V5;NZ_KK350895.1 START:Z:0 \n"
                + "S    2   T   *   CTG:Z:NZ_KK327777.1;NZ_KK327775.1;"
                + "MT_H37RV_BRD_V5;NZ_KK350895.1 START:Z:0 \n"
                + "S	3	C	*	ORI:Z:TKK_02_0008.fasta	"
                +"CRD:Z:TKK_02_0008.fasta	CRDCTG:Z:NZ_KK \n"
                + "327777.1	CTG:Z:NZ_KK327777.1	START:Z:1451\n"
                + "L	1	+	3	+	0M \n"
                + "L	2	+	3	+	0M";
        parser.parse(stringToInputStream(gfaFile));
        SegmentMap segmentMap = parser.getSegmentMap();
        assertEquals(-5, segmentMap.get(2).getY());
    }

    /**
     * Test if position of single node in column is calculated correctly.
     */
    @Test
    public void testOneNodeInColumn() {
        final String gfaFile = "H	VN:Z:1.0\n"
                + "S    1   T   *   CTG:Z:NZ_KK327777.1;NZ_KK327775.1;"
                + "MT_H37RV_BRD_V5;NZ_KK350895.1 START:Z:0 \n"
                + "S    2   T   *   CTG:Z:NZ_KK327777.1;NZ_KK327775.1;"
                + "MT_H37RV_BRD_V5;NZ_KK350895.1 START:Z:0 \n"
                + "S	3	C	*	ORI:Z:TKK_02_0008.fasta	"
                +"CRD:Z:TKK_02_0008.fasta	CRDCTG:Z:NZ_KK \n"
                + "327777.1	CTG:Z:NZ_KK327777.1	START:Z:1451\n"
                + "L	1	+	3	+	0M \n"
                + "L	2	+	3	+	0M";
        parser.parse(stringToInputStream(gfaFile));
        SegmentMap segmentMap = parser.getSegmentMap();
        assertEquals(0, segmentMap.get(3).getY());
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
