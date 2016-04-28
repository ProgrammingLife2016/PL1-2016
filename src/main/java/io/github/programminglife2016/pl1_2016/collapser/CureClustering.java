package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravishivam on 27-4-16.
 */
public class CureClustering implements ClusterHandler {

    private static final int NUM_REPRESENTATIVES = 5;

    private int clusters;

    private NodeCollection collection;

    public CureClustering(int clusters, NodeCollection collection) {
        this.clusters = clusters;
        this.collection = collection;
    }

    public NodeCollection retrieveClusters() throws CloneNotSupportedException {
        int samplesize = (collection.size() * 2)/100;
        NodeCollection sample = new NodeList(samplesize);
        for (int i = 1; i < samplesize; i++) {
            sample.put(i, collection.get((int)(Math.random()*samplesize + 1)).clone());
        }
        ClusterHandler clustering = new HierarchicalClustering(clusters, sample);
        NodeCollection cureCollection = clustering.retrieveClusters();
        return determineCureClusters(cureCollection);
    }

    public NodeCollection determineCureClusters(NodeCollection cureCollection) {
        List<Node> representatives = new ArrayList<>();
        determineRepresentative(representatives, cureCollection);
        for (Node node :
                cureCollection) {
            Bubble bubble = (Bubble) node;
            bubble.clear();
        }
        for (Node realnode : collection) {
            double mindist = Double.POSITIVE_INFINITY;
            Node curr = realnode;
            for (Node repr : representatives) {
                double dist = curr.distanceTo(repr);
                if(dist<mindist){
                    mindist = dist;
                    curr = repr;
                }
            }
            ((Bubble) cureCollection.get(curr.getId())).add(realnode);
        }
        return cureCollection;
    }

    private void determineRepresentative(List<Node> representatives, NodeCollection cureCollection) {
        for (Node node : cureCollection) {
            Bubble cluster =  (Bubble) node;
            int centerx = cluster.getX();
            int centery = cluster.getY();
            for (int i = 0; i < NUM_REPRESENTATIVES; i++) {
                if(cluster.size()==0) break;
                Node randomRep = cluster.remove((int) (Math.random()*cluster.size()));
                int randomx = randomRep.getX();
                int randomy = randomRep.getY();
                if(randomx==centerx) continue;
                Node adder = new Segment(node.getId());
                double l = pythagoras(centerx, centery, randomx, randomy)*0.8;
                System.out.println(randomy + "" + centery);
                System.out.println(randomx + "" + centerx);
                double alpha = Math.acos((randomy-centery)/(randomx-centerx));
                adder.setXY((int)(l*Math.cos(alpha)+centerx), (int) (l*Math.sin(alpha)+centery));
                representatives.add(randomRep);
            }
        }

    }

    private double pythagoras(int centerx, int centery, int randomx, int randomy) {
        return Math.sqrt(Math.pow(centerx-randomx, 2)+Math.pow(centery-randomy, 2));
    }
}
