package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kamran Tadzjibov on 14.06.2016.
 */
public class BubbleAligner {

    private List<Node> bubbleCollection;

    /**
     * Create instance of bubbel aligner to align the position the node in the graph.
     * @param bubbleCollection collection of collapsed bubbles in the graph.
     */
    public BubbleAligner(Set<Node> bubbleCollection) {
        this.bubbleCollection = new ArrayList<>(bubbleCollection);
    }

    /**
     * Smooth the bubbles by minimizing the variance in the y coordinate of the bubbles.
     * @return collection with bubbles with modified posititons.
     */
    public Collection<Node> alignVertical() {
        sortByX(bubbleCollection);
        bubbleCollection.forEach(this::setNewY);
        modify(bubbleCollection.get(0), true);
        return bubbleCollection;
    }

    private void sortByX(Collection<Node> nodes) {
        Comparator<Node> comparatorByX = (n1, n2) -> Integer.compare(
                n1.getX(), n2.getX());
        nodes.stream().sorted(comparatorByX).collect(Collectors.toList());
    }

    private void modify(Node node, boolean isMainBranch) {
        Optional<Node> optMain =
                node.getLinks().stream().max(Comparator.comparing(x -> x.getGenomes().size()));
        if (optMain.isPresent()) {
            Node mainNode = optMain.get();
            if (!isMainBranch && node.getGenomes().size() < mainNode.getGenomes().size()) {
                return;
            }
            for (Node next : node.getLinks()) {
                if (next.getId() != mainNode.getId()) {
                    next.setXY(next.getX(), next.getY() + (node.getY() - mainNode.getY()));
                    modify(next, false);
                }
            }
            mainNode.setXY(mainNode.getX(), node.getY());
            modify(mainNode, isMainBranch);
        }
    }

    private void setNewY(Node node) {
        if (node.getLinks().size() == 1) {
            Node next = node.getLinks().iterator().next();
            next.setXY(next.getX(), node.getY());
        }
    }
}
