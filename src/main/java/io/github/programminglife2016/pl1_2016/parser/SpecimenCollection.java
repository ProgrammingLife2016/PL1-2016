package io.github.programminglife2016.pl1_2016.parser;

import java.util.Collection;
import java.util.Iterator;

/**
 * A data structure that represents the segments.
 */
public interface SpecimenCollection extends JsonSerializable, Iterable<Specimen> {
    /**
     * Add a segment to the collection.
     *
     * @param id      id of the segment
     * @param specimen the actual segment
     * @return the segment parameter
     */
    Specimen put(Integer id, Specimen specimen);

    /**
     * Get a segment by id.
     *
     * @param id id of the segment
     * @return segment
     */
    Specimen get(Object id);

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
    Collection<Specimen> getSpecimen();

    /**
     * Iterator for the collection.
     * @return iterator
     */
    default Iterator<Specimen> iterator() {
        return getSpecimen().iterator();
    }
}
