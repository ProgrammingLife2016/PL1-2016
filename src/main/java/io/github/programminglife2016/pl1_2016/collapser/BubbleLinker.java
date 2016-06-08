package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Linker class creates links between the bubbles of the same level,
 * and sets level to the bubbles if it is undefined.
 * @author Kamran Tadzjibov
 */
public class BubbleLinker {
    private List<Node> bubbles;
    private int lastId;
    private int bubblesListSize;
    private int lowestLevel = 1;
    private static final double TIME = 1000000000d;
    /**
     *
     * @param bubbles list of all collapsedSegments bubbles
     */
    public BubbleLinker(List<Node> bubbles) {
        this.bubbles = bubbles;
        lastId = bubbles
                .stream()
                .max((b1, b2) ->
                        Integer.compare(b1.getId(),
                                b2.getId())).get()
                .getId();
        bubblesListSize = bubbles.size();

        System.out.println("Started linking.");
        long startTime = System.nanoTime();
        setCorrectLevelsToNodes();
        long endTime = System.nanoTime();
        System.out.println("Linking time: " + ((endTime - startTime) / TIME) + " s.");
    }

    private void setCorrectLevelsToNodes() {
        setLevels();
        lowestLevel = bubbles
                .stream()
                .filter(x -> !x.getStartNode().isBubble())
                .max((b1, b2) ->
                        Integer.compare(b1.getStartNode().getZoomLevel(),
                                b2.getStartNode().getZoomLevel()))
                .get()
                .getStartNode()
                .getZoomLevel();
        while (needLowerLevels()) {
            lowerSegments();
        }
        addLinks();
        System.out.println("Lowest bubblelevel: " + lowestLevel);
        for (int i = 0; i < bubbles.size(); i++) {
            if (bubbles.get(i).getZoomLevel() == -1) {
                throw new RuntimeException("Not single neither nested bubble with zoomlevel = "
                        + bubbles.get(i).getZoomLevel()
                        + ": "
                        + bubbles.get(i));
            }
        }
    }

    private void setLevels() {
        List<Node> highestLevel = bubbles
                .stream()
                .filter(x -> x.getContainerId() == 0)
                .collect(Collectors.toList());
        for (Node bubble : highestLevel) {
            bubble.setZoomLevel(1);
            setLowerLevels(bubble);
        }
    }

    private void setLowerLevels(Node bubble) {
        Set<Node> children = new HashSet<>(bubble.getContainer());
        children.add(bubble.getStartNode());
        children.add(bubble.getEndNode());
        for (Node n : children) {
            n.setZoomLevel(bubble.getZoomLevel() + 1);
            if (lowestLevel < n.getZoomLevel()) {
                lowestLevel = n.getZoomLevel();
            }
            if (n.isBubble()) {
                setLowerLevels(n);
            }
        }
    }

    private boolean needLowerLevels() {
        lowestLevel = bubbles
                .stream()
                .filter(x -> !x.getStartNode().isBubble())
                .max((b1, b2) ->
                        Integer.compare(b1.getStartNode().getZoomLevel(),
                                b2.getStartNode().getZoomLevel()))
                .get().getStartNode().getZoomLevel();
        return bubbles.stream().filter(x ->
                (!x.getStartNode().isBubble() && x.getStartNode().getZoomLevel() < lowestLevel)
                || (!x.getEndNode().isBubble() && x.getEndNode().getZoomLevel() < lowestLevel)
                || x.getContainer()
                        .stream()
                        .filter(y -> !y.isBubble() && y.getZoomLevel() < lowestLevel)
                        .count() > 0)
                .count() > 0;
    }

    /**
     * Connect all bubbles per level with each other to get representative graph.
     */
    public void addLinks() {
        int tempLevel = lowestLevel - 1;
        while (tempLevel > 0) {
            final int currLevel = tempLevel;
            List<Node> level = bubbles
                    .stream()
                    .filter(x -> x.getZoomLevel() == currLevel)
                    .collect(Collectors.toList());
            int l = level.size();
            for (int i = 0; i < l; i++) {
                addLinkToBubble(level.get(i));
            }
            List<Node> unlinked = bubbles
                    .stream()
                    .filter(x -> x.getZoomLevel() == currLevel
                            && x.getLinks().isEmpty()
                            && !isEndNode(x))
                    .collect(Collectors.toList());
            if (unlinked.isEmpty()) {
                tempLevel -= 1;
            }
        }
    }

    /**
     * Add forward links to the given bubble.
     * @param bubble bubble to link with the rest of the graph
     */
    private void addLinkToBubble(Node bubble) {
        Node prospectiveLink;
        for (Node link : bubble.getEndNode().getLinks()) {
            prospectiveLink = getHighestBubble(bubble.getZoomLevel(), link);
            if (prospectiveLink != null) {
                bubble.getLinks().add(prospectiveLink);
            }
        }
    }

    private Node getHighestBubble(int level, Node prospectiveLink) {
        int cId = prospectiveLink.getContainerId();
        if (prospectiveLink.getZoomLevel() > level) {
            prospectiveLink = getHighestBubble(level, bubbles
                    .stream()
                    .filter(x -> x.getId() == cId)
                    .findFirst()
                    .get());
        }
        if (prospectiveLink != null
                && prospectiveLink.getZoomLevel() == level
                && prospectiveLink.isBubble()) {
            return prospectiveLink;
        }
        return null;
    }

    private boolean isEndNode(Node node) {
        if (!node.isBubble()) {
            return node.getLinks().isEmpty();
        }
        return isEndNode(node.getEndNode());
    }

    private void lowerSegments() {
        List<Node> needLower = bubbles.stream()
                .filter(x -> (!x.getStartNode().isBubble()
                        && x.getStartNode().getZoomLevel() < lowestLevel)
                        || (!x.getEndNode().isBubble()
                        && x.getEndNode().getZoomLevel() < lowestLevel)
                        || x.getContainer().stream().filter(y -> !y.isBubble()
                                && y.getZoomLevel() < lowestLevel).count() > 0)
                .collect(Collectors.toList());
        while (needLower.size() != 0) {
            for (Node b : needLower) {
                lowerSegmentInBubble(b);
            }
            needLower = bubbles
                    .stream()
                    .filter(x -> !x.getStartNode().isBubble()
                            && x.getStartNode().getZoomLevel() < lowestLevel)
                    .collect(Collectors.toList());
        }
    }

    private void lowerSegmentInBubble(Node bubble) {
        int segLevel =  bubble.getZoomLevel() + 1;
        if (!bubble.getStartNode().isBubble()
                && bubble.getStartNode().getZoomLevel() < lowestLevel) {
            bubble.setStartNode(replaceNode(bubble.getStartNode(), segLevel));
        }
        if (!bubble.getEndNode().isBubble()
                && bubble.getEndNode().getZoomLevel() < lowestLevel) {
            bubble.setEndNode(replaceNode(bubble.getEndNode(), segLevel));
        }
        Set<Node> newContainer = new HashSet<>();
        for (Node n : bubble.getContainer()) {
            if (!n.isBubble() && n.getZoomLevel() < lowestLevel) {
                newContainer.add(replaceNode(n, segLevel));
            }
            else {
                newContainer.add(n);
            }
        }
        bubble.getContainer().clear();
        bubble.getContainer().addAll(newContainer);
    }

    private Bubble initNewBubble(Node node, int level) {
        Optional<Node> exist = bubbles.stream()
                .filter(x -> x.getStartNode().getId() == node.getStartNode().getId()
                && x.getEndNode().getId() == node.getEndNode().getId()
                && x.getZoomLevel() == level).findFirst();
        if (exist.isPresent()) {
            return (Bubble) exist.get();
        }
        if (lowestLevel < node.getZoomLevel()) {
            lowestLevel = node.getZoomLevel();
        }
        lastId++;
        Bubble newBubble = new Bubble(lastId, level, (Segment) node);
        bubbles.add(newBubble);
        bubblesListSize++;
        return newBubble;
    }

    private Node replaceNode(Node node, int level) {
        Bubble newBubble = initNewBubble(node, level);
        node.setZoomLevel(level + 1);
        if (newBubble.getZoomLevel() > lowestLevel) {
            lowestLevel = newBubble.getZoomLevel();
        }
        return newBubble;
    }
}