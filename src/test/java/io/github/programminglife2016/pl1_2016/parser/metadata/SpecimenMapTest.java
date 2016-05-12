package io.github.programminglife2016.pl1_2016.parser.metadata;

import org.junit.Before;

/**
 * Created by ravishivam on 12-5-16.
 */
public class SpecimenMapTest extends SpecimenCollectionTest {

    /**
     * Pass an instance of Specimenmap to the superclass.
     */
    @Before
    public void setUp() {
        setSpecimenCollection(new SpecimenMap());
    }
}
