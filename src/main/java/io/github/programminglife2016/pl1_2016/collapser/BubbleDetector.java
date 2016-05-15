package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravishivam on 15-5-16.
 */
public class BubbleDetector {

    private boolean[] visited;

    private NodeCollection collection;

    public BubbleDetector(NodeCollection collection) {
        this.visited = new boolean[collection.size() + 1];
        initVisited(collection);
        this.collection = collection;
    }

    public void DFS(NodeCollection collection, Node source) {
        visited[source.getId()] = true;
        List<Node> connectedTo = new ArrayList<>(source.getLinks());
        for (Node child : connectedTo) {
            if (visited[child.getId()] == false) {
                DFS(collection, child);
            }
        }
    }

    private void initVisited(NodeCollection collection) {
        for (Node node : collection.getNodes()) {
            visited[node.getId()] = false;
        }
    }
}
