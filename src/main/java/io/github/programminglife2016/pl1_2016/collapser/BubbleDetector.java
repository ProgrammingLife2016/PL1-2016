package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Detect all bubbles in the given graph, inclusive nested bubbles using top-bottom method
 * @author Ravi Autar
 */
public class BubbleDetector {
    private static final int NOT_A_BUBBLE = 0;
    private static final int BUBBLE_DETECTED = 1;
    private static final int FOUND_MORE_GENOMES = 2;
    private static final int REACHED_FINAL_DESTINATION = 3;
    private static final int NO_CHILDREN_FOUND = 4;
    private Set<SimplePair<Integer, Integer>> conductedSearches;
    private NodeCollection collection;
    private List<Node> bubbleBoundaries;
    private int lastId;
    private int reachedLevel = 1;

    /**
     * Initialize bubble detector.
     * @param collection collection of segments
     */
    public BubbleDetector(NodeCollection collection) {
        this.lastId = collection.size() + 1;
        initVisited(collection);
        this.collection = collection;
        this.bubbleBoundaries = new ArrayList<>();
        this.conductedSearches = new HashSet<>();
    }

    /**
     * Method search for all possible bubbles and adds them to the bubbleBoundaries.
     */
    public void findMultiLevelBubbles() {
        System.out.println("Starting detector....");
        Map<Integer, List<Node>> levelBubbles = new HashMap<>();
        levelBubbles.put(1,
                findLevelBubbles(this.collection.get(1), collection.get(collection.size())));
        this.reachedLevel++;

        List<Node> currLevelList = findDeeperLevelBubbles(levelBubbles);
        while (!currLevelList.isEmpty()) {
            this.conductedSearches = new HashSet<>();
            levelBubbles.put(reachedLevel, currLevelList);
            reachedLevel++;
            currLevelList = findDeeperLevelBubbles(levelBubbles);
        }
        levelBubbles.values().forEach(this.bubbleBoundaries::addAll);
    }


    private List<Node> findLevelBubbles(Node startNode, Node destination) {
        conductedSearches.add(new SimplePair<>(startNode.getId(), destination.getId()));
        if (startNode.getId() >= destination.getId()) {
            return new ArrayList<>();
        }
        boolean[] visited = initVisited(this.collection);
        List<Node> levelCollection = new ArrayList<>();
        Map.Entry<Integer, Node> stoppedAtNode =
                searchBubble(startNode, startNode.getGenomes(), destination, visited);
        int status = stoppedAtNode.getKey();
        Node stoppedNode = stoppedAtNode.getValue();
        loop : while (status != REACHED_FINAL_DESTINATION) {
            if (startNode.getId() == stoppedNode.getId()
                    || stoppedNode.getId() > destination.getId()) {
                return new ArrayList<>();
            }
            switch (status) {
                case BUBBLE_DETECTED:
                    handleDetectedBubble(startNode, stoppedAtNode.getValue(), levelCollection);
                    startNode = stoppedAtNode.getValue();
                    stoppedAtNode = searchBubble(startNode,
                            startNode.getGenomes(), destination, visited);
                    break;
                case FOUND_MORE_GENOMES:
                    startNode = stoppedAtNode.getValue();
                    stoppedAtNode = searchBubble(startNode,
                            startNode.getGenomes(), destination, visited);
                    break;
                case NO_CHILDREN_FOUND:
                    checkIfStoppedNodeIsABubble(startNode, destination,
                            levelCollection, stoppedNode);
                    break loop;
                default: break;
            }
            status = stoppedAtNode.getKey();
            stoppedNode = stoppedAtNode.getValue();
        }
        checkIfStoppedNodeIsABubble(startNode, destination, levelCollection, stoppedNode);
        return levelCollection;
    }

    private List<Node> findDeeperLevelBubbles(Map<Integer, List<Node>> levelBubbles) {
        List<Node> currLevelList = new ArrayList<>();
        for (Node bubble : levelBubbles.get(reachedLevel - 1)) {
            if (bubble.getStartNode().getId() >= bubble.getEndNode().getId()) {
                continue;
            }
            for (Node node : bubble.getStartNode().getLinks()) {
                List<Node> newBubbles = findLevelBubbles(node, bubble.getEndNode());
                currLevelList.addAll(newBubbles);
            }
        }
        return currLevelList;
    }

    private void checkIfStoppedNodeIsABubble(Node startNode,
                                             Node destination,
                                             List<Node> levelCollection,
                                             Node stoppedNode) {
        if (startNode.getGenomes().equals(stoppedNode.getGenomes())) {
            handleDetectedBubble(startNode, stoppedNode, levelCollection);
        } else {
            continueLoopingThroughInnerBubbles(startNode, destination, levelCollection);
        }
    }

    private void continueLoopingThroughInnerBubbles(Node startNode,
                                                    Node destination,
                                                    List<Node> levelCollection) {
        for (Node childNode : startNode.getLinks()) {
            if (!this.conductedSearches
                    .contains(new SimplePair<>(childNode.getId(), destination.getId()))) {
                levelCollection.addAll(findLevelBubbles(childNode, destination));
            }
        }
    }

    private Map.Entry<Integer, Node> searchBubble(Node curr,
                                                  Collection<String> genomes,
                                                  Node destination,
                                                  boolean[] visited) {
        visited[curr.getId()] = true;
        List<Node> connectedTo = new ArrayList<>(curr.getLinks());
        for (Node child : connectedTo) {
            int status = checkGenomeMatch(genomes, child, destination);
            if (status != NOT_A_BUBBLE) {
                return new SimpleEntry<>(status, child);
            }
            if (!visited[child.getId()]) {
                return searchBubble(child, genomes, destination, visited);
            }
        }
        return new SimpleEntry<>(NO_CHILDREN_FOUND, curr);
    }

    private int checkGenomeMatch(Collection<String> initGenomes,
                                 Node secondNode, Node destination) {
        if (secondNode.equals(destination)) {
            return REACHED_FINAL_DESTINATION;
        }
        if (initGenomes.equals(secondNode.getGenomes())) {
            return BUBBLE_DETECTED;
        }
        else if (isLargerSet(initGenomes, secondNode.getGenomes())) {
            return FOUND_MORE_GENOMES;
        }
        else {
            return NOT_A_BUBBLE;
        }
    }

    private boolean isLargerSet(Collection<String> genomes, Set<String> secondSet) {
        if (secondSet.size() < genomes.size()) {
            return false;
        }
        HashSet<String> intersection = new HashSet<>(genomes);
        intersection.retainAll(new HashSet<>(secondSet));
        return intersection.equals(genomes);
    }

    private void handleDetectedBubble(Node starting, Node value, List<Node> levelCollection) {
        Node found = new Bubble(lastId, starting, value);
        found.getStartNode().setContainerId(found.getId());
        found.getEndNode().setContainerId(found.getId());
        found.setZoomLevel(reachedLevel);
        levelCollection.add(found);
        lastId++;
    }

    private boolean[] initVisited(NodeCollection collection) {
        boolean[] visited = new boolean[collection.size() + 1];
        for (Node node : collection.values()) {
            visited[node.getId()] = false;
        }
        return visited;
    }

    /**
     * Get bubbles with unique pair of start and end node.
     * @return list of unique bubbles
     */
    public List<Node> getBubbleBoundaries() {
        List<Node> retrieved = new ArrayList<>();
        Set<Map.Entry<Integer, Integer>> uniques = new HashSet<>();
        for (Node bubble : bubbleBoundaries) {
            SimpleEntry<Integer, Integer> entry =
                    new SimpleEntry<>(bubble.getStartNode().getId(), bubble.getEndNode().getId());
            if (!uniques.contains(entry)) {
                retrieved.add(bubble);
                uniques.add(entry);
            }
        }
        return retrieved;
    }
}