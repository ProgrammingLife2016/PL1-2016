//CHECKSTYLE.OFF: MagicNumber

package io.github.programminglife2016.pl1_2016.parser.nodes;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Abstract test class for NodeCollection
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class SegmentCollectionTest {
    private NodeCollection nodeCollection;

    @Mock
    private Segment segment;

    /**
     * Set the NodeCollection to be a concrete object (of an implementation). Note: set the size
     * of the NodeCollection to 5.
     *
     * @param nodeCollection object to be tested
     */
    public void setNodeCollection(NodeCollection nodeCollection) {
        this.nodeCollection = nodeCollection;
    }

    /**
     * Put a segment with index 1 (the first index), and retrieve it.
     */
    @Test
    public void testPutFirstIndex() {
        nodeCollection.put(1, segment);
        assertEquals(segment, nodeCollection.get(1));
    }

    /**
     * A non-existant index should be null.
     */
    @Test
    public void testGetNonExistantIndex() {
        nodeCollection.put(2, segment);
        assertNull(nodeCollection.get(1));
    }

    /**
     * Put a segment with the last segment and retrieve it.
     */
    @Test
    public void testGetLastIndex() {
        nodeCollection.put(5, segment);
        assertEquals(segment, nodeCollection.get(5));
    }

    /**
     * Test containsKey with a segment in that place.
     */
    @Test
    public void testContainsKeyTrue() {
        nodeCollection.put(3, segment);
        assertTrue(nodeCollection.containsKey(3));
    }

    /**
     * Test containsKey with no segment in that place.
     */
    @Test
    public void testContainsKeyFalse() {
        nodeCollection.put(2, segment);
        assertFalse(nodeCollection.containsKey(3));
    }

    /**
     * Test conversion into JSON.
     */
    @Test
    public void testToJson() {
        Segment segment1 = new Segment(1, "one", 1);
        Segment segment2 = new Segment(2, "two", 2);
        Segment segment3 = new Segment(3, "three", 3);
        Segment segment4 = new Segment(4, "four", 4);
        Segment segment5 = new Segment(5, "five", 5);
        segment1.addLink(segment2);
        segment1.addLink(segment3);
        segment1.addLink(segment5);
        segment3.addLink(segment4);
        segment4.addLink(segment5);
        nodeCollection.put(1, segment1);
        nodeCollection.put(2, segment2);
        nodeCollection.put(3, segment3);
        nodeCollection.put(4, segment4);
        nodeCollection.put(5, segment5);
        JsonParser jsonParser = new JsonParser();
        JsonElement actual = jsonParser.parse(nodeCollection.toJson());
        JsonElement expected = jsonParser.parse("{\"status\":\"success\",\"nodes\":[{\"id\":1,\"bu"
                + "bble\":false,\"data\":\"one\",\"x\":0,\"y\":0},{\"id\":2,\"bubble\":false,\"dat"
                + "a\":\"two\",\"x\":0,\"y\":0},{\"id\":3,\"bubble\":false,\"data\":\"three\",\"x"
                + "\":0,\"y\":0},{\"id\":4,\"bubble\":false,\"data\":\"four\",\"x\":0,\"y\":0},{\""
                + "id\":5,\"bubble\":false,\"data\":\"five\",\"x\":0,\"y\":0}],\"edges\":[{\"from"
                + "\":1,\"to\":2},{\"from\":1,\"to\":3},{\"from\":1,\"to\":5},{\"from\":3,\"to\":4"
                + "},{\"from\":4,\"to\":5}]}");
        assertEquals(expected, actual);
    }
}
