package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ravishivam on 16-5-16.
 *
 */
public class BubbleCollapser {

    List<Bubble> bubbles;

    public BubbleCollapser(List<Bubble> bubbles){
        this.bubbles = bubbles;
    }

    public void collapseBubbles(){
        for (Bubble bubble : bubbles)
            bubble.getContainer().addAll(breadth(bubble));
        addLinks(bubbles);
    }

    private List<Node> breadth(Bubble bubble) {
        int startId =  bubble.getStartNode().getId();
        int endId =  bubble.getEndNode().getId();
        Queue<Node> q = new ConcurrentLinkedQueue<>();
        q.add(bubble.getStartNode());
        List<Node> visited = new ArrayList<>();
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

    private void addLinks(List<Bubble> bubbles){
        for (Bubble bubble : bubbles){
            addBackLinks(bubble);
            addForwardLinks(bubble);
        }
    }

    private void addBackLinks(Bubble bubble){
        Optional<Bubble> container;
        for(Node node: bubble.getStartNode().getBackLinks()) {
            container = bubbles.stream().filter(x -> x.getId() == node.getContainerId()).findFirst();
            if (container.isPresent())
                bubble.getBackLinks().add(container.get());
            else
                bubble.getBackLinks().add(node);
        }
        System.out.println("Id: " + bubble.getId() + " BackLinks:" + bubble.getBackLinks().size());
    }

    private void addForwardLinks(Bubble bubble){
        Optional<Bubble> container;
        for(Node node: bubble.getEndNode().getLinks()) {
            container = bubbles.stream().filter(x -> x.getId() == node.getContainerId()).findFirst();
            if (container.isPresent())
                bubble.getLinks().add(container.get());
            else
                bubble.getLinks().add(node);
        }
        System.out.println("Id: " + bubble.getId() + " ForwardLinks:" + bubble.getBackLinks().size());
    }
}
