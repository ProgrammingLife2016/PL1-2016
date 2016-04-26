//CHECKSTYLE.OFF: MagicNumber

package io.github.programminglife2016.pl1_2016.parser;

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
}
