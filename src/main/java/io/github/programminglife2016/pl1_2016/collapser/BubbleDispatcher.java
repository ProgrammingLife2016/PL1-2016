package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Takes apart collapsed layered bubbles and
 * uncollapses most important parts depending on threshold value.
 * @author Kamran Tadzjibov
 */
public class BubbleDispatcher {

    private List<Node> bubbleCollection;
    private int lastId;
    private int bubblesListSize;
    private int lowestLevel;
    private static final double TIME = 1000000000d;
    private static final int THREADS = 8;
    private ForkJoinPool forkJoinPool = new ForkJoinPool(THREADS);

    private Map<String, Node> quickReference;

    /**
     * Initialize bubbles by running bubble collapser.
     * @param collection of nodes
     */
    public BubbleDispatcher(NodeCollection collection) {
        BubbleCollapser collapser = new BubbleCollapser(collection);
        collapser.collapseBubbles();
        this.bubbleCollection = collapser.getBubbles();
        bubblesListSize = bubbleCollection.size();
        lowestLevel = collapser.getLowestLevel();
        initDispatcher();
        lastId = bubbleCollection.stream().max((b1, b2) ->
                Integer.compare(b1.getId(), b2.getId())).get().getId();
    }

    /**
     * Initialize dispatcher by calculating real container size.
     */
    private void initDispatcher() {
        for (Node bubble : bubbleCollection) {
            bubble.setContainerSize(getBubbleSize(bubble));
        }
    }

    /**
     * Calculates real bubble size.
     * Only segments are counted to calculate size.
     * @param bubble to get amount of segments that it contains
     * @return size of bubble
     */
    private int getBubbleSize(Node bubble) {
        if (bubble.getContainer().size() == 0) {
            return 1;
        }
        else {
            int size = 2;
            for (int i = 0; i < bubble.getContainer().size(); i++) {
                size += getBubbleSize(bubble.getContainer().get(i));
            }
            return size;
        }
    }

    /**
     * Get graph where each bubble contains not more segments than given threshold.
     * @param threshold value to filter the graphs bubbles
     * @return graph with thresholded bubbles
     */
    public NodeCollection getThresholdedBubbles(int threshold) {
        System.out.println("Started filtering....");
        long startTime = System.nanoTime();
        Set<Node> filtered = filterMultithreaded(threshold);
        long endTime = System.nanoTime();
        System.out.println("Done filtering. time: " + ((endTime - startTime) / TIME) + " s.");
        startTime = System.nanoTime();
        findNewLinks(filtered);
        endTime = System.nanoTime();
        System.out.println("Done relinking. time: " + ((endTime - startTime) / TIME) + " s.");
        return listAsNodeCollection(filtered);
    }

    /**
     * Run multithreaded bubble filtering based on threshold value.
     * @param threshold amount of segments per bubble which is used to filter bubbles
     * @return filtered set of bubbles
     */
    public Set<Node> filterMultithreaded(int threshold) {
        Set<Node> filtered = Collections.synchronizedSet(new HashSet<>());
        try {
            forkJoinPool.submit(() -> {
                filtered.addAll(filterBubbles(threshold));
                }
            ).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filtered;
    }

    /**
     * Get unlinked bubbles by given threshold.
     * @param threshold value to filter the graphs bubbles
     * @return set of wrong linked in current context bubbles which are filtered on threshold value
     */
    private Set<Node> filterBubbles(int threshold) {
        createQuickRefForFiltering();
        Set<Integer> containers = Collections.synchronizedSet(new HashSet<>()); //new ArrayList<>();
        Set<Node> filtered = Collections.synchronizedSet(new HashSet<>());
        for (int i = 1; i < lowestLevel; i++) {
            Set<Node> tempFiltered = getFilteredNodes(containers, i,
                    filtered, threshold);
            containers.addAll(tempFiltered.parallelStream().map(Node::getId)
                    .collect(Collectors.toList()));
            filtered.addAll(tempFiltered);
        }
        return filtered;
    }

    /**
     * Get all needed bubbles from current level by given threshold
     * @param containers list of bubble ids which are directly or
     *                   indirectly in the set of filtered bubbles
     * @param currentLevel current bubbles level
     * @param filtered set of already filtered nodes
     * @param threshold value to filter the graphs bubbles
     * @return filtered bubbles by threshold for given level
     */
    private Set<Node> getFilteredNodes(Set<Integer> containers, int currentLevel,
                                       Set<Node> filtered, int threshold) {
        Set<Node> tempFiltered = Collections.synchronizedSet(new HashSet<>());
        List<Node> currLevelBubbles = bubbleCollection.parallelStream()
                .filter(x -> x.getZoomLevel() == currentLevel).collect(Collectors.toList());
        currLevelBubbles.forEach(x -> {
            if (filtered.parallelStream().filter(b -> b.getEndNode().equals(x)).count() == 0
                    && x.getZoomLevel() == currentLevel
                    && !containers.contains(x.getContainerId())) {
            /* TODO collapse only one threshold level or everything under the threshold on choise
             * Make use of this code:
             * (x.getContainerSize() == threshold || x.getContainerSize() <= 1) {
             */
                if (x.getContainerSize() <= threshold) {
                    tempFiltered.removeAll(filtered.stream().filter(b -> b.getZoomLevel() == -1
                            && (b.getStartNode().getId() == x.getStartNode().getId()
                            || b.getEndNode().getId() == x.getEndNode().getId()))
                            .collect(Collectors.toList()));
                    tempFiltered.removeIf(b -> b.getZoomLevel() == -1
                            && (b.getStartNode().getId() == x.getStartNode().getId()
                            || b.getEndNode().getId() == x.getEndNode().getId()));
                    tempFiltered.add(x);
                }
                else if (!x.getStartNode().isBubble()) {
                    tempFiltered.addAll(replaceInside(x));
                }
            }
            if (containers.contains(x.getContainerId())) {
                containers.add(x.getId());
            }
        });
        return tempFiltered;
    }

    /**
     * Replace old links by the found links for each bubble in given collection.
     * @param filteredBubbles collection of by threshold filtered bubbles
     */
    private void findNewLinks(Collection<Node> filteredBubbles) {
        createQuickRefForRelinking();
        filteredBubbles.parallelStream().forEach(bubble -> {
            Node primitiveEnd = getPrimitiveEnd(bubble);
            Node prospectiveLink = getExistingAncestor(primitiveEnd,
                    filteredBubbles, (n1, n2) -> n1.getStartNode().equals(n2));
            bubble.getLinks().clear();
            if (prospectiveLink != null && !prospectiveLink.equals(bubble)) {
                bubble.getLinks().add(prospectiveLink);
            }
            else {
                for (Node primLink : primitiveEnd.getLinks()) {
                    Node found = getExistingAncestor(primLink,
                            filteredBubbles, (n1, n2) -> n1.getId() == n2.getContainerId());
                    if (found != null) {
                        bubble.getLinks().add(found);
                    }
                    else {
                        System.err.println("Verkeerd connected node: " + bubble);
                    }
                }
            }
        });
    }

    /**
     * Get simple leaf segment of the given end node.
     * @param node Bubble to get end segment
     * @return end segment of given bubble
     */
    private Node getPrimitiveEnd(Node node) {
        if (node.getEndNode().isBubble()) {
            return getPrimitiveEnd(node.getEndNode());
        }
        return node.getEndNode();
    }

    /**
     * Get highest existing ancestor which exist in filtered bubbles.
     * @param node to find its ancestor
     * @param filteredBubbles collection of by threshold filtered bubbles
     * @param equals equals function to filter the ancestors
     * @return highest existing ancestor which exist in filtered bubbles
     */
    private Node getExistingAncestor(Node node,
                                     Collection<Node> filteredBubbles,
                                     BiFunction<Node, Node, Boolean> equals) {
        if (filteredBubbles.contains(node)) {
            Optional<Node> parent = filteredBubbles
                    .parallelStream()
                    .filter(x -> equals.apply(x, node))
                    .findAny();
//                    .findFirst();
            if (parent.isPresent()) {
                return parent.get();
            }
            return node;
        }
        if (quickReference.containsKey(String.valueOf(node.getContainerId()))) {
            return getExistingAncestor(quickReference
                    .get(String.valueOf(node.getContainerId())), filteredBubbles, equals);
        }
        return null;
    }

    /**
     * Convert collection of nodes to NodeCollection
     * @param res collection of nodes
     * @return NodeCollection of given nodes
     */
    private NodeCollection listAsNodeCollection(Collection<Node> res) {
        NodeCollection collection = new NodeMap();
        for (Node node: res) {
            collection.put(node.getId(), node);
        }
        return collection;
    }

    private Set<Node> replaceInside(Node bubble) {
        Set<Node> newBubbles = new HashSet<>();
        newBubbles.add(initNewBubble(bubble.getStartNode()));
        newBubbles.add(initNewBubble(bubble.getEndNode()));
        for (Node n : bubble.getContainer()) {
            if (!n.isBubble()) {
                newBubbles.add(initNewBubble(n));
            }
            else {
                newBubbles.add(n);
            }
        }
        return newBubbles;
    }

    private Bubble initNewBubble(Node node) {
        String key = getNodeKeyForFiltering(node);
        if (quickReference.containsKey(key)) {
            return (Bubble) quickReference.get(key);
        }
        lastId++;
        Bubble newBubble = new Bubble(lastId, -1, (Segment) node);
        bubbleCollection.add(newBubble);
        quickReference.put(getNodeKeyForFiltering(newBubble), newBubble);
        bubblesListSize++;
        return newBubble;
    }


    private void createQuickRefForFiltering() {
        quickReference = Collections.synchronizedMap(new HashMap<>(bubbleCollection.size()));
        for (Node n : bubbleCollection) {
            quickReference.put(getNodeKeyForFiltering(n), n);
        }
    }

    private void createQuickRefForRelinking() {
        quickReference = Collections.synchronizedMap(new HashMap<>(bubbleCollection.size()));
        for (Node n : bubbleCollection) {
            quickReference.put(String.valueOf(n.getId()), n);
        }
    }

    private String getNodeKeyForFiltering(Node node) {
        return node.getStartNode().getId() + "_"
                + node.getEndNode().getId();
    }

}