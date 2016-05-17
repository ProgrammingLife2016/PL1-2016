package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ravishivam on 16-5-16.
 *
 */
public class BubbleCollapser {

    public void collapseBubbles(List<Bubble> bubbles){
        int i = 0;
        for (Bubble bubble : bubbles){
            bubble.getContainer().addAll(breadth(bubble));
            System.out.println("Id: " + i +" Found nodes: " + bubble.getContainer());
            i++;
        }
    }

    public List<Node> breadth(Bubble bubble) {
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
                q.add(v);
                visited.add(v);
            }
        }
        return visited;
    }
}
