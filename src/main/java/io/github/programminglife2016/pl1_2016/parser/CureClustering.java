package io.github.programminglife2016.pl1_2016.parser;

/**
 * Created by ravishivam on 27-4-16.
 */
public class CureClustering implements ClusterHandler{

    private int clusters;
    private NodeCollection collection;

    public CureClustering(int clusters, NodeCollection collection) {
        this.clusters = clusters;
        this.collection = collection;
    }

    public NodeCollection determineClusters() {
        return null;
    }
}
