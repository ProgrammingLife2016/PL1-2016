//CHECKSTYLE.OFF: MagicNumber

package io.github.programminglife2016.pl1_2016.parser.nodes;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

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
        Mockito.when(segment.getId()).thenReturn(3);
        nodeCollection.remove(segment.getId());
        assertFalse(nodeCollection.containsKey(3));
    }

    /**
     * Test containsKey with no segment in that place.
     */
    @Test
    public void testContainsKeyFalse() {
        nodeCollection.put(2, segment);
        assertFalse(nodeCollection.containsKey(3));
    }

    private void createFiveNodes() {
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
    }
    /**
     * Test conversion into JSON.
     */
    @Test
    public void testToJson() {
        createFiveNodes();
        JsonParser jsonParser = new JsonParser();
        JsonElement actual = jsonParser.parse(nodeCollection.toJson());
        assertEquals(2, actual.getAsJsonObject().get("nodes").getAsJsonArray().get(1)
                .getAsJsonObject().get("id").getAsInt());
    }

    /**
     * Test Basic segment operations.
     */
    @Test
    public void testBasicNodeOperations() {
        createFiveNodes();
        Segment segment1 = (Segment) nodeCollection.get(1);
        segment1.setData("data");
        assertEquals("data", segment1.getData());
        Node seg = segment1.clone();
        assertEquals(seg, segment1);
    }

    /**
     * Test basic getter functions.
     */
    @Test
    public void testGettersSegment() {
        Segment segment1 = new Segment(1);
        segment1.setXY(2, 3);
        segment1.setColumn(1);
        assertEquals(2, segment1.getX());
        assertEquals(3, segment1.getY());
        assertEquals(1, segment1.getColumn());
        assertEquals("Segment{id=1, x=2, y=3, containerid=0}", segment1.toString());
    }
}
