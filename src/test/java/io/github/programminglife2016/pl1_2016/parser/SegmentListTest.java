//CHECKSTYLE.OFF: MagicNumber

package io.github.programminglife2016.pl1_2016.parser;

import org.junit.Before;

/**
 * Test class for NodeList, using the tests in SegmentCollectionTest.
 */
public class SegmentListTest extends SegmentCollectionTest {
    /**
     * Pass an instance of NodeList to the superclass.
     */
    @Before
    public void setUp() {
        setNodeCollection(new NodeList(5));
    }
}
