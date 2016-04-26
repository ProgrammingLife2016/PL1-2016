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
 * Test class for NodeList, using the tests in SegmentCollectionTest.
 */
public class SegmentMapTest extends SegmentCollectionTest {
    /**
     * Pass an instance of NodeMap to the superclass.
     */
    @Before
    public void setUp() {
        setNodeCollection(new NodeMap(5));
    }

//    /**
//     * Test coversion into JSON.
//     */
//    @Test
//    public void testToJson() {
//        Node segment1 = mock(Node.class);
//        Node segment2 = mock(Node.class);
//        List<Node> links1 = new ArrayList<Node>();
//        links1.add(segment2);
//        List<Node> links2 = new ArrayList<Node>();
//        when(segment1.getColumn()).thenReturn(1);
//        when(segment1.getData()).thenReturn("one");
//        when(segment1.getId()).thenReturn(1);
//        when(segment1.getLinks()).thenReturn(links1);
//        when(segment2.getColumn()).thenReturn(2);
//        when(segment2.getData()).thenReturn("two");
//        when(segment2.getId()).thenReturn(2);
//        when(segment2.getLinks()).thenReturn(links2);
//        NodeCollection segments = new NodeMap(2);
//        segments.put(1, segment1);
//        segments.put(2, segment2);
//        // TODO: Change to standardized API.
//        assertEquals("{\"1\":{\"id\":1,\"data\":\"one\",\"links\":[2]},\"2\":{\"id\":2,\"data\":\""
//                + "two\",\"links\":[]}}", segments.toJson());
//    }
}
