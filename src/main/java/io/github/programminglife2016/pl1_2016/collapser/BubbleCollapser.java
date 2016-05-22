package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Collapser populates detected bubbles with bubbles of the lower level or with segments.
 *
 * Created by ravishivam on 16-5-16.
 *
 */
public class BubbleCollapser {
    List<Node> bubbles;
    int lastId;
    int bubblesListSize;

    public BubbleCollapser(NodeCollection collection){
        BubbleDetector detector = new BubbleDetector(collection);
        detector.findMultiLevelBubbles();
        this.bubbles = detector.getBubbleBoundaries();
        System.out.println("Detected");
        for (int i = 0; i < bubbles.size(); i++) {
            System.out.println(bubbles.get(i));
        }
        System.out.println();
        lastId = bubbles.stream().max((b1, b2) -> Integer.compare( b1.getId(), b2.getId())).get().getId();
        bubblesListSize = bubbles.size();
    }

    /**
     * Main method that collapses all bubbles.
     */
    public void collapseBubbles(){
        for (Node bubble : bubbles)
            bubble.getContainer().addAll(bfs(bubble));
        for (Node bubble : bubbles)
            modifyContainer(bubble);
        addLinks(bubbles);
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

    /**
     * Replace segments by bubbles of higher level in the container of given bubble.
     * @param bubble bubble populated with segments
     */
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

    /**
     * Connect all bubbles per level with each other to get representative graph.
     * @param bubbles list of all collapsed bubbles
     */
    private void addLinks(List<Node> bubbles){
        for (int i = 0; i < bubblesListSize; i++){
            addForwardLinks(bubbles.get(i));
        }
    }

    /**
     * Add forward links to the given bubble
     * @param bubble bubble to link with the rest of the graph
     */
    private void addForwardLinks(Node bubble){
        Collection<Node> container;
        for(Node node: bubble.getEndNode().getLinks()) {
            container = bubbles.stream().filter(x -> (x.getStartNode().getId() != x.getEndNode().getId()) &&
                    x.getStartNode().getId() == bubble.getEndNode().getId()).collect(Collectors.toSet());
            if (container.size() > 0)
                bubble.getLinks().addAll(container);
            else {
                bubble.getLinks().remove(node);
                lastId++;
                Node newBubble = Bubble.getBestParentNode(lastId, node, bubbles, bubble.getZoomLevel());
                bubble.getLinks().add(newBubble);//
                bubbles.add(newBubble);
                bubblesListSize++;

            }
        }
//        System.out.println("Id: " + bubble.getId() + " Contains:" + bubble.getContainer().stream().map(x -> x.getId()).collect(Collectors.toList()));
//        System.out.println("Links: " + bubble.getLinks().stream().collect(Collectors.toList()));
//        System.out.println("StartNode: " + bubble.getStartNode());
//        System.out.println("EndNode: " + bubble.getEndNode());
//        System.out.print("" + linksToString(bubble.getId(), bubble.getLinks()));
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
