package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Represent segments as a list. Warning: the size of the SegmentList must be set beforehand.
 */
public class SegmentList extends ArrayList<Segment> implements SegmentCollection {
    /**
     * Add a segment to the collection.
     *
     * @param id      id of the segment
     * @param segment the actual segment
     * @return the segment parameter
     */
    public Segment put(Integer id, Segment segment) {
        add(id, segment);
        return segment;
    }

    /**
     * Get a segment by id.
     *
     * @param id id of the segment
     * @return segment
     */
    public Segment get(Object id) {
        if (id instanceof Integer) {
            return get((Integer) id);
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
     * Convert the representation to JSON.
     *
     * @return JSON representation of this object.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Segment.class, new SegmentSerializer())
                .create();
        return gson.toJson(this);
    }
}
