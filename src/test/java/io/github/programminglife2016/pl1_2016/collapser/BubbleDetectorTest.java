//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the BubbleDetector class.
 */
public class BubbleDetectorTest {
    private InputStream idorder;
    private InputStream startorder;
    private InputStream endorder;
    private BubbleDetector detector;

    public InputStream INPUT;
    public InputStream META;
    public InputStream POS;


    /**
     * Create parser object.
     *
     * @throws IOException thrown if the test data INPUT faulty
     */
    @Before
     public void setUp() throws IOException {
        META = BubbleDetectorTest.class.getClass().getResourceAsStream("/genomes/metadata.csv");
        INPUT = BubbleDetectorTest.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        POS = BubbleDetectorTest.class.getClass().getResourceAsStream("/genomes/TB10.positions");
        idorder = BubbleDetectorTest.class.getClass().getResourceAsStream("/features/tb10.id");
        startorder = BubbleDetectorTest.class.getClass().getResourceAsStream("/features/tb10.start");
        endorder = BubbleDetectorTest.class.getClass().getResourceAsStream("/features/tb10.end");

        NodeCollection nodeCollection = new SegmentParser(POS, META).parse(INPUT);
        detector = new BubbleDetector(nodeCollection);
        detector.findMultiLevelBubbles();
    }

    /**
     * Test correct size for detected bubbles.
     */
    @Test
    public void testBubbleFirstLevel() {
        assertEquals(detector.getBubbleBoundaries().size(), 2920);
    }

    /**
     * Verify all nodes id order and numbering.
     */
    @Test
    public void testBubbleIdOrder() throws IOException {
        List<Integer> ids = getListForTesting(idorder);
        List<Integer> bounds = detector.getBubbleBoundaries().stream().map(Node::getId).collect(Collectors.toList());
        assertEquals(ids, bounds);
    }

    @Test
    public void testStartNodeOrder() throws IOException {
        List<Integer> ids = getListForTesting(startorder);
        List<Integer> bounds = detector.getBubbleBoundaries().stream().map(x -> x.getStartNode().getId()).collect(Collectors.toList());
        assertEquals(ids, bounds);
    }

    @Test
    public void testEndNodeOrder() throws IOException {
        List<Integer> ids = getListForTesting(endorder);
        List<Integer> bounds = detector.getBubbleBoundaries().stream().map(x -> x.getEndNode().getId()).collect(Collectors.toList());
        assertEquals(ids, bounds);
    }

    private List<Integer> getListForTesting(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<Integer> list = new ArrayList<>();
        String line = reader.readLine();
        while(line != null) {
            list.add(Integer.parseInt(line));
            line = reader.readLine();
        }
        reader.close();
        return list;
    }
}
