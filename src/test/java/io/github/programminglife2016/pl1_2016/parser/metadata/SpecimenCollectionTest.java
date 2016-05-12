package io.github.programminglife2016.pl1_2016.parser.metadata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by ravishivam on 12-5-16.
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class SpecimenCollectionTest {
    private SpecimenCollection collection;

    @Mock
    private Specimen specimen;

    public void setSpecimenCollection(SpecimenCollection collection) {
        this.collection = collection;
    }

    @Test
    public void setFirstElement() {
        collection.put("specimen1", specimen);
        assertEquals(specimen, collection.get("specimen1"));
    }

}
