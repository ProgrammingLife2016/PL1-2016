package io.github.programminglife2016.pl1_2016.parser.metadata;

import java.util.Collection;
import java.util.HashMap;

/**
 * Adapter for segment hashmap
 */
public class SpecimenMap extends HashMap<String, Subject> implements SpecimenCollection {

    /**
     * Create segment hashmap.
     */
    public SpecimenMap() {
    }
    /**
     * Return all segments.
     *
     * @return all segments
     */
    public Collection<Subject> getSpecimen() {
        return values();
    }

    @Override
    public String toJson() {
        return "";
    }
}