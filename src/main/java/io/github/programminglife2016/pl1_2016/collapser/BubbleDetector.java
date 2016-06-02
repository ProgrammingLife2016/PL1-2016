package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.util.*;

/**
 * Detect all bubbles in the given graph, inclusive nested bubbles using top-bottom method
 * @author Ravi Autar.
 */
public class BubbleDetector {
    private static final int NOT_A_BUBBLE = 0;
    private static final int BUBBLE_DETECTED = 1;
    private static final int FOUND_MORE_GENOMES = 2;
    private static final int REACHED_FINAL_DESTINATION = 3;
    private static final int NO_CHILDREN_FOUND = 4;
    private boolean[] visited;
    private NodeCollection collection;
    private List<Node> bubbleBoundaries;
    private int lastId;
    private int reachedLevel = 1;

    public BubbleDetector(NodeCollection collection) {
        this.visited = new boolean[collection.size() + 1];
        this.lastId = collection.size() + 1;
        initVisited(collection);
        this.collection = collection;
        this.bubbleBoundaries = new ArrayList<>();
    }

    public void findMultiLevelBubbles() {
        System.out.println("Detecting...");
        Map<Integer, List<Node>> levelBubbles = new HashMap<>();
        Node destination = collection.get(collection.size());
        levelBubbles.put(1, findLevelBubbles(this.collection.get(1), destination));
        this.reachedLevel++;
        while (true){
            List<Node> currLevelList = findLevelCollectionBubbles(levelBubbles.get(reachedLevel - 1));
            if (currLevelList.size() == 0) { break; }
            levelBubbles.put(reachedLevel, currLevelList);
            reachedLevel++;
        }
        levelBubbles.values().forEach(this.bubbleBoundaries :: addAll);
    }

    public List<Node> findLevelCollectionBubbles(List<Node> bubbles) {
        initVisited(collection);
        Set<Node> repeat = new HashSet<>();
        List<Node> currLevelList = new ArrayList<>();
        for (Node bubble : bubbles)  {
            if (repeat.contains(bubble.getStartNode()) || (bubble.getStartNode().getLinks().size() == 1)
                    || (bubble.getStartNode() == bubble.getEndNode())) {
                continue;
            }
            else {
                repeat.add(bubble.getStartNode());
            }
            for (Node node : bubble.getStartNode().getLinks()) {
                List<Node> newBubbles = findLevelBubbles(node, bubble.getEndNode());
                currLevelList.addAll(newBubbles);
            }
        }
        return currLevelList;
    }

    public List<Node> findLevelBubbles(Node startNode, Node destination) {
        if (startNode == destination) {
            return new ArrayList<>();
        }
        List<Node> levelCollection = new ArrayList<>();
        Map.Entry<Integer, Node> stoppedAtNode = searchBubble(startNode, startNode.getGenomes(), destination);
        int status = stoppedAtNode.getKey();
        Node stoppedNode = stoppedAtNode.getValue();
        while (status != REACHED_FINAL_DESTINATION) {
            if (startNode==stoppedNode) {
                return new ArrayList<>();
            }
            switch (status) {
                case BUBBLE_DETECTED :
                    handleDetectedBubble(startNode, stoppedAtNode.getValue(), levelCollection);
                    startNode = stoppedAtNode.getValue();
                    stoppedAtNode = searchBubble(startNode, startNode.getGenomes(), destination);
                    break;
                case FOUND_MORE_GENOMES :
                    startNode = stoppedAtNode.getValue();
                    stoppedAtNode = searchBubble(startNode, startNode.getGenomes(), destination);
                    break;
            }
            if (status == NO_CHILDREN_FOUND) {
                if (startNode.getGenomes().equals(stoppedNode.getGenomes())) {
                    handleDetectedBubble(startNode, stoppedNode, levelCollection);
                } else {
                    for (Node childNode : startNode.getLinks()) {
                        initVisited(collection);
                        levelCollection.addAll(findLevelBubbles(childNode, destination));
                    }
                }
                break;
            }
            status = stoppedAtNode.getKey();
            stoppedNode = stoppedAtNode.getValue();
        }
        if (startNode.getGenomes().equals(stoppedNode.getGenomes())) {
            handleDetectedBubble(startNode, stoppedAtNode.getValue(), levelCollection);
        }
        else {
            for (Node childNode : startNode.getLinks()) {
                levelCollection.addAll(findLevelBubbles(childNode, stoppedNode));
            }
        }
        return levelCollection;
    }

    public Map.Entry<Integer, Node> searchBubble(Node curr, Collection genomes, Node destination) {
        visited[curr.getId()] = true;
        if (curr.getId() > destination.getId()) {
            return new AbstractMap.SimpleEntry<>(REACHED_FINAL_DESTINATION, curr);
        }
        List<Node> connectedTo = new ArrayList<>(curr.getLinks());
        for (Node child : connectedTo) {
            int status = checkGenomeMatch(genomes, child, destination);
            if (status != NOT_A_BUBBLE) {
                return new AbstractMap.SimpleEntry<>(status, child);
            }
            if (visited[child.getId()] == false) {
                return searchBubble(child, genomes, destination);
            }
        }
        return new AbstractMap.SimpleEntry<>(NO_CHILDREN_FOUND, curr);
    }

    private int checkGenomeMatch(Collection initGenomes, Node secondNode, Node destination) {
        if(secondNode.equals(destination)) {
            return REACHED_FINAL_DESTINATION;
        }
        if (initGenomes.equals(secondNode.getGenomes())) {
            return BUBBLE_DETECTED;
        }
        else if (isLargerSet(initGenomes, secondNode.getGenomes())){
            return FOUND_MORE_GENOMES;
        }
        else {
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

    public List<Node> getBubbleBoundaries() {
        List<Node> retrieved = new ArrayList<>();
        Set<Map.Entry<Integer, Integer>> uniques = new HashSet<>();
        for (Node bubble : bubbleBoundaries) {
            Map.Entry<Integer, Integer> entry = new AbstractMap.SimpleEntry<>(bubble.getStartNode().getId(), bubble.getEndNode().getId());
            if (uniques.contains(entry)) {
                continue;
            }
            else {
                retrieved.add(bubble);
                uniques.add(entry);
            }
        }
        return retrieved;
    }
}
