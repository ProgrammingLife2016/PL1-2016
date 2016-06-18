package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kamran Tadzjibov on 14.06.2016.
 */
public class BubbleAligner {

    private List<Node> bubbleCollection;
    private List<Node> visited = new ArrayList<>();
    private List<Node> mainBranch = new ArrayList<>();
    private Map<Node, Integer> otherNodes = new HashMap<>();

    /**
     * Create instance of bubbel aligner to align the position the node in the graph.
     * @param bubbleCollection collection of collapsed bubbles in the graph.
     */
    public BubbleAligner(Set<Node> bubbleCollection) {
        this.bubbleCollection = new ArrayList<>(bubbleCollection);
    }

    private int count = 0;
    /**
     * Smooth the bubbles by minimizing the variance in the y coordinate of the bubbles.
     * @return collection with bubbles with modified posititons.
     */
    public Collection<Node> align() {
        sortByX(bubbleCollection);
        findMainBranch(bubbleCollection);
        alignMainBranch();
        alignOtherNodes();
        bubbleCollection.forEach(this::setNewY);
        return bubbleCollection;
    }

    private void sortByX(Collection<Node> nodes) {
        Comparator<Node> comparatorByX = (n1, n2) -> Integer.compare(
                n1.getX(), n2.getX());
        nodes.stream().sorted(comparatorByX).collect(Collectors.toList());
    }

    private void setNewY(Node node) {
        if (node.getLinks().size() == 1) {
            count++;
            Node next = node.getLinks().iterator().next();
            if (!visited.contains(next)) {
                next.setXY(next.getX(), node.getY());
                visited.add(next);
            }
        }
        else if (node.getLinks().size() > 1) {
            count++;
            List<Node> links = new ArrayList<>(node.getLinks());
            links.retainAll(mainBranch);
            if (links.size() > 1)
                throw new RuntimeException();
            Node main = links.get(0);
            for (Node next : node.getLinks()) {
                if (!visited.contains(next) && next.getId() != main.getId()) {
                    next.setXY(next.getX(), next.getY() + node.getY() - main.getY());
                    visited.add(next);
                }
            }
            if (!visited.contains(main)) {
                main.setXY(main.getX(), node.getY());
                visited.add(main);
            }
        }
    }

    private void findMainBranch(Collection<Node> nodes) {
        List<Node> startNodes = nodes.stream()
                    .filter(x -> x.getBackLinks().size() == 0).collect(Collectors.toList());
        findMainNode(startNodes);
    }

    private void findMainNode(Collection<Node> nodes) {
        if (!nodes.isEmpty()) {
            Node main = nodes.stream()
                    .max(Comparator.comparing(x -> x.getGenomes().size())).get();
            mainBranch.add(main);
            for (Node n : nodes) {
                if (n.getId() != main.getId()) {
                    otherNodes.put(n, main.getY() - n.getY());
                }
            }
            findMainNode(main.getLinks());
        }
    }

    private void alignMainBranch() {
        int y = mainBranch.get(0).getY();
        mainBranch.forEach(n -> {
            n.setXY(n.getX(), y);
            visited.add(n);
        });
    }

    private void alignOtherNodes() {
        int y = mainBranch.get(0).getY();
        for (Map.Entry<Node, Integer> entry : otherNodes.entrySet()) {
            Node next = entry.getKey();
            next.setXY(next.getX(), y - entry.getValue());
            visited.add(next);
        }
    }
}
