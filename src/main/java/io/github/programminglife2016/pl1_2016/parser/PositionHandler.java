package io.github.programminglife2016.pl1_2016.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * Handler for nodes to calculate positions on the graph.
 */
public class PositionHandler implements PositionManager {
    /**
     * Spacing between segments in the graph.
     */
    private static final int SPACING = 100;

    /**
     * Map containing the DNA seqments.
     */
    private NodeCollection nodeCollection;

    /**
     * Create a PositionHandler.
     * @param nodeCollection Map containing all the segments which positions need to be calculated.
     */
    public PositionHandler(NodeCollection nodeCollection) {
        this.nodeCollection = new NodeMap(nodeCollection.getNodes().size());
        for (Node node : nodeCollection) {
            this.nodeCollection.put(node.getId(), node);
        }
    }

    /**
     * Calculate the positions of the nodes, and set the positions in each node.
     */
    public void calculatePositions() {
        // Ugly and long code, works for us now however.
        Random random = new Random();
        int currx = 0;
        Collection<Node> processed = new ArrayList<Node>();
        // Detect and position snips.
        for (Node node : nodeCollection) {
            if (processed.contains(node)) {
                continue;
            }
            Collection<Node> nodes = new ArrayList<Node>();
            for (Node node2 : nodeCollection) {
                Collection<Node> intersectionBack = new ArrayList<Node>(node2.getBackLinks());
                intersectionBack.retainAll(node.getBackLinks());
                Collection<Node> intersectionFront = new ArrayList<Node>(node2.getLinks());
                intersectionFront.retainAll(node.getLinks());
                if (!intersectionBack.isEmpty() && !intersectionFront.isEmpty()) {
                    nodes.add(node2);
                }
            }
            int height = nodes.size() * SPACING / 2;
            for (Node node2 : nodes) {
                node2.setXY(currx, height);
                height -= SPACING;
            }
            processed.addAll(nodes);
            currx += SPACING;
        }
        // Detect and correct the positions of indels.
        for (Node node : nodeCollection) {
            for (Node nodeBack : node.getBackLinks()) {
                Collection<Node> intersection = new ArrayList<Node>(nodeBack.getLinks());
                intersection.retainAll(node.getLinks());
                if (!intersection.isEmpty()) {
                    node.setXY(node.getX(), node.getY() + SPACING / 2);
                }
            }
        }
        // Remove the inversion links, until we know what to do with them.
        for (Node node : nodeCollection) {
            Iterator<Node> linkIterator = node.getLinks().iterator();
            while (linkIterator.hasNext()) {
                Node node2 = linkIterator.next();
                if (node2.getX() < node.getX()) {
                    linkIterator.remove();
                }
            }
        }
    }
}
