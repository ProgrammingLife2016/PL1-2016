package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Represent segments as a list.
 */
public class NodeList implements NodeCollection {
    private Node[] array;

    /**
     * Initialize NodeList with an initial capacity.
     *
     * @param initialCapacity the initial capacity
     */
    public NodeList(int initialCapacity) {
        array = new Node[initialCapacity];
    }
    /**
     * Add a segment to the collection.
     *
     * @param id      id of the segment
     * @param segment the actual segment
     * @return the segment parameter
     */
    public Node put(Integer id, Node segment) {
        array[id - 1] = segment;
        return segment;
    }

    /**
     * Get a segment by id.
     *
     * @param id id of the segment. IllegalArgumentException if id is not an integer
     * @return segment
     */
    public Node get(Object id) {
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
