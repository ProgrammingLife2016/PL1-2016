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


    /**
     * Recalculates the positions of the bubbles contained in the hashmap and leaves all segment
     * positions as is.
     */
    @Override
    public final void recalculatePositions() {
        for (Map.Entry<Integer, Node> entry : entrySet()) {
            Node node = entry.getValue();
            Node start = retrieveSegment(node, true);
            Node end = retrieveSegment(node, false);
            int x = (start.getX() + end.getX()) / 2;
            int y = (start.getY() + end.getY()) / 2;
            this.get(entry.getKey()).setXY(x, y);
        }
    }

    @Override
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public void assignNewPositions(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(" ");
                if (data[0].equals("node")) {
                    resetAllContainerPositions(data);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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