package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Detect bubbles in a dataset.
 */
public class BubbleDetector {
    private static final int NOT_A_BUBBLE = 0;
    private static final int BUBBLE_DETECTED = 1;
    private static final int FOUND_MORE_GENOMES = 2;
    private static final int REACHED_FINAL_DESTINATION = 3;
    private boolean[] visited;
    private NodeCollection collection;
    private List<Node> bubbleBoundaries;
    private int lastId;
    private int reachedLevel = 1;

    /**
     * Construct a BubbleDetector using the original dataset.
     *
     * @param collection the original dataset
     */
    public BubbleDetector(NodeCollection collection) {
        this.visited = new boolean[collection.size() + 1];
        this.lastId = collection.size() + 1;
        initVisited(collection);
        this.collection = collection;
        this.bubbleBoundaries = new ArrayList<>();
    }

    /**
     * Detect the bubbles in the original dataset.
     */
    public void findMultiLevelBubbles() {
        Map<Integer, List<Node>> levelBubbles = new HashMap<>();
        Node destination = collection.get(collection.size());
        levelBubbles.put(1, findLevelBubbles(this.collection.get(1), destination));
        this.reachedLevel++;
        int lastlistsize = levelBubbles.size();
        while (lastlistsize != 0) {
            initVisited(collection);
            List<Node> currLevelList = new ArrayList<>();
            for (Node bubble : levelBubbles.get(reachedLevel - 1)) {
                if (bubble.getStartNode().getLinks().size() == 1
                        || bubble.getStartNode() == bubble.getEndNode()) {
                    continue;
                }
                for (Node node : bubble.getStartNode().getLinks()) {
                    List<Node> newBubbles = findLevelBubbles(node, bubble.getEndNode());
                    currLevelList.addAll(newBubbles);
                }
            }
            lastlistsize = currLevelList.size();
            levelBubbles.put(reachedLevel, currLevelList);
            reachedLevel++;
        }
        if (levelBubbles.size() > 1) {
            levelBubbles.remove(reachedLevel - 1);
        }
        this.bubbleBoundaries = new ArrayList<>(levelBubbles.get(1));
        for (int i = 2; i < levelBubbles.size() + 1; i++) {
            this.bubbleBoundaries.addAll(levelBubbles.get(i));
        }
    }

    /**
     * Find the bubbles from a certain start node to a certain end node.
     *
     * @param startNode start node
     * @param destination end node
     * @return the bubbles in that level
     */
    public List<Node> findLevelBubbles(Node startNode, Node destination) {
        if (startNode == destination) {
            return new ArrayList<>();
        }
        List<Node> levelCollection = new ArrayList<>();
        Map.Entry<Integer, Node> bubbleAt = searchBubble(startNode, startNode.getGenomes(),
                destination);
        while (bubbleAt.getKey() != REACHED_FINAL_DESTINATION) {
            switch (bubbleAt.getKey()) {
                case BUBBLE_DETECTED:
                    handleDetectedBubble(startNode, bubbleAt.getValue(), levelCollection);
                    startNode = bubbleAt.getValue();
                    bubbleAt = searchBubble(startNode, startNode.getGenomes(), destination);
                    break;
                case FOUND_MORE_GENOMES:
                    handleDetectedBubble(startNode, startNode, levelCollection);
                    startNode = bubbleAt.getValue();
                    bubbleAt = searchBubble(startNode, startNode.getGenomes(), destination);
                    break;
                default:
                    break;
            }
        }
        if (startNode.getGenomes().equals(bubbleAt.getValue().getGenomes())) {
            handleDetectedBubble(startNode, bubbleAt.getValue(), levelCollection);
        }
        return levelCollection;
    }

    /**
     * Search for the bubble, with curr as start node.
     *
     * @param curr start node
     * @param genomes the genomes to match with the end node.
     * @param destination the end node
     * @return s
     */
    public Map.Entry<Integer, Node> searchBubble(Node curr, Collection genomes, Node destination) {
        visited[curr.getId()] = true;
        List<Node> connectedTo = new ArrayList<>(curr.getLinks());
        for (Node child : connectedTo) {
            int status = checkGenomeMatch(genomes, child, destination);
            if (status != NOT_A_BUBBLE) {
                return new AbstractMap.SimpleEntry<>(status, child);
            }
            if (!visited[child.getId()]) {
                return searchBubble(child, genomes, destination);
            }
        }
        return new AbstractMap.SimpleEntry<>(REACHED_FINAL_DESTINATION, curr);
    }

    private int checkGenomeMatch(Collection initGenomes, Node secondNode, Node destination) {
        if (secondNode.equals(destination)) {
            return REACHED_FINAL_DESTINATION;
        }
        if (initGenomes.equals(secondNode.getGenomes())) {
            return BUBBLE_DETECTED;
        } else if (isLargerSet(initGenomes, secondNode.getGenomes())) {
            return FOUND_MORE_GENOMES;
        } else {
            return NOT_A_BUBBLE;
        }
    }

    private boolean isLargerSet(Collection genomes, Set<String> secondSet) {
        if (secondSet.size() < genomes.size()) {
            return false;
        }
        Collection<String> intersection = new HashSet<>(genomes);
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

    private void initVisited(NodeCollection collection) {
        for (Node node : collection.values()) {
            visited[node.getId()] = false;
        }
    }

    /**
     * Return the start and end nodes of all bubbles.
     *
     * @return the start and end nodes of all bubbles.
     */
    public List<Node> getBubbleBoundaries() {
        return bubbleBoundaries;
    }
}
