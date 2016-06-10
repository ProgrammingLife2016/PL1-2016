package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Linker class creates links between the bubbles of the same level,
 * and sets level to the bubbles if it is undefined.
 * @author Kamran Tadzjibov
 */
public class BubbleLinker {

    private ForkJoinPool forkJoinPool = new ForkJoinPool(8);
    private List<Node> bubbles;
//    private int lastId;
    private final AtomicLong lowestLevel = new AtomicLong(1);
    private final AtomicLong lastId = new AtomicLong();
//    private int bubblesListSize;
//    private int lowestLevel = 1;
    private static final double TIME = 1000000000d;
    /**
     *
     * @param bubbles list of all collapsedSegments bubbles
     */
    public BubbleLinker(List<Node> bubbles) {
        this.bubbles = Collections.synchronizedList(bubbles);
//        lastId =
                lastId.set(bubbles
                .stream()
                .max((b1, b2) ->
                        Integer.compare(b1.getId(),
                                b2.getId())).get()
                .getId());
//        bubblesListSize = bubbles.size();

        System.out.println("Started linking....");
        long startTime = System.nanoTime();

//        setCorrectLevelsToNodes();
        try {
            forkJoinPool.submit(() -> {setCorrectLevelsToNodes();}
//                    syncList.parallelStream().forEach((rating) -> {
//                        rating.setRating(predict(rating, itemBased));
//                    })
            ).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        System.out.println("Linking time: " + ((endTime - startTime) / TIME) + " s.");
    }

    private void setCorrectLevelsToNodes() {
        setLevels();
//        lowestLevel = bubbles
        lowestLevel.set(bubbles
                .parallelStream()
                .filter(x -> !x.getStartNode().isBubble())
                .max((b1, b2) ->
                        Integer.compare(b1.getStartNode().getZoomLevel(),
                                b2.getStartNode().getZoomLevel()))
                .get()
                .getStartNode()
                .getZoomLevel());
        while (needLowerLevels()) {
            lowerSegments();
        }
        addLinks();
        System.out.println("Lowest bubble level: " + lowestLevel);
        for (int i = 0; i < bubbles.size(); i++) {
            if (bubbles.get(i).getZoomLevel() == -1) {
                throw new RuntimeException("Not single neither nested bubble with zoom level = "
                        + bubbles.get(i).getZoomLevel()
                        + ": "
                        + bubbles.get(i));
            }
        }
    }

    private void setLevels() {
        List<Node> highestLevel = bubbles
                .parallelStream()
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
            if (lowestLevel.intValue() < n.getZoomLevel()) {
                lowestLevel.set(n.getZoomLevel());
            }
            if (n.isBubble()) {
                setLowerLevels(n);
            }
        }
    }

    private boolean needLowerLevels() {
//        lowestLevel = bubbles
        lowestLevel.set(bubbles
                .parallelStream()
                .filter(x -> !x.getStartNode().isBubble())
                .max((b1, b2) ->
                        Integer.compare(b1.getStartNode().getZoomLevel(),
                                b2.getStartNode().getZoomLevel()))
                .get().getStartNode().getZoomLevel());
        return bubbles.parallelStream().filter(x ->
                (!x.getStartNode().isBubble() && x.getStartNode().getZoomLevel() < lowestLevel.intValue())
                || (!x.getEndNode().isBubble() && x.getEndNode().getZoomLevel() < lowestLevel.intValue())
                || x.getContainer()
                        .parallelStream()
                        .filter(y -> !y.isBubble() && y.getZoomLevel() < lowestLevel.intValue())
                        .count() > 0)
                .count() > 0;
    }

    /**
     * Connect all bubbles per level with each other to get representative graph.
     */
    public void addLinks() {
        int tempLevel = lowestLevel.intValue() - 1;
        while (tempLevel > 0) {
            final int currLevel = tempLevel;
            List<Node> level = bubbles
                    .parallelStream()
                    .filter(x -> x.getZoomLevel() == currLevel)
                    .collect(Collectors.toList());
            int l = level.size();
            for (int i = 0; i < l; i++) {
                addLinkToBubble(level.get(i));
            }
            List<Node> unlinked = bubbles
                    .parallelStream()
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
                    .parallelStream()
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
        List<Node> needLower = bubbles.parallelStream()
                .filter(x -> (!x.getStartNode().isBubble()
                        && x.getStartNode().getZoomLevel() < lowestLevel.intValue())
                        || (!x.getEndNode().isBubble()
                        && x.getEndNode().getZoomLevel() < lowestLevel.intValue())
                        || x.getContainer().parallelStream().filter(y -> !y.isBubble()
                                && y.getZoomLevel() < lowestLevel.intValue()).count() > 0)
                .collect(Collectors.toList());
        while (needLower.size() != 0) {
            System.out.print("\rPlacing " + needLower.size() +" bubbles to lower level, lowestLevel = " + lowestLevel);
            for (Node b : needLower) {
                lowerSegmentInBubble(b);
            }
            needLower = bubbles
                    .parallelStream()
                    .filter(x -> !x.getStartNode().isBubble()
                            && x.getStartNode().getZoomLevel() < lowestLevel.intValue())
                    .collect(Collectors.toList());
        }
        System.out.println();
    }

    private void lowerSegmentInBubble(Node bubble) {
        int segLevel =  bubble.getZoomLevel() + 1;
        if (!bubble.getStartNode().isBubble()
                && bubble.getStartNode().getZoomLevel() < lowestLevel.intValue()) {
            bubble.setStartNode(replaceNode(bubble.getStartNode(), segLevel));
        }
        if (!bubble.getEndNode().isBubble()
                && bubble.getEndNode().getZoomLevel() < lowestLevel.intValue()) {
            bubble.setEndNode(replaceNode(bubble.getEndNode(), segLevel));
        }
        Set<Node> newContainer = new HashSet<>();
        for (Node n : bubble.getContainer()) {
            if (!n.isBubble() && n.getZoomLevel() < lowestLevel.intValue()) {
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
        Optional<Node> exist = bubbles.parallelStream()
                .filter(x -> x.getStartNode().getId() == node.getStartNode().getId()
                && x.getEndNode().getId() == node.getEndNode().getId()
                && x.getZoomLevel() == level).findFirst();
        if (exist.isPresent()) {
            return (Bubble) exist.get();
        }
        if (lowestLevel.intValue() < node.getZoomLevel()) {
            lowestLevel.set(node.getZoomLevel());
        }
        lastId.incrementAndGet(); //++;
        Bubble newBubble = new Bubble(lastId.intValue(), level, (Segment) node);
        bubbles.add(newBubble);
//        bubblesListSize++;
        return newBubble;
    }

    private Node replaceNode(Node node, int level) {
        Bubble newBubble = initNewBubble(node, level);
        node.setZoomLevel(level + 1);
        if (newBubble.getZoomLevel() > lowestLevel.intValue()) {
            lowestLevel.set(newBubble.getZoomLevel());
        }
        return newBubble;
    }
}