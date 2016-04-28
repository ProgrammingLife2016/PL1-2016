package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Represent segments as a list.
 */
public class NodeList implements NodeCollection, Cloneable {
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
     * Remove a segment from the collection.
     *
     * @param id id of the node.
     * @return the node removed.
     */
    public boolean remove(Integer id) {
        array[id - 1] = null;
        return true;
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
     * Return size of the list.
     * @return size of the list.
     */
    public int size() {
        return this.array.length;
    }

    /**
     * Return all segments.
     * @return all segments
     */
    public Collection<Node> getNodes() {
        return Arrays.asList(array).stream().filter(x -> x != null).collect(Collectors.toList());
    }

    /**
     * Convert the representation to JSON.
     *
     * @return JSON representation of this object.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(NodeList.class,
                new NodeCollectionSerializer()).create();
        return gson.toJson(this);
    }

    /**
     * Return a deep cloned object of the collection
     * @return Deep cloned object of node collection
     * @throws CloneNotSupportedException Not a cloneable object
     */
    public NodeCollection clone() throws CloneNotSupportedException {
        return (NodeCollection) super.clone();
    }
}
