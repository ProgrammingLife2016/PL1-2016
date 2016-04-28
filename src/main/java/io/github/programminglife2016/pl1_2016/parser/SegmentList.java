package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represent segments as a list.
 */
public class SegmentList implements SegmentCollection {
    private Segment[] array;

    /**
     * Initialize SegmentList with an initial capacity.
     *
     * @param initialCapacity the initial capacity
     */
    public SegmentList(int initialCapacity) {
        array = new Segment[initialCapacity];
    }
    /**
     * Add a segment to the collection.
     *
     * @param id      id of the segment
     * @param segment the actual segment
     * @return the segment parameter
     */
    public Segment put(Integer id, Segment segment) {
        array[id - 1] = segment;
        return segment;
    }

    /**
     * Get a segment by id.
     *
     * @param id id of the segment. IllegalArgumentException if id is not an integer
     * @return segment
     */
    public Segment get(Object id) {
        if (id instanceof Integer) {
            return array[((Integer) id) - 1];
        } else {
            throw new IllegalArgumentException("The index must be an integer");
        }
    }


    /**
     * Checks if the collection contains a particular key.
     *
     * @param id id
     * @return true if the collection contains an item with this id, otherwise false
     */
    public boolean containsKey(Object id) {
        return get(id) != null;
    }

    /**
     * Return all segments.
     *
     * @return all segments
     */
    public Collection<Segment> getSegments() {
        return Arrays.asList(array);
    }

    /**
     * Convert the representation to JSON.
     *
     * @return JSON representation of this object.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(SegmentList.class,
                new SegmentCollectionSerializer()).create();
        return gson.toJson(this);
    }
}
