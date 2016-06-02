package io.github.programminglife2016.pl1_2016.parser.nodes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter for segment hashmap
 */
public class NodeMap extends HashMap<Integer, Node> implements NodeCollection {
    @Override
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(NodeMap.class,
                new NodeCollectionSerializer()).create();
        return gson.toJson(this);
    }


    /**
     * Recalculates the positions of the bubbles contained in the hashmap and leaves all segment
     * positions as is.
     */
    @Override
    public void recalculatePositions() {
        for(Map.Entry<Integer, Node> entry : entrySet()) {
            Node node = entry.getValue();
            int x = (node.getStartNode().getX() + node.getEndNode().getX())/2;
            int y = (node.getStartNode().getY() + node.getEndNode().getY())/2;
            this.get(entry.getKey()).setXY(x, y);
        }
    }
}
