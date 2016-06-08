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
 * Detect all bubbles in the given graph, inclusive nested bubbles using top-bottom method
 * Created by ravishivam on 15-5-16.
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
        System.out.println("Starting detector....");
        Map<Integer, List<Node>> levelBubbles = new HashMap<>();
        levelBubbles.put(1, findLevelBubbles(this.collection.get(1), collection.get(collection.size() + 1)));
        this.reachedLevel++;
        while (true) {
            List<Node> currLevelList = findDeeperLevelBubbles(levelBubbles);
            if (currLevelList.isEmpty()) {
                break;
            }
            levelBubbles.put(reachedLevel,currLevelList);
            reachedLevel++;
        }
        levelBubbles.values().stream().forEach(this.bubbleBoundaries::addAll);
    }

    private List<Node> findDeeperLevelBubbles(Map<Integer, List<Node>> levelBubbles) {
        List<Node> currLevelList = new ArrayList<>();
        initVisited(collection);
        for (Node bubble : levelBubbles.get(reachedLevel - 1)) {
            if(bubble.getStartNode().getId() == bubble.getEndNode().getId()) {
                System.out.println("Reached");
                continue;
            }
            for (Node node : bubble.getStartNode().getLinks()) {
                List<Node> newBubbles = findLevelBubbles(node, bubble.getEndNode());
                currLevelList.addAll(newBubbles);
            }
        }
        return currLevelList;
    }

    private List<Node> findLevelBubbles(Node startNode, Node destination) {
        if (startNode == destination) {
            return new ArrayList<>();
        }
        List<Node> levelCollection = new ArrayList<>();
        Map.Entry<Integer, Node> stoppedAtNode = searchBubble(startNode, startNode.getGenomes(), destination);
        int status = stoppedAtNode.getKey();
        Node stoppedNode = stoppedAtNode.getValue();
        while (status != REACHED_FINAL_DESTINATION) {
            if (startNode == stoppedNode) {
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
                handleNodeWithNoChildren(startNode, destination, levelCollection, stoppedNode);
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
//                initVisited(collection);
                levelCollection.addAll(findLevelBubbles(childNode, stoppedNode));
            }
        }
        return levelCollection;
    }

    private void handleNodeWithNoChildren(Node startNode, Node destination, List<Node> levelCollection, Node stoppedNode) {
        if (startNode.getGenomes().equals(stoppedNode.getGenomes())) {
            handleDetectedBubble(startNode, stoppedNode, levelCollection);
        } else {
            for (Node childNode : startNode.getLinks()) {
                initVisited(collection);
                levelCollection.addAll(findLevelBubbles(childNode, destination));
            }
        }
    }

    private Map.Entry<Integer, Node> searchBubble(Node curr, Collection<String> genomes, Node destination) {
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
        return new AbstractMap.SimpleEntry<>(NO_CHILDREN_FOUND, curr);
    }

    private int checkGenomeMatch(Collection<String> initGenomes, Node secondNode, Node destination) {
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

    private void initVisited(NodeCollection collection) {
        for (Node node : collection.values()) {
            visited[node.getId()] = false;
        }
    }

    List<Node> getBubbleBoundaries() {
        List<Node> retrieved = new ArrayList<>();
        Set<Map.Entry<Integer, Integer>> uniques = new HashSet<>();
        for (Node bubble : bubbleBoundaries) {
            AbstractMap.SimpleEntry<Integer, Integer> entry = new AbstractMap.SimpleEntry<>(bubble.getStartNode().getId(), bubble.getEndNode().getId());
            if (!uniques.contains(entry)) {
                retrieved.add(bubble);
                uniques.add(entry);
            }
        }
        return retrieved;
    }
}