package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

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
        addLinks(bubbles);
//        for (Node bubble : bubbles)
        for(int i = 0; i < bubbles.size(); i++)
            modifyContainer(bubbles.get(i));
        removeUnnecessaryBubbles(bubbles);
        modifyStartNodes();
        modifyEndNodes();
        addInnerLinks();
        System.out.println("Linked");
        for (Node bubble : bubbles) {
            System.out.print("" + linksToString(bubble.getId(), bubble.getLinks(), true));
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
        Set<Integer> innerBubbles = findAllInnerBubbles(bubble.getContainer(), bubble.getId());
//        bubble.getContainer().removeIf(n -> n.getContainerId() != bubble.getId());
        for (int id : innerBubbles){
            bubble.getContainer().add(bubbles.stream().filter(b -> b.getId() == id).findFirst().get());
        }

        if(bubble.getContainer().stream().filter(x -> x.isBubble()).collect(Collectors.toList()).size() != 0) {
            for (int i = 0; i < bubble.getContainer().size(); i++) {
                if (bubble.getContainer().get(i) instanceof Segment) {
                    lastId++;
                    Node newChild = //Bubble.getBestParentNode(lastId, segS, bubbles, bubbles.get(i).getZoomLevel(), true);
                            new Bubble(lastId, bubbles.get(i).getZoomLevel(), (Segment) bubble.getContainer().get(i));
                    bubble.getContainer().remove(i);
                    bubble.getContainer().add(newChild);
                    bubbles.add(newChild);
                }
            }
        }
    }

    private void modifyStartNodes(){
        for(int i = 0; i < bubblesListSize; i++){
            Node segS = bubbles.get(i).getStartNode();
            boolean containsBubbles = bubbles.get(i).getContainer().stream().filter(x -> x.isBubble()).collect(Collectors.toList()).size() != 0;
            if(segS.getId() != bubbles.get(i).getEndNode().getId() && containsBubbles) {
                if (segS instanceof Segment) {
                    lastId++;
                    Node newStart = //Bubble.getBestParentNode(lastId, segS, bubbles, bubbles.get(i).getZoomLevel(), true);
                             new Bubble(lastId, bubbles.get(i).getZoomLevel(), (Segment) segS);
                    if(bubbles.contains(newStart)) {
                        lastId--;
                        continue;
                    }
                    bubbles.get(i).setStartNode(newStart);
                    bubbles.add(newStart);
                    bubblesListSize++;
                }
            }
        }
    }

    private void modifyEndNodes(){
        for(int i = 0; i < bubblesListSize; i++){
            Node segE = bubbles.get(i).getEndNode();
            boolean containsBubbles = bubbles.get(i).getContainer().stream().filter(x -> x.isBubble()).collect(Collectors.toList()).size() != 0;
            if(segE.getId() != bubbles.get(i).getStartNode().getId() && containsBubbles) {
                if (segE instanceof Segment) {
                    Optional<Node> newEnd = bubbles.stream().filter(x -> x.getStartNode().equals(x.getEndNode()) && x.getStartNode().equals(segE)).findFirst();
                    if(newEnd.isPresent()) {
                        bubbles.get(i).setEndNode(newEnd.get());
                    }
                }
            }
        }
    }

    private void addInnerLinks(){
        for(Node bubble : bubbles) {
            boolean containsBubbles = bubble.getContainer().stream().filter(x -> x.isBubble()).collect(Collectors.toList()).size() != 0;
            if(containsBubbles) {
                addInnerStartLinks(bubble);
                addInnerContainerLinks(bubble);
            }
        }
    }

    private void addInnerStartLinks(Node bubble){
//        if(containsBubbles) {
            Set<Node> newStartLinks = new HashSet<>();
            for (Node oldStartLink : bubble.getStartNode().getLinks()) {
//                newStartLinks.addAll(bubble.getContainer()
//                                                .stream()
//                                                .filter(x -> oldStartLink.getId() == x.getStartNode().getId())
//                                                .collect(Collectors.toSet()));
                newStartLinks.add(Bubble.getBestParentNode(-1, oldStartLink, bubble.getContainer(), bubble.getStartNode().getZoomLevel(), true));
            }
            bubble.getStartNode().getLinks().clear();
            bubble.getStartNode().getLinks().addAll(newStartLinks);
//        }
    }

    private void addInnerContainerLinks(Node bubble){
        Set<Node> allPossibleLinks = new HashSet<>(bubble.getContainer());
        allPossibleLinks.add(bubble.getEndNode());
        Set<Node> newChildLinks;
        for(Node child : bubble.getContainer()) {
            newChildLinks = new HashSet<>();
            for (Node oldChildLink : child.getLinks()) {
//                newChildLinks.addAll(
//                        allPossibleLinks.stream().filter(x -> oldChildLink.getId() == x.getStartNode().getId()).collect(Collectors.toSet()));
                newChildLinks.add(Bubble.getBestParentNode(-1, oldChildLink, allPossibleLinks, bubble.getStartNode().getZoomLevel(), true));
            }
            if(newChildLinks.size() > 0) {
                child.getLinks().clear();
                child.getLinks().addAll(newChildLinks);
            }
        }
    }


    /**
     * Finds id's of all nested bubbles through all levels.
     * @param container container of the bubble that should be modified
     * @param bubbleId id of the bubble which container should be modified
     * @return unique set of id's of all nested bubbles.
     */
    private Set<Integer> findAllInnerBubbles(Collection<Node> container, int bubbleId){
        Set<Integer> innerBubbles = new HashSet<>();
        for(Node n : container){
            if(n.getContainerId() > 0 && n.getContainerId() != bubbleId) {
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
    private void removeUnnecessaryBubbles(Collection<Node> bubbles){
        for(Node bubble : bubbles)
            bubble.getContainer().removeIf(x -> isNested(bubble.getContainer(), x));
    }

    /**
     * @param container collection of nodes that represent container of a particular bubble
     * @param bubble node to invent if it already defined in container of lower level
     * @return true if given bubble is already defined in the container of other lower level bubbles.
     */
    private boolean isNested(Collection<Node> container, Node bubble){
        for(Node node : container){
            if(node.getContainer().contains(bubble))
                return true;
        }
        return false;
    }

    /**
     * Connect all bubbles per level with each other to get representative graph.
     * @param bubbles list of all collapsed bubbles
     */
    private void addLinks(List<Node> bubbles){
        for (int i = 0; i < bubblesListSize; i++){
//            addBackLinks(bubbles.get(i));
            addForwardLinks(bubbles.get(i));
        }
    }

//    private void addBackLinks(Node bubble){
//        Collection<Node> container;
//        for(Node node: bubble.getStartNode().getBackLinks()) {
//            container = bubbles.stream().filter(x -> (x.getStartNode().getId() != x.getEndNode().getId()) &&
//                    x.getEndNode().getId() == bubble.getStartNode().getId()).collect(Collectors.toSet());
//            if (container.size() > 0)
//                bubble.getBackLinks().addAll(container);
//            else {
//                bubble.getBackLinks().remove(node);
//                lastId++;
//                Node newBubble = Bubble.getBestParentNode(lastId, node, bubbles, bubble.getZoomLevel(), false);
//                bubble.getBackLinks().add(newBubble);//
//                if(bubbles.contains(newBubble)) {
//                    lastId--;
//                    continue;
//                }
//                bubbles.add(newBubble);
//                bubblesListSize++;
//            }
////            System.out.print("" + linksToString(bubble.getId(), bubble.getBackLinks(), false));
//        }
//    }
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
                Node newBubble = Bubble.getBestParentNode(lastId, node, bubbles, bubble.getZoomLevel(), true);
                bubble.getLinks().add(newBubble);//
                if(bubbles.contains(newBubble)){
                    lastId--;
                    continue;
                }
                bubbles.add(newBubble);
                bubblesListSize++;
            }
        }
//        System.out.println("Id: " + bubble.getId() + " Contains:" + bubble.getContainer().stream().map(x -> x.getId()).collect(Collectors.toList()));
//        System.out.println("Links: " + bubble.getLinks().stream().collect(Collectors.toList()));
//        System.out.println("StartNode: " + bubble.getStartNode());
//        System.out.println("EndNode: " + bubble.getEndNode());
//        System.out.print("" + linksToString(bubble.getId(), bubble.getLinks(), true));
    }

    /**
     * Temporary method to view changes in links
     * @param links
     * @return
     */
    private String linksToString(int bId, Collection<Node> links, boolean forward){
        String result = "";
        for (Node n: links) {
            if(forward)
                result += bId + " -> " + n.getId() + "\n";
            else
                result += n.getId() + " -> " + bId + "\n";
        }
        return result;
    }


    public List<Node> getBubbles() {
        return bubbles;
    }

}
