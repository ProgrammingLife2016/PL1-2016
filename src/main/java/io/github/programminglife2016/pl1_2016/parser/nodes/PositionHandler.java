package io.github.programminglife2016.pl1_2016.parser.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Handler for nodes to calculate positions on the graph.
 */
public class PositionHandler implements PositionManager {
    /**
     * Spacing between segments in the graph.
     */
    private static final int SPACING = 100;

    private static final int PRINT_TIMES = 500;
    private static final int PERCENT = 100;

    /**
     * Used for percentage printing.
     */
    private int percentageParts = 2;
    private int currnode = 0;

    /**
     * Map containing the DNA seqments.
     */
    private NodeCollection nodeCollection;

    /**
     * Create a PositionHandler.
     * @param nodeCollection Map containing all the segments which positions need to be calculated.
     */
    public PositionHandler(NodeCollection nodeCollection) {
        this.nodeCollection = new NodeList(nodeCollection.getNodes().size());
        for (Node node : nodeCollection) {
            this.nodeCollection.put(node.getId(), node);
        }
    }

    /**
     * Calculate the positions of the nodes, and set the positions in each node.
     */
    public void calculatePositions() {
        int currx = 0;
        Collection<Node> processed = new ArrayList<Node>();
        // Detect and position snips.
        for (Node node : nodeCollection) {
            printPercentage(nodeCollection.getNodes().size());
            if (!processed.contains(node)) {
                node.calculatePosition(nodeCollection, processed, SPACING, currx);
                currx += SPACING;
            }
        }
        // Detect and correct the positions of indels.
        for (Node node : nodeCollection) {
            printPercentage(nodeCollection.getNodes().size());
            node.correctIndelPosition(SPACING);
        }
        System.out.println("\rPositioning... 100%");
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

    /**
     * Print the percentage 500 times in total.
     *
     * @param totalSize size of node collection
     */
    private void printPercentage(int totalSize) {
        currnode++;
        if (totalSize < PRINT_TIMES) {
            System.out.print(String.format("\rPositioning... %.1f%%.", (double) currnode
                    / totalSize * (PERCENT / percentageParts)));
        } else if (currnode % (totalSize / PRINT_TIMES) == 0) {
            System.out.print(String.format("\rPositioning... %.1f%%.", (double) currnode
                    / totalSize * (PERCENT / percentageParts)));
        }
    }
}
