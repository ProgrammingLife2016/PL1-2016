package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
     * Convert hashmap to JSON representation and return as string.
     * @return hashmap converted to JSON string.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Node.class, new SegmentSerializer())
                .create();
        return gson.toJson(this);
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
