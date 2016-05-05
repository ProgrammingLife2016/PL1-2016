package io.github.programminglife2016.pl1_2016.parser;

import java.util.Collection;
import java.util.HashMap;
/**
 * Created by ravishivam on 4-5-16.
 */
public class SpecimenList extends HashMap<String, Specimen> implements NodeCollection {


    /**
     * Create segment hashmap.
     * @param initialCapacity capacity of the hashmap.
     */
    public SpecimenList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Return all segments.
     *
     * @return all segments
     */
    public Collection<Specimen> getNodes() {
        return values();
    }
}
