package io.github.programminglife2016.pl1_2016.parser.nodes;

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

    @Override
    public void removeNode(Node node) {
        remove(node.getId());
    }

    @Override
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(NodeMap.class, new NodeCollectionSerializer()).create();
        return gson.toJson(this);
    }
}
