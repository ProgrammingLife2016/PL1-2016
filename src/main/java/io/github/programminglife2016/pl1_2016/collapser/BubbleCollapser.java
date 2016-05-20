package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Created by ravishivam on 16-5-16.
 *
 */
public class BubbleCollapser {
    List<Node> bubbles;

    public BubbleCollapser(NodeCollection collection){
        BubbleDetector detector = new BubbleDetector(collection);
        detector.findMultiLevelBubbles();
        this.bubbles = detector.getBubbleBoundaries();
    }

    public void collapseBubbles(){
        for (Node bubble : bubbles)
            bubble.getContainer().addAll(breadth(bubble));
        for (Node bubble : bubbles)
            modifyContainer(bubble);
        addLinks(bubbles);
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
//        bubble.getContainer().clear();
        bubble.getContainer().removeIf(n -> n.getContainerId() != bubble.getId());
        for (int id : innerBubbles){
            bubble.getContainer().add(bubbles.stream().filter(b -> b.getId() == id).findFirst().get());
        }
    }

    private void addLinks(List<Node> bubbles){
        for (Node bubble : bubbles){
            System.out.println("Id: " + bubble.getId() + " Contains:" + bubble.getContainer().stream().map(x -> x.getId()).collect(Collectors.toList()));
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
        System.out.println("Id: " + bubble.getId() + " BackLinks:" + linksToString(bubble.getBackLinks()));
    }

    private void addForwardLinks(Node bubble){
        Collection<Node> container;
        for(Node node: bubble.getEndNode().getLinks()) {
            container = bubbles.stream().filter(x -> (x.getStartNode().getId() != x.getEndNode().getId()) &&
                    x.getStartNode().getId() == bubble.getEndNode().getId()).collect(Collectors.toSet());
            if (container.size() > 0)
                bubble.getLinks().addAll(container);
            else
                bubble.getLinks().add(node);
        }
        System.out.println("Id: " + bubble.getId() + " ForwardLinks:" + linksToString(bubble.getLinks()));
    }

    /**
     * Temporary method to view changes in links
     * @param links
     * @return
     */
    private String linksToString(Collection<Node> links){
        String result = "";
        for (Node n: links)
            result += n.getId() + ", ";
        return result;
    }

    public List<Node> getBubbles() {
        return bubbles;
    }

}
