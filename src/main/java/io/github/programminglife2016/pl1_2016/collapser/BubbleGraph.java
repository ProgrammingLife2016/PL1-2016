package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi Autar on 26-4-16.
 */
public class BubbleGraph implements Graph {
    private static final int INITIAL_CLUSTERS = 100;

    private NodeCollection fullgraph;

    private NodeCollection currentGraph;

    public BubbleGraph(InputStream is) throws CloneNotSupportedException {
        Parser parser = new SimpleParser();
        parser.parse(is);
        this.fullgraph = parser.getSegmentCollection();

        //TODO not tested
        this.currentGraph = clusterGraph(INITIAL_CLUSTERS, fullgraph.clone());
    }

    public NodeCollection retrieveBoundedGraph(int startx, int endx, int starty, int endy) {
        NodeCollection boundedGraph = new NodeList(currentGraph.size());
        for (Node node : currentGraph) {
            int thisx = node.getX();
            int thisy = node.getY();
            if(thisx >= startx && thisx <= endx && thisy >= starty && thisy <= endy) {
                boundedGraph.put(node.getId(), node);
            }
        }
        this.currentGraph = generateZoomedGraph(boundedGraph);
        return currentGraph;
    }

    public NodeCollection generateZoomedGraph(NodeCollection graph) {
        // TODO: clean this monstrosity
        List<Node> nonnulls = new ArrayList<Node>(graph.size());
        for (Node node : currentGraph) {
            nonnulls.add(node);
        }
        NodeCollection curr = new NodeList(nonnulls.size());
        int i = 1;
        for (Node node : nonnulls) {
            curr.put(i, node);
            i++;
        }

        //Cluster the graph
        curr = clusterGraph(INITIAL_CLUSTERS, curr);

        return curr;
    }

    public NodeCollection clusterGraph(int k, NodeCollection collection) {
        HierarchicalClustering hierarchicalClustering = new HierarchicalClustering(k, collection);
        return hierarchicalClustering.determineClusters();
    }

    public NodeCollection getFullGraph() {
        return fullgraph;
    }

    public NodeCollection getCurrentGraph() {
        return currentGraph;
    }
}
