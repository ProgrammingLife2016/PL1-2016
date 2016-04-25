package io.github.programminglife2016.pl1_2016.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * Abstract test class for SegmentCollection
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class SegmentCollectionTest {
    private SegmentCollection segmentCollection;

    @Mock
    private Segment segment;

    /**
     * Set the SegmentCollection to be a concrete object (of an implementation). Note: set the size
     * of the SegmentCollection to 5.
     *
     * @param segmentCollection object to be tested
     */
    public void setSegmentCollection(SegmentCollection segmentCollection) {
        this.segmentCollection = segmentCollection;
    }

    /**
     * Put a segment with index 1 (the first index), and retrieve it.
     */
    @Test
    public void testPutFirstIndex() {
        segmentCollection.put(1, segment);
        assertEquals(segment, segmentCollection.get(1));
    }

    /**
     * A non-existant index should be null.
     */
    @Test
    public void testGetNonExistantIndex() {
        segmentCollection.put(2, segment);
        assertNull(segmentCollection.get(1));
    }

    /**
     * Put a segment with the last segment and retrieve it.
     */
    @Test
    public void testGetLastIndex() {
        segmentCollection.put(5, segment);
        assertEquals(segment, segmentCollection.get(5));
    }

    /**
     * Test containsKey with a segment in that place.
     */
    @Test
    public void testContainsKeyTrue() {
        segmentCollection.put(3, segment);
        assertTrue(segmentCollection.containsKey(3));
    }

    /**
     * Test containsKey with no segment in that place.
     */
    @Test
    public void testContainsKeyFalse() {
        segmentCollection.put(2, segment);
        assertFalse(segmentCollection.containsKey(3));
    }
}
