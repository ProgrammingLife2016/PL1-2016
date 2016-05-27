package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Linker class creates links between the bubbles of the same level, and sets level to the bubbles if it is undefined.
 * Created by Kamran Tadzjibov on 22.05.2016.
 */
public class BubbleLinker {
    List<Node> bubbles;
    int lastId;
    int bubblesListSize;
    int lowestLevel = 1;

    /**
     *
     * @param bubbles list of all collapsedSegments bubbles
     */
    public BubbleLinker(List<Node> bubbles){
        this.bubbles = bubbles;
        lastId = bubbles.stream().max((b1, b2) -> Integer.compare( b1.getId(), b2.getId())).get().getId();
        bubblesListSize = bubbles.size();

        System.out.println("Started linking.");
        long startTime = System.currentTimeMillis();
        setCorrectLevelsToNodes();
        long endTime = System.currentTimeMillis();
        System.out.println("Linking time: " + (endTime - startTime)/3600 + " m.");
    }

    private void setCorrectLevelsToNodes(){
        setLevels();
        while(needLowerLevels())
            lowerSegments();
        addLinks();
        List<Node> level = bubbles.stream().filter(x -> x.getZoomLevel() == 2).collect(Collectors.toList());
        for(Node bubble: level)
            System.out.print(linksToString(bubble.getId(), bubble.getLinks(), true));

        for(int i = 0; i < bubbles.size(); i++)
            if(bubbles.get(i).getZoomLevel() == -1)
                throw new RuntimeException("Not single neither nested bubble with zoomlevel = " + bubbles.get(i).getZoomLevel() + ": " + bubbles.get(i));
    }

    private void setLevels(){
        List<Node> highestLevel = bubbles.stream().filter(x -> x.getContainerId() == 0).collect(Collectors.toList());
        for (Node bubble : highestLevel) {
            bubble.setZoomLevel(1);
            setLowerLevels(bubble);
        }
    }

    private void setLowerLevels(Node bubble){
        Set<Node> children = new HashSet<>(bubble.getContainer());
        children.add(bubble.getStartNode());
        children.add(bubble.getEndNode());
        for (Node n : children){
            n.setZoomLevel(bubble.getZoomLevel()+1);
            if(lowestLevel < n.getZoomLevel())
                lowestLevel = n.getZoomLevel();
            if(n.isBubble())
                setLowerLevels(n);
        }
    }

    private boolean needLowerLevels(){
        if(bubbles.stream().filter(x -> (x.getStartNode().isBubble() == false && x.getStartNode().getZoomLevel() < lowestLevel) ||
                (x.getEndNode().isBubble() == false && x.getEndNode().getZoomLevel() < lowestLevel)).count() > 0)
            return true;
        return false;
    }

    /**
     * Connect all bubbles per level with each other to get representative graph.
     */
    public void addLinks(){
//        Comparator<Node> comparator = (b1, b2) ->
//                Boolean.compare(b1.getStartNode().isBubble(), b2.getStartNode().isBubble());
//        comparator = comparator.thenComparing((b1, b2) ->
//                Integer.compare(b2.getZoomLevel(),b1.getZoomLevel()));
//        comparator = comparator.thenComparing((b1, b2) ->
//                Integer.compare(b1.getContainer().size(),b2.getContainer().size()));
//
//        bubbles.sort(comparator);

        List<Node> leafs = bubbles.stream().filter(x -> x.getStartNode().isBubble() == false && x.getEndNode().isBubble() == false).collect(Collectors.toList());
        int l;
        Set<Node> nextLevel = new HashSet<>();

        while(!leafs.isEmpty()) {
            l = leafs.size();
            for (int i = 0; i < l; i++) {
                addLinkToBubble(leafs.get(i));
                final int cId = leafs.get(i).getContainerId();
                if (cId != 0)
                    nextLevel.add(bubbles.stream().filter(x -> x.getId() == cId).findFirst().get());
            }
            //10570
            //5880
            leafs.clear();
            leafs.addAll(nextLevel);
            nextLevel.clear();
        }
        int stop = 0;
    }

    /**
     * Add forward links to the given bubble.
     * @param bubble bubble to link with the rest of the graph
     */
    private void addLinkToBubble(Node bubble){
        Node prospectiveLink;
        for(Node link : bubble.getEndNode().getLinks()){
            prospectiveLink = getHighestBubble(bubble.getZoomLevel(), link);
            if(prospectiveLink != null)
                bubble.getLinks().add(prospectiveLink);
        }
    }

    private Node getHighestBubble(int level, Node prospectiveLink){
        int cId = prospectiveLink.getContainerId();
        if(prospectiveLink.getZoomLevel() > level)
            prospectiveLink = getHighestBubble(level, bubbles.stream().filter(x -> x.getId() == cId).findFirst().get());
        if(prospectiveLink!=null && prospectiveLink.getZoomLevel() == level && prospectiveLink.isBubble())
            return prospectiveLink;
        return null;
    }

    private void lowerSegments(){
        List<Node> needLower = bubbles.stream()
                .filter(x -> !x.getStartNode().isBubble() &&
                        x.getStartNode().getZoomLevel() < lowestLevel)
                .collect(Collectors.toList());
        while(needLower.size() != 0){
            for(Node b : needLower) {
                lowerSegmentInBubble(b);
                if(bubbles.stream().filter(x -> x.getStartNode().isBubble() == false && x.getEndNode().getZoomLevel() > lowestLevel).count() > 0) {
                    // id = 4545
                    int x23 = 0;
                }
            }
            needLower = bubbles.stream().filter(x -> !x.getStartNode().isBubble() && x.getStartNode().getZoomLevel() < lowestLevel).collect(Collectors.toList());
        }
    }

    private void lowerSegmentInBubble(Node bubble){
        int segLevel =  bubble.getZoomLevel()+1;
        bubble.setStartNode(replaceNode(bubble.getStartNode(), segLevel));
//        if(bubble.getEndNode().getZoomLevel() != bubble.getStartNode().getZoomLevel())
        bubble.setEndNode(replaceNode(bubble.getEndNode(), segLevel));
        Set<Node> newContainer = new HashSet<>();
        for(Node n : bubble.getContainer()){
            newContainer.add(replaceNode(n, segLevel));
        }
        bubble.getContainer().clear();
        bubble.getContainer().addAll(newContainer);
    }

    private Bubble initNewBubble(Node node, int level){
        Optional<Node> exist = bubbles.stream().filter(x -> x.getStartNode().getId() == node.getStartNode().getId() &&
                x.getEndNode().getId() == node.getEndNode().getId() && x.getZoomLevel() == level).findFirst();
        if(exist.isPresent())
            return (Bubble)exist.get();
        lastId++;
        Bubble newBubble = new Bubble(lastId, level, (Segment)node);
        bubbles.add(newBubble);
        bubblesListSize++;
        return newBubble;
    }

    private Node replaceNode(Node node, int level){
        Bubble newBubble = initNewBubble(node, level);//new Bubble(lastId, segLevel, (Segment)bubble.getStartNode());
        node.setZoomLevel(level+1);
//        if(node.getZoomLevel() > lowestLevel)
//            lowestLevel = node.getZoomLevel();
        return newBubble;
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
