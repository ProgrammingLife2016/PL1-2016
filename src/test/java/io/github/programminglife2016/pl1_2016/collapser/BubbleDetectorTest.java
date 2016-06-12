//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for the BubbleDetector class.
 */
public class BubbleDetectorTest {
    private InputStream idorder;
    private InputStream startorder;
    private InputStream endorder;
    private BubbleDetector detector;

    private InputStream input;
    private InputStream meta;
    private InputStream pos;


    /**
     * Create parser object.
     *
     * @throws IOException thrown if the test data input faulty
     */
    @Before
     public void setUp() throws IOException {
        meta = BubbleDetectorTest.class.getClass().getResourceAsStream("/genomes/metadata.csv");
        input = BubbleDetectorTest.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        pos = BubbleDetectorTest.class.getClass().getResourceAsStream("/genomes/TB10.positions");
        idorder = BubbleDetectorTest.class.getClass().getResourceAsStream("/features/tb10.id");
        startorder =
                BubbleDetectorTest.class.getClass().getResourceAsStream("/features/tb10.start");
        endorder = BubbleDetectorTest.class.getClass().getResourceAsStream("/features/tb10.end");

        NodeCollection nodeCollection = new SegmentParser(pos, meta).parse(input);
        detector = new BubbleDetector(nodeCollection);
        detector.findMultiLevelBubbles();
    }
}
