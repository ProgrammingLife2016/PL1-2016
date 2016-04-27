package io.github.programminglife2016.pl1_2016.parser;

/**
 * Created by ravishivam on 27-4-16.
 */
public class HierarchicalClustering implements ClusterHandler {

    private int clusters;

    private NodeCollection fullgraph;

    private NodeCollection clusteredGraph;

    public HierarchicalClustering(int clusters, NodeCollection collection) {
        this.clusters = clusters;
        this.fullgraph = collection;
        initClusters();
    }

    public NodeCollection determineClusters() {
        while(clusteredGraph.size()!=this.clusters) {
            update();
        }
        return clusteredGraph;
    }

    /**
     * Performs one update step of the algorithm.
     */
    public void update() {
        if (clusteredGraph.size() == this.clusters)
            return;
        double smallestDistance = Double.POSITIVE_INFINITY;
        Node a = new Bubble();
        Node b = new Bubble();
        for (int i = 0; i < clusteredGraph.size(); i++) {
            for (int j = i + 1; j < clusteredGraph.size(); j++) {
                double distance = clusteredGraph.get(i).distanceTo(clusteredGraph.get(j));
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    a = clusteredGraph.get(i);
                    b = clusteredGraph.get(j);
                }
            }
        }
        ((Bubble) a).addAll((Bubble) b);
//        clusteredGraph.remove(b);
    }
    private void initClusters() {
        for (Node node :
                (Node[]) this.fullgraph.getCollection()) {
            if(node instanceof Segment) {
                Bubble cluster = new Bubble();
                cluster.add(node);
                this.clusteredGraph.put(this.clusteredGraph.size(), cluster);
            }
            else {
                this.clusteredGraph.put(this.clusteredGraph.size(), node);
            }
        }
    }

    public int getNumClusters() {
        return this.clusters;
    }

    public NodeCollection getFullgraph() {
        return fullgraph;
    }

    public NodeCollection getClusteredGraph() {
        return clusteredGraph;
    }
}
