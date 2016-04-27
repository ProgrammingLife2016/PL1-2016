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
        return null;
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
}
