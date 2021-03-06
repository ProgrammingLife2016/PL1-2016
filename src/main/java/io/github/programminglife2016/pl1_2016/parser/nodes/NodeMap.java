package io.github.programminglife2016.pl1_2016.parser.nodes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.programminglife2016.pl1_2016.parser.metadata.Annotation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter for segment hashmap
 */
public class NodeMap extends HashMap<Integer, Node> implements NodeCollection {
    private static final double SEGMENT_POSITION_FACTOR = 100;
    private static final long serialVersionUID = -4468555693994055308L;

    private List<Annotation> annotations;

    public NodeMap() {
    }

    public NodeMap(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public final String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(NodeMap.class,
                new NodeCollectionSerializer()).create();
        return gson.toJson(this);
    }

    @Override
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    private void resetAllContainerPositions(String[] line) {
        int id = Integer.parseInt(line[1]);
        int x =(int) (Double.parseDouble(line[2]) * SEGMENT_POSITION_FACTOR);
        int y = (int) (Double.parseDouble(line[3]) * SEGMENT_POSITION_FACTOR);
        this.get(id).setXY(x, y);
        this.get(id).getStartNode().setXY(x, y);
        this.get(id).getEndNode().setXY(x, y);
        for (Node contain : this.get(id).getContainer()) {
            contain.setXY(x, y);
        }
    }

    private Node retrieveSegment(Node node, boolean start) {
        if (node.isBubble()) {
            if (start) {
                return retrieveSegment(node.getStartNode(), true);
            } else {
                return retrieveSegment(node.getEndNode(), false);
            }
        }
        return node;
    }
}