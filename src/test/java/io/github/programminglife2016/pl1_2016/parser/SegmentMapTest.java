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
public class SegmentMapTest extends SegmentCollectionTest {
    /**
     * Pass an instance of SegmentMap to the superclass.
     */
    @Before
    public void setUp() {
        setSegmentCollection(new SegmentMap(5));
    }
}
