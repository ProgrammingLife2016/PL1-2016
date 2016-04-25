//CHECKSTYLE.OFF: MagicNumber

package io.github.programminglife2016.pl1_2016.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for SegmentList, using the tests in SegmentCollectionTest.
 */
public class SegmentListTest extends SegmentCollectionTest {
    /**
     * Pass an instance of SegmentList to the superclass.
     */
    @Before
    public void setUp() {
        setSegmentCollection(new SegmentList(5));
    }

    /**
     * Test coversion into JSON.
     */
    @Test
    public void testToJson() {
        Segment segment1 = mock(Segment.class);
        Segment segment2 = mock(Segment.class);
        List<Segment> links1 = new ArrayList<Segment>();
        links1.add(segment2);
        List<Segment> links2 = new ArrayList<Segment>();
        when(segment1.getColumn()).thenReturn(1);
        when(segment1.getData()).thenReturn("one");
        when(segment1.getId()).thenReturn(1);
        when(segment1.getLinks()).thenReturn(links1);
        when(segment2.getColumn()).thenReturn(2);
        when(segment2.getData()).thenReturn("two");
        when(segment2.getId()).thenReturn(2);
        when(segment2.getLinks()).thenReturn(links2);
        SegmentCollection segments = new SegmentList(2);
        segments.put(1, segment1);
        segments.put(2, segment2);
        // TODO: Change to standardized API.
        assertEquals("{\"array\":[{\"id\":1,\"data\":\"one\",\"links\":[2]},{\"id\":2,\"data\":\"t"
                + "wo\",\"links\":[]}]}", segments.toJson());
    }
}
