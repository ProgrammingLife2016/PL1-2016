package io.github.programminglife2016.pl1_2016.parser;

/**
 * A data structure that represents the segments.
 */
public interface SegmentCollection extends JsonSerializable {
    /**
     * Add a segment to the collection.
     *
     * @param id      id of the segment
     * @param segment the actual segment
     * @return the segment parameter
     */
    Segment put(Integer id, Segment segment);

    /**
     * Get a segment by id.
     *
     * @param id id of the segment
     * @return segment
     */
    Segment get(Object id);

    /**
     * Checks if the collection contains a particular key.
     *
     * @param id id
     * @return true if the collection contains an item with this id, otherwise false
     */
    boolean containsKey(Object id);
}
