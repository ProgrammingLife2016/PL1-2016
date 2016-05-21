package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Created by ravishivam on 16-5-16.
 *
 */
public class BubbleCollapser {
    List<Node> bubbles;
    List<Node> newBubbles = new ArrayList<>();
    int lastId;

    public BubbleCollapser(NodeCollection collection){
        BubbleDetector detector = new BubbleDetector(collection);
        detector.findMultiLevelBubbles();
        this.bubbles = detector.getBubbleBoundaries();
        lastId = bubbles.stream().max((b1, b2) -> Integer.compare( b1.getId(), b2.getId())).get().getId();
    }

    public void collapseBubbles(){
        for (Node bubble : bubbles)
            bubble.getContainer().addAll(breadth(bubble));
        for (Node bubble : bubbles)
            modifyContainer(bubble);
        addLinks(bubbles);
        bubbles.addAll(newBubbles);
    }

    private List<Node> breadth(Node bubble) {
        List<Node> visited = new ArrayList<>();
        int startId =  bubble.getStartNode().getId();
        int endId =  bubble.getEndNode().getId();
        if(startId == endId)
            return visited;
        bubble.getStartNode().setContainerId(bubble.getId());
        bubble.getEndNode().setContainerId(bubble.getId());
        Queue<Node> q = new ConcurrentLinkedQueue<>();
        q.add(bubble.getStartNode());
        while (!q.isEmpty()) {
            Node n = q.poll();
            for (Node v : n.getLinks()) {
                if (visited.contains(v) || v.getId() == startId || v.getId() == endId)
                    continue;
                v.setContainerId(bubble.getId());
                q.add(v);
                visited.add(v);
            }
        }
        return visited;
    }

    private void modifyContainer(Node bubble){
        Set<Integer> innerBubbles = new HashSet<>();
        for(Node n : bubble.getContainer()){
            if(n.getContainerId() > 0 && n.getContainerId() != bubble.getId()) {
                innerBubbles.add(n.getContainerId());
            }
        }
        bubble.getContainer().removeIf(n -> n.getContainerId() != bubble.getId());
        for (int id : innerBubbles){
            bubble.getContainer().add(bubbles.stream().filter(b -> b.getId() == id).findFirst().get());
        }
    }

    private void addLinks(List<Node> bubbles){
        for (Node bubble : bubbles){
//            System.out.println("Id: " + bubble.getId() + " Contains:" + bubble.getContainer().stream().map(x -> x.getId()).collect(Collectors.toList()));
//            System.out.println("ZoomLevel: " + bubble.getZoomLevel());
            addBackLinks(bubble);
            addForwardLinks(bubble);
        }
    }

    private void addBackLinks(Node bubble){
        Collection<Node> container;
        for(Node node: bubble.getStartNode().getBackLinks()) {
            container = bubbles.stream().filter(x -> (x.getStartNode().getId() != x.getEndNode().getId()) &&
                    x.getEndNode().getId() == bubble.getStartNode().getId()).collect(Collectors.toSet());
            if (container.size() > 0)
                bubble.getBackLinks().addAll(container);
            else
                bubble.getBackLinks().add(node);
        }
//        System.out.println("Id: " + bubble.getId() + " BackLinks:" + linksToString(bubble.getBackLinks()));
    }

    private void addForwardLinks(Node bubble){
        Collection<Node> container;
        for(Node node: bubble.getEndNode().getLinks()) {
            container = bubbles.stream().filter(x -> (x.getStartNode().getId() != x.getEndNode().getId()) &&
                    x.getStartNode().getId() == bubble.getEndNode().getId()).collect(Collectors.toSet());
            if (container.size() > 0)
                bubble.getLinks().addAll(container);
            else
                bubble.getLinks().add(getBestParentNode(node, bubble.getZoomLevel()));//
        }
//        System.out.println("Id: " + bubble.getId() + " Contains:" + bubble.getContainer().stream().map(x -> x.getId()).collect(Collectors.toList()));
//        System.out.println("Links: " + bubble.getLinks().stream().collect(Collectors.toList()));
//        System.out.println("StartNode: " + bubble.getStartNode());
//        System.out.println("EndNode: " + bubble.getEndNode());
        System.out.print("" + linksToString(bubble.getId(), bubble.getLinks()));
    }

    private Node getBestParentNode(Node leaf, int boundZoom){
        if(leaf instanceof Segment)
            leaf = createNewBubbleFromSegment((Segment) leaf);
        Node bestParent = leaf;
        for (Node newCont : bubbles){
            if(newCont.getId() == bestParent.getContainerId()) {
                if(newCont.getZoomLevel() >= boundZoom)
                    bestParent = newCont;
            }
        }
        if(bestParent.getId() != leaf.getId())
            return getBestParentNode(bestParent, boundZoom);
        return bestParent;
    }

    private Bubble createNewBubbleFromSegment(Segment segment){
        lastId++;
        Bubble newB = new Bubble(lastId, segment, segment);
        newB.setZoomLevel(segment.getZoomLevel());
        newB.setContainerId(segment.getContainerId());
        newBubbles.add(newB);
        return newB;
    }

    /**
     * Temporary method to view changes in links
     * @param links
     * @return
     */
    private String linksToString(int bId, Collection<Node> links){
        String result = "";
        for (Node n: links)
            result += bId + " -> " + n.getId() + "\n";
        return result;
    }


    public List<Node> getBubbles() {
        return bubbles;
    }

}
