//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.Launcher;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * Test for the BubbleDispatcher class.
 */
public class BubbleDispatcherTest {
    private BubbleDispatcher dispatcher;
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
        META = Launcher.class.getClass().getResourceAsStream("/genomes/metadata.csv");
        INPUT = Launcher.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        POS = Launcher.class.getClass().getResourceAsStream("/genomes/TB10.positions");
        NodeCollection nodeCollection = new SegmentParser(POS, META).parse(INPUT);
        dispatcher = new BubbleDispatcher(nodeCollection);
    }

    /**
     * Verify if the dispatcher has created the correct number of nodes.
     */
    @Test
    public void testDispatchingCorrectView() {
        NodeCollection testCollection = dispatcher.getThresholdedBubbles(4);
        assertEquals(3447, testCollection.size());
    }
}
