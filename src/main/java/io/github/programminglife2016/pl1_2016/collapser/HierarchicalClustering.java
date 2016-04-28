package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravishivam on 27-4-16.
 */
public class HierarchicalClustering implements ClusterHandler {

    private int clusters;

    private List<Node> clusteredGraph;

    public HierarchicalClustering(int clusters, NodeCollection collection) {
        this.clusters = clusters;
        this.clusteredGraph = new ArrayList<Node>();
        initClusters(collection);
    }

    public NodeCollection retrieveClusters() {
        while(clusteredGraph.size()!=this.clusters) {
            System.out.println(this.clusteredGraph.size());
            update();
        }
        int i = 1;
        NodeCollection currentGraph = new NodeList(clusteredGraph.size());
        for (Node node : clusteredGraph) {
            currentGraph.put(i++, node);
        }
        return currentGraph;
    }

    /**
     * Performs one update step of the algorithm.
     */
    public void update() {
        if (clusteredGraph.size() == this.clusters)
            return;
        double smallestDistance = Double.POSITIVE_INFINITY;
        Bubble a = new Bubble();
        Bubble b = new Bubble();
        for (int i = 0; i < clusteredGraph.size(); i++) {
            for (int j = i + 1; j < clusteredGraph.size(); j++) {
                double distance = clusteredGraph.get(i).distanceTo(clusteredGraph.get(j));
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    a = (Bubble) clusteredGraph.get(i);
                    b = (Bubble) clusteredGraph.get(j);
                }
            }
        }
        a.addAll(b);
        clusteredGraph.remove(b);
    }
    
    private void initClusters(NodeCollection collection) {
        for (Node node :
                collection.getNodes()) {
            if(node instanceof Segment) {
                Bubble cluster = new Bubble();
                cluster.add(node);
                this.clusteredGraph.add(cluster);
            }
            else {
                this.clusteredGraph.add(node);
            }
        }
    }
}
