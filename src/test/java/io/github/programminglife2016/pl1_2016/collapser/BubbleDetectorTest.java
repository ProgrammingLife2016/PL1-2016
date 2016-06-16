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

    private InputStream INPUT;
    private InputStream META;
    private InputStream POS;


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

//    /**
//     * Test correct size for detected bubbles.
//     */
//    @Test
//    public void testCorrectNumbersOfBubblesDetected() {
//        assertEquals(detector.getBubbleBoundaries().size(), 2921);
//    }
//
//    /**
//     * Verify all nodes id order and numbering.
//     */
//    @Test
//    public void testBubbleIdOrder() throws IOException {
//        Set<Integer> ids = getListForTesting(idorder);
//        Set<Integer> bounds = detector.getBubbleBoundaries().stream().map(Node::getId).collect(Collectors.toSet());
//        assertEquals(ids.size(), bounds.size());
//        for (Integer i : ids) {
//            assertTrue(bounds.contains(i));
//        }
//    }
//
//    @Test
//    public void testStartNodeOrder() throws IOException {
//        Set<Integer> ids = getListForTesting(startorder);
//        Set<Integer> bounds = detector.getBubbleBoundaries().stream().map(Node::getId).collect(Collectors.toSet());
//        assertEquals(ids.size(), bounds.size());
//            assertTrue(bounds.contains(ids.iterator().next()));
//        }
//
//    @Test
//    public void testEndNodeOrder() throws IOException {
//        Set<Integer> ids = getListForTesting(endorder);
//        Set<Integer> bounds = detector.getBubbleBoundaries().stream().map(Node::getId).collect(Collectors.toSet());
//        assertEquals(ids.size(), bounds.size());
//        for (Integer i : ids) {
//            assertTrue(bounds.contains(i));
//        }
//    }
//
//    private HashSet<Integer> getListForTesting(InputStream inputStream) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        Set<Integer> list = new HashSet<>();
//        String line = reader.readLine();
//        while(line != null) {
//            list.add(Integer.parseInt(line));
//            line = reader.readLine();
//        }
//        reader.close();
//        return (HashSet<Integer>) list;
//    }
}
