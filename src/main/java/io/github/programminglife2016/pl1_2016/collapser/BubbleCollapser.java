package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Collapser populates detected bubbles with bubbles of the lower level or with segments.
 *
 * @author Kamran Tadzjibov
 *
 */
public class BubbleCollapser {
    List<Node> bubbles;
    int lastId;
    int bubblesListSize;
    Set<Integer> collapsedSegments = new HashSet<>();
    NodeCollection collection;
    BubbleLinker linker;

    /**
     * Initialize all bubbles to collapse.
     * @param collection of nodes
     */
    public BubbleCollapser(NodeCollection collection) {
        this.collection = collection;
        BubbleDetector detector = new BubbleDetector(collection);
        detector.findMultiLevelBubbles();
        this.bubbles = detector.getBubbleBoundaries();
        lastId = bubbles.stream().max((b1, b2) ->
                Integer.compare(b1.getId(), b2.getId())).get().getId();
        bubblesListSize = bubbles.size();
    }

    /**
     * Main method that collapses all bubbles.
     */
    public void collapseBubbles() {
        for (Node bubble : bubbles) {
            bubble.getContainer().addAll(bfs(bubble));
        }
        for (int i = 0; i < bubblesListSize; i++) {
            modifyContainer(bubbles.get(i));
        }
        removeUnnecessaryBubbles(bubbles);
        addContainerIdToNestedBubbles(bubbles);
        collapseSingleSegments(collection);
        collapseInnerSegments();
        replaceInconsistentSegments();
        linker = new BubbleLinker(bubbles);
        linker.addLinks();
    }

    private void replaceInconsistentSegments() {
        for (Node bubble : bubbles) {
            if (bubble.getStartNode().isBubble() && !bubble.getEndNode().isBubble()) {
                bubble.setEndNode(bubbles.stream().filter(x ->
                        x.getId() == bubble.getEndNode().getContainerId()).findFirst().get());
            }
            else if (!bubble.getStartNode().isBubble() && bubble.getEndNode().isBubble()) {
                bubble.setStartNode(bubbles.stream().filter(x ->
                        x.getId() == bubble.getStartNode().getContainerId()).findFirst().get());
            }
        }
    }

    /**
     * Find all segments that given bubble contains using breadth-first-search .
     * @param bubble bubble to collapse
     * @return
     */
    private List<Node> bfs(Node bubble) {
        List<Node> visited = new ArrayList<>();
        int startId =  bubble.getStartNode().getId();
        int endId =  bubble.getEndNode().getId();
        collapsedSegments.add(startId);
        collapsedSegments.add(endId);
        if (startId == endId) {
            return visited;
        }
        bubble.getStartNode().setContainerId(bubble.getId());
        bubble.getEndNode().setContainerId(bubble.getId());
        Queue<Node> q = new ConcurrentLinkedQueue<>();
        q.add(bubble.getStartNode());
        while (!q.isEmpty()) {
            Node n = q.poll();
            for (Node v : n.getLinks()) {
                if (visited.contains(v) || v.getId() == startId || v.getId() == endId) {
                    continue;
                }
                v.setContainerId(bubble.getId());
                q.add(v);
                visited.add(v);
                collapsedSegments.add(v.getId());
            }
        }
        return visited;
    }

    private void collapseSingleSegments(NodeCollection collection) {
        for (Node node : collection.values()) {
            if (collapsedSegments.contains(node.getId())) {
                continue;
            }
            initNewBubble(node);
        }
    }

    private void collapseInnerSegments() {
        Set<Node> newCont = new HashSet<>();
        for (int i = 0; i < bubblesListSize; i++) {
            Node segS = bubbles.get(i).getStartNode();
            boolean containsBubbles = bubbles.get(i)
                    .getContainer()
                    .stream()
                    .filter(x -> x.isBubble())
                    .collect(Collectors.toList()).size() != 0;
            boolean tooComplex = bubbles.get(i).getContainer().size() > 2
                    && bubbles.get(i).getContainer()
                    .stream()
                    .filter(x -> !x.isBubble()).count() > 0;
            if (containsBubbles || tooComplex) {
                if (!segS.isBubble()) {
                    bubbles.get(i).setStartNode(initNewBubble(segS));
                    bubbles.get(i).setEndNode(initNewBubble(bubbles.get(i).getEndNode()));
                    for (Node n : bubbles.get(i).getContainer()) {
                        if (!n.isBubble()) {
                            newCont.add(initNewBubble(n));
                        }
                        else {
                            newCont.add(n);
                        }
                    }
                    bubbles.get(i).getContainer().clear();
                    bubbles.get(i).getContainer().addAll(newCont);
                    newCont.clear();
                }
            }
        }
    }

    private Bubble initNewBubble(Node node) {
        Optional<Node> exist = bubbles.stream().filter(x ->
                x.getStartNode().getId() == node.getStartNode().getId()
                && x.getEndNode().getId() == node.getEndNode().getId()).findFirst();
        if (exist.isPresent()) {
            return (Bubble) exist.get();
        }
        lastId++;
        Bubble newBubble = new Bubble(lastId, -1, (Segment) node);
        bubbles.add(newBubble);
        bubblesListSize++;
        return newBubble;
    }

    /**
     * Replace segments by bubbles of higher level in the container of given bubble.
     * @param bubble bubble populated with segments
     */
    private void modifyContainer(Node bubble) {
        Set<Integer> innerBubbles = findAllInnerBubbles(bubble.getContainer(), bubble.getId());
        for (int id : innerBubbles) {
            bubble.getContainer().add(bubbles.stream()
                    .filter(b -> b.getId() == id).findFirst().get());
        }
        if (bubble.getContainer().stream().filter(x -> x.isBubble())
                .collect(Collectors.toList()).size() != 0) {
            for (int i = 0; i < bubble.getContainer().size(); i++) {
                if (bubble.getContainer().get(i) instanceof Segment) {
                    lastId++;
                    Node newChild = new Bubble(lastId, bubbles.get(i).getZoomLevel(),
                            (Segment) bubble.getContainer().get(i));
                    bubble.getContainer().remove(i);
                    bubble.getContainer().add(newChild);
                    bubbles.add(newChild);
                    bubblesListSize++;
                }
            }
        }
    }

    /**
     * Finds id's of all nested bubbles through all levels.
     * @param container container of the bubble that should be modified
     * @param bubbleId id of the bubble which container should be modified
     * @return unique set of id's of all nested bubbles.
     */
    private Set<Integer> findAllInnerBubbles(Collection<Node> container, int bubbleId) {
        Set<Integer> innerBubbles = new HashSet<>();
        for (Node n : container) {
            if (n.getContainerId() > 0 && n.getContainerId() != bubbleId) {
                innerBubbles.add(n.getContainerId());
            }
        }
        container.removeIf(n -> n.getContainerId() != bubbleId);
        return innerBubbles;
    }

    /**
     * Remove bubbles which are already defined in deeper level.
     * Example: Bubble [id=1, container={2,3}]
     *          Bubble [id=2, container={3}]
     *          Bubble [id=3, container={}]
     * Bubble 3 will be removed from container of bubble 1.
     * @param bubbles collection of nodes that represent container of a particular bubble
     */
    private void removeUnnecessaryBubbles(Collection<Node> bubbles) {
        for (Node bubble : bubbles) {
            bubble.getContainer().removeIf(x -> isNested(bubble.getContainer(), x));
        }
    }

    private void addContainerIdToNestedBubbles(Collection<Node> bubbles) {
        for (Node bubble : bubbles) {
            bubble.getContainer().stream()
                  .filter(x -> x.getContainerId() != bubble.getId())
                  .forEach(x -> x.setContainerId(bubble.getId()));
            bubble.getStartNode().setContainerId(bubble.getId());
            bubble.getEndNode().setContainerId(bubble.getId());
        }
    }

    /**
     * @param container collection of nodes that represent container of a particular bubble
     * @param bubble node to invent if it already defined in container of lower level
     * @return true if given bubble is already defined
     *          in the container of other lower level bubbles.
     */
    private boolean isNested(Collection<Node> container, Node bubble) {
        for (Node node : container) {
            if (node.getContainer().contains(bubble)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return list of the bubbles
     */
    public List<Node> getBubbles() {
        return bubbles;
    }
}