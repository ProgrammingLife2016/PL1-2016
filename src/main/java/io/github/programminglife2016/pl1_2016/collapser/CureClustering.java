package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.NodeCollection;

/**
 * Created by ravishivam on 27-4-16.
 */
public class CureClustering implements ClusterHandler {

    private int clusters;
    private NodeCollection collection;

    public CureClustering(int clusters, NodeCollection collection) {
        this.clusters = clusters;
        this.collection = collection;
    }

    public NodeCollection determineClusters() {
        ClusterHandler clustering = new HierarchicalClustering(clusters, collection);
        return null;
    }
}