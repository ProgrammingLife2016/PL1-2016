package io.github.programminglife2016.pl1_2016.server.parser;

import io.github.programminglife2016.pl1_2016.parser.Segment;
import io.github.programminglife2016.pl1_2016.parser.SegmentMap;
import io.github.programminglife2016.pl1_2016.parser.SimpleParser;
import org.junit.After;
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

    @Test
    public void testSegmentLine() {
        parser.parse(stringToInputStream("S	1	TTGA	*	ORI:Z:MT_H37RV_BRD_V5.ref.fasta;"));
        SegmentMap segmentMap = parser.getSegmentMap();
        Segment segment = segmentMap.get(1);
        assertEquals(1, segment.getId());
        assertEquals("TTGA", segment.getData());
    }

    @Test
    public void testLinkLine() {
        final String gfaFile = "H	VN:Z:1.0\n" +
                               "S 1 AGAT\n" +
                               "S 2 TTGC\n" +
                               "S 3 ATGC\n" +
                               "L	1	+	2	+	0M";
        parser.parse(stringToInputStream(gfaFile));
        SegmentMap segmentMap = parser.getSegmentMap();
        Segment segment1 = segmentMap.get(1);
        Segment segment2 = segmentMap.get(2);
        Segment segment3 = segmentMap.get(3);
        assertEquals(2, segment1.getLinks().get(0).getId());
        assertEquals(1, segment2.getLinks().get(0).getId());
        assertTrue(segment3.getLinks().isEmpty());
    }

    public InputStream stringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }
}
