package io.github.programminglife2016.pl1_2016.parser.metadata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver.iterator;
import static org.junit.Assert.assertEquals;

/** Abstract test class for classes extending SpecimenCollection.
 * Created by ravishivam on 12-5-16.
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class SpecimenCollectionTest {
    private SpecimenCollection collection;

    @Mock
    private Specimen specimen;

    /**
     * Initialize local collection with the one that is passed on by extended
     * class.
     * @param collection Collection passed on by extended class.
     */
    public void setSpecimenCollection(SpecimenCollection collection) {
        this.collection = collection;
    }

    /**
     * Test basic put and get method of the specimenCollection.
     */
    @Test
    public void setFirstElement() {
        collection.put("specimen1", specimen);
        assertEquals(specimen, collection.get("specimen1"));
    }

    /**
     * Test the iterator of specimentCollection.
     */
    @Test
    public void testContainsItem() {
        collection.put("specimen1", specimen);
        collection.put("specimen1", specimen);
        Iterator<Subject> it = collection.iterator();
        assertEquals(false, collection.containsKey((Specimen)it.next()));
    }
}
