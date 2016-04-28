package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collection;
import java.util.HashMap;

/**
 * Adapter for segment hashmap
 */
public class NodeMap extends HashMap<Integer, Node> implements NodeCollection {

    /**
     * Create segment hashmap.
     * @param initialCapacity capacity of the hashmap.
     */
    public NodeMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Return all segments.
     *
     * @return all segments
     */
    public Collection<Node> getNodes() {
        return values();
    }

    public HashMap<Integer, Node> getCollection(){
        return this;
    }
    /**
     * Redefine clone for hashmap.
     * @return deep clone for NodeMap.
     */
    @Override
    public NodeCollection clone() {
        return (NodeCollection) super.clone();
    }
}
