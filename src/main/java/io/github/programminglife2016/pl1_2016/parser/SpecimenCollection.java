package io.github.programminglife2016.pl1_2016.parser;

import java.util.Collection;
import java.util.Iterator;

/**
 * A data structure that represents the segments.
 */
public interface SpecimenCollection extends JsonSerializable, Iterable<Subject> {
    /**
     * Add a segment to the collection.
     *
     * @param id      id of the segment
     * @param specimen the actual segment
     * @return the segment parameter
     */
    Subject put(String id, Subject specimen);

    /**
     * Get a segment by id.
     *
     * @param id id of the segment
     * @return segment
     */
    Subject get(Object id);

    /**
     * Checks if the collection contains a particular key.
     *
     * @param id id
     * @return true if the collection contains an item with this id, otherwise false
     */
    boolean containsKey(Object id);

    /**
     * Return all segments.
     * @return all segments.
     */
    Collection<Subject> getSpecimen();

    /**
     * Iterator for the collection.
     * @return iterator
     */
    default Iterator<Subject> iterator() {
        return getSpecimen().iterator();
    }
}
