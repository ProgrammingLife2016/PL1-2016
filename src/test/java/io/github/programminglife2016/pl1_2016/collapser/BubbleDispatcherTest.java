//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import org.apache.commons.io.IOUtils;
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

    /**
     * Create parser object.
     *
     * @throws IOException thrown if the test data is faulty
     */
    @Before
    public void setUp() throws IOException {
        InputStream is = IOUtils.toInputStream(BubbleCollapserTest.DATA, "UTF-8");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        dispatcher = new BubbleDispatcher(nodeCollection);
    }

    /**
     * Verify if the dispatcher has created the correct number of nodes.
     */
    @Test
    public void testDispatchingCorrectView() {
        NodeCollection testCollection = dispatcher.getLevelBubbles(0, 4);
        assertEquals(42, testCollection.size());
    }
}
