package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Linker class creates links between the bubbles of the same level,
 * and sets level to the bubbles if it is undefined.
 * @author Kamran Tadzjibov
 */
public class BubbleLinker {

    private static final int THREADS = 8;
    private ForkJoinPool forkJoinPool = new ForkJoinPool(THREADS);
    private List<Node> bubbles;
    private final AtomicLong lowestLevel = new AtomicLong(1);
    private final AtomicLong lastId = new AtomicLong();
    private Map<String, Node> quickReference;
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
    }

    private void createQuickRefForLowering() {
//        System.out.println("Started creating quick reference for lowering....");
        long startTime = System.nanoTime();
        quickReference = Collections.synchronizedMap(new HashMap<>(bubbles.size()));
        for (Node n : bubbles) {
            quickReference.put(getNodeKeyForLowering(n), n);
        }
        long endTime = System.nanoTime();
//        System.out.println("Done. time: " + ((endTime - startTime) / TIME) + " s.");
    }

    private void createQuickRefForLinking() {
//        System.out.println("Started creating quick reference for linking....");
        long startTime = System.nanoTime();
        quickReference = Collections.synchronizedMap(new HashMap<>(bubbles.size()));
        for (Node n : bubbles) {
            quickReference.put(String.valueOf(n.getId()), n);
        }
        long endTime = System.nanoTime();
//        System.out.println("Done. time: " + ((endTime - startTime) / TIME) + " s.");
    }

    /**
     * Run multithreaded linking.
     */
    public void run() {
        try {
            forkJoinPool.submit(() -> { setCorrectLevelsToNodes(); }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCorrectLevelsToNodes() {
        setLevels();
        createQuickRefForLowering();
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
//        System.out.println("Finished lower segments.");
//
//        System.out.println("Started linking....");
        long startTime = System.nanoTime();
        addLinks();
        long endTime = System.nanoTime();
//        System.out.println("Linking time: " + ((endTime - startTime) / TIME) + " s.");
        quickReference = null;
//        System.out.println("Lowest bubble level: " + lowestLevel);
        for (Node bubble : bubbles) {
            if (bubble.getZoomLevel() == -1) {
                throw new RuntimeException("Not single neither nested bubble with zoom level = "
                        + bubble.getZoomLevel()
                        + ": "
                        + bubble);
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
        lowestLevel.set(bubbles
                .parallelStream()
                .filter(x -> !x.getStartNode().isBubble())
                .max((b1, b2) ->
                        Integer.compare(b1.getStartNode().getZoomLevel(),
                                b2.getStartNode().getZoomLevel()))
                .get().getStartNode().getZoomLevel());
        return getNeedLower().count() > 0;
    }

    /**
     * Connect all bubbles per level with each other to get representative graph.
     */
    private void addLinks() {
        createQuickRefForLinking();
        int tempLevel = lowestLevel.intValue() - 1;
        while (tempLevel > 0) {
            final int currLevel = tempLevel;
            List<Node> level = bubbles
                    .parallelStream()
                    .filter(x -> x.getZoomLevel() == currLevel)
                    .collect(Collectors.toList());
            level.parallelStream().forEach(this::addLinkToBubble);
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
        if (prospectiveLink.getZoomLevel() > level
                && quickReference.containsKey(String.valueOf(cId))) {
            prospectiveLink = getHighestBubble(level, quickReference.get(String.valueOf(cId)));
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
        List<Node> needLower = getNeedLower().collect(Collectors.toList());
        while (needLower.size() != 0) {
            System.out.println("\rPlacing " + needLower.size()
                    + " bubbles to lower level, lowestLevel.ser = " + lowestLevel + " ");
            long startTime = System.nanoTime();
//            needLower.forEach(this::lowerSegmentInBubble);
            for (int i = 0; i < needLower.size(); i++) {
                lowerSegmentInBubble(needLower.get(i));
//                System.out.print("\rPlaced: " + i);
            }
            long endTime = System.nanoTime();
//            System.out.println("Lowered segments in: " + ((endTime - startTime) / TIME) + " s.");
            needLower = getNeedLower().collect(Collectors.toList());
        }
        System.out.println("");
    }

    private Stream<Node> getNeedLower() {
        return bubbles
                .parallelStream()
                .filter(x -> (!x.getStartNode().isBubble()
                        && x.getStartNode().getZoomLevel() < lowestLevel.intValue())
                        || (!x.getEndNode().isBubble()
                        && x.getEndNode().getZoomLevel() < lowestLevel.intValue())
                        || x.getContainer().parallelStream().filter(y -> !y.isBubble()
                        && y.getZoomLevel() < lowestLevel.intValue()).count() > 0);
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
        Set<Node> newContainer = Collections.synchronizedSet(new HashSet<>());
        List<Node> oldContainer = Collections.synchronizedList(bubble.getContainer());

        oldContainer.parallelStream().forEach(n -> //
        {
            if (!n.isBubble() && n.getZoomLevel() < lowestLevel.intValue()) {
                newContainer.add(replaceNode(n, segLevel));
            }
            else {
                newContainer.add(n);
            }
        });
        bubble.getContainer().clear();
        bubble.getContainer().addAll(newContainer);
    }

    private synchronized Bubble initNewBubble(Node node, int level) {
        String key = node.getStartNode().getId() + "_"
                + node.getEndNode().getId() + "_"
                + level;
        if (quickReference.containsKey(key)) {
            return (Bubble) quickReference.get(key);
        }

        if (lowestLevel.intValue() < node.getZoomLevel()) {
            lowestLevel.set(node.getZoomLevel());
        }
        lastId.incrementAndGet();
        Bubble newBubble = new Bubble(lastId.intValue(), level, (Segment) node);
        bubbles.add(newBubble);
        quickReference.put(getNodeKeyForLowering(newBubble), newBubble);
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

    private String getNodeKeyForLowering(Node node) {
        return node.getStartNode().getId() + "_"
                + node.getEndNode().getId() + "_"
                + node.getZoomLevel();
    }

    /**
     * Get the lowest bubble level
     * @return lowest bubble level
     */
    public int getLowestLevel() {
        return lowestLevel.intValue();
    }
}