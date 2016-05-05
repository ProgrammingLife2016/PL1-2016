package io.github.programminglife2016.pl1_2016.parser;

import java.util.Collection;
import java.util.HashMap;

/**
 * Adapter for segment hashmap
 */
public class SpecimenMap extends HashMap<Integer, Specimen> implements SpecimenCollection {

    /**
     * Create segment hashmap.
     */
    public SpecimenMap() {
    }
    /**
     * Create segment hashmap.
     * @param initialCapacity capacity of the hashmap.
     */
    public SpecimenMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Return all segments.
     *
     * @return all segments
     */
    public Collection<Specimen> getSpecimen() {
        return values();
    }
}