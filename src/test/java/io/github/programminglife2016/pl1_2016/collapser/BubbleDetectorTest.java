//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for the BubbleDetector class.
 */
public class BubbleDetectorTest {
    private BubbleDetector detector;

    /**
     * Create parser object.
     *
     * @throws IOException thrown if the test data is faulty
     */
    @Before
    public void setUp() throws IOException {
        InputStream is = IOUtils.toInputStream(BubbleCollapserTest.DATA, "UTF-8");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        detector = new BubbleDetector(nodeCollection);
    }

    /**
     * Verify if the correct bubbles are detected.
     */
    @Test
    public void testBubbleFirstLevel() {
        detector.findMultiLevelBubbles();
        assertEquals(detector.getBubbleBoundaries().get(1).getId(), 18);
    }

    /**
     * Verify if the detected bubbles have the correct size.
     */
    @Test
    public void testBubblingInContainer() {
        detector.findMultiLevelBubbles();
        Node node = detector.getBubbleBoundaries().get(1);
        int container = node.getContainerSize();
        assertEquals(container, 0);
    }
}
