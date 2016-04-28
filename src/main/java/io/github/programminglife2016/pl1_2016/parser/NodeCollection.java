package io.github.programminglife2016.pl1_2016.parser;

import java.util.Collection;
import java.util.Iterator;

/**
 * A data structure that represents the segments.
 */
public interface NodeCollection extends JsonSerializable, Iterable<Node> {
    /**
     * Add a segment to the collection.
     *
     * @param id      id of the segment
     * @param node the actual segment
     * @return the segment parameter
     */
    Node put(Integer id, Node node);

    /**
     * Get a segment by id.
     *
     * @param id id of the segment
     * @return segment
     */
    Node get(Object id);

    /**
     * Checks if the collection contains a particular key.
     *
     * @param id id
     * @return true if the collection contains an item with this id, otherwise false
     */
    boolean containsKey(Object id);

    /**
     * Return size of the list.
     * @return size of the list.
     */
    int size();

    /**
     * Make the object cloneable.
     * @return A deep clone of the collection.
     *
     */
    NodeCollection clone() throws CloneNotSupportedException;

    /**
     * Return all segments.
     * @return all segments.
     */
    Collection<Node> getNodes();

    /**
     * Iterator for the collection.
     * @return iterator
     */
    default Iterator<Node> iterator() {
        return getNodes().iterator();
    }
}
