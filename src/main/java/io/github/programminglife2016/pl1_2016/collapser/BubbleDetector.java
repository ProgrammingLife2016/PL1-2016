package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.util.*;

/**
 * Created by ravishivam on 15-5-16.
 */
public class BubbleDetector {
    private static final int NOT_A_BUBBLE = 0;

    private static final int BUBBLE_DETECTED = 1;

    private static final int FOUND_MORE_GENOMES = 2;

    private boolean[] visited;

    private NodeCollection collection;

    private List<Bubble> bubbleBoundaries;

    private int lastId;
    
    private int reachedLevel = 1;

    public BubbleDetector(NodeCollection collection) {
        this.visited = new boolean[collection.size() + 1];
        this.lastId = collection.size() + 1;
        initVisited(collection);
        this.collection = collection;
        this.bubbleBoundaries = new ArrayList<>();
    }

    public void findLevelBubbles(NodeCollection collection) {
        Node starting = collection.get(1);
        Map.Entry<Integer, Node> bubbleAt = searchBubble(collection, starting, starting.getGenomes());
        while (bubbleAt != null) {
            switch (bubbleAt.getKey()) {
                case BUBBLE_DETECTED :
                    Bubble found = new Bubble(lastId, starting, bubbleAt.getValue());
                    found.getStartNode().setContainerId(found.getId());
                    this.bubbleBoundaries.add(found);
                    lastId++;
                    starting = bubbleAt.getValue();
                    bubbleAt = searchBubble(collection, starting, starting.getGenomes());
                    break;
                case FOUND_MORE_GENOMES :
                    starting = bubbleAt.getValue();
                    bubbleAt = searchBubble(collection,bubbleAt.getValue(), bubbleAt.getValue().getGenomes());
                    break;
            }
        }
    }

    public Map.Entry<Integer, Node> searchBubble(NodeCollection collection, Node source, Collection genomes) {
        visited[source.getId()] = true;
        List<Node> connectedTo = new ArrayList<>(source.getLinks());
        for (Node child : connectedTo) {
            int status = checkGenomeMatch(genomes, child.getGenomes());
            if (status != NOT_A_BUBBLE) {
                return new AbstractMap.SimpleEntry<Integer, Node>(status, child);
            }
            if (visited[child.getId()] == false) {
                return searchBubble(collection, child, genomes);
            }
        }
        return null;
    }

    private int checkGenomeMatch(Collection genomes, Set<String> secondSet) {
        if(genomes.equals(secondSet)) {
            return BUBBLE_DETECTED;
        }
        else if (isLargerSet(genomes, secondSet)){
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

    private void initVisited(NodeCollection collection) {
        for (Node node : collection.getNodes()) {
            visited[node.getId()] = false;
        }
    }

    public List<Bubble> getBubbleBoundaries() {
        return bubbleBoundaries;
    }
}
