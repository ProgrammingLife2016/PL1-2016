package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Takes apart collapsed layered bubbles and uncollapses most important parts depending on threshold value.
 *
 * @author Kamran Tadzjibov
 */
public class BubbleDispatcher {

    private List<Node> bubbleCollection;

    public BubbleDispatcher(NodeCollection collection) {
        BubbleCollapser collapser = new BubbleCollapser(collection);
        collapser.collapseBubbles();
        this.bubbleCollection = collapser.getBubbles();
        initDispatcher();
    }

    /**
     * Initialize dispatcher by calculating real container size.
     */
    private void initDispatcher() {
        for (int i = 0; i < bubbleCollection.size(); i++) {
            Node bubble = bubbleCollection.get(i);
            bubble.setContainerSize(getBubbleSize(bubble));
        }
    }

    /**
     * Calculates real bubble size.
     * Only segments are counted to calculate size.
     * @param bubble to get amount of segments that it contains
     * @return size of bubble
     */
    private int getBubbleSize(Node bubble) {
        if (bubble.getContainer().size() == 0) {
            return 1;
        }
        else {
            int size = 2;
            for (int i = 0; i < bubble.getContainer().size(); i++) {
                size += getBubbleSize(bubble.getContainer().get(i));
            }
            return size;
        }
    }

    /**
     * Get graph where each bubble contains not more segments than given threshold.
     * @param threshold value to filter the graphs bubbles
     * @return graph with thresholded bubbles
     */
    public NodeCollection getThresholdedBubbles(int threshold) {
        Set<Node> filtered = filterBubbles(threshold);
        findNewLinks(filtered);
        return listAsNodeCollection(filtered);
    }

    /**
     * Get unlinked bubbles by given threshold.
     * @param threshold value to filter the graphs bubbles
     * @return set of wrong linked in current context bubbles which are filtered on threshold value
     */
    private Set<Node> filterBubbles(int threshold) {
        List<Integer> containers = new ArrayList<>();
        Set<Node> filtered = new HashSet<>();
        int maxLevel = bubbleCollection.stream().filter(x -> !x.getStartNode().isBubble()).max((b1, b2) -> Integer.compare( b1.getStartNode().getZoomLevel(), b2.getStartNode().getZoomLevel())).get().getStartNode().getZoomLevel();
        for(int i = 1; i < maxLevel; i++) {
            final int currentLevel = i;
            Set<Node> tempFiltered = getFilteredNodes(containers, currentLevel, filtered, threshold);
            for (Node bubble : tempFiltered)
                containers.add(bubble.getId());
            filtered.addAll(tempFiltered);
        }
        return filtered;
    }

    /**
     * Get all needed bubbles from current level by given threshold
     * @param containers list of bubble ids which are directly or indirectly in the set of filtered bubbles
     * @param currentLevel current bubbles level
     * @param filtered set of already filtered nodes
     * @param threshold value to filter the graphs bubbles
     * @return filtered bubbles by threshold for given level
     */
    private Set<Node> getFilteredNodes(List<Integer> containers, int currentLevel, Set<Node> filtered, int threshold){
        Set<Node> tempFiltered = new HashSet<>();
        for (Node x :  bubbleCollection) {
            if (filtered.stream().filter(b -> b.getEndNode().equals(x)).count() == 0 &&
                    x.getZoomLevel() == currentLevel &&
                    x.getContainerSize() <= threshold &&
                    !containers.contains(x.getContainerId())) {
                tempFiltered.add(x);
            }
            if (containers.contains(x.getContainerId()))
                containers.add(x.getId());
        }
        return tempFiltered;
    }

    /**
     * Replace old links by the found links for each bubble in given collection.
     * @param filteredBubbles collection of by threshold filtered bubbles
     */
    private void findNewLinks(Collection<Node> filteredBubbles) {
        int fout = 0;
        for(Node bubble: filteredBubbles){
            Node primitiveEnd = getPrimitiveEnd(bubble);
            Node prospectiveLink = getExistingAncestor(primitiveEnd, filteredBubbles, (n1, n2) -> n1.getStartNode().equals(n2));
            bubble.getLinks().clear();
            if(prospectiveLink != null && !prospectiveLink.equals(bubble)) {
                bubble.getLinks().add(prospectiveLink);
            }
            else {
                for (Node primLink : primitiveEnd.getLinks()) {
                    Node found = getExistingAncestor(primLink, filteredBubbles, (n1, n2) -> n1.getId() == n2.getContainerId());
                    if(found != null)
                        bubble.getLinks().add(found);
                    else {
                        fout++;
                    }
                }
            }
//            System.out.print(linksToString(bubble.getId(), bubble.getLinks()));
        }
//        System.out.println("Aantal verkeerd opgestelde bubbles: " + fout);
    }

    /**
     * Get simple leaf segment of the given end node.
     * @param node Bubble to get end segment
     * @return end segment of given bubble
     */
    private Node getPrimitiveEnd(Node node) {
        if(node.getEndNode().isBubble())
            return getPrimitiveEnd(node.getEndNode());
        return node.getEndNode();
    }

    /**
     * Get highest existing ancestor which exist in filtered bubbles.
     * @param node to find its ancestor
     * @param filteredBubbles collection of by threshold filtered bubbles
     * @param equals equals function to filter the ancestors
     * @return highest existing ancestor which exist in filtered bubbles
     */
    private Node getExistingAncestor(Node node, Collection<Node> filteredBubbles, BiFunction<Node, Node, Boolean> equals){
        if(filteredBubbles.contains(node)) {
            Optional<Node> parent = filteredBubbles.stream().filter(x -> equals.apply(x, node)).findFirst();
            if(parent.isPresent())
                return parent.get();
            return node;
        }
        Optional<Node> n = bubbleCollection.stream().filter(x -> equals.apply(x, node)).findFirst();
        if(n.isPresent())
            return getExistingAncestor(n.get(), filteredBubbles, equals);
        return null;
    }

    /**
     * Method to print graphviz friendly linked nodes.
     * @param bId bubble id
     * @param links links collection
     * @return graphviz friendly from-to link
     */
    private String linksToString(int bId, Collection<Node> links){
        String result = "";
        for (Node n: links)
            result += bId + " -> " + n.getId() + ";\n";
        return result;
    }

    /**
     * Convert collection of nodes to NodeCollection
     * @param res collection of nodes
     * @return NodeCollection of given nodes
     */
    private NodeCollection listAsNodeCollection(Collection<Node> res) {
        NodeCollection collection = new NodeMap();
        for(Node node: res) {
            collection.put(node.getId(), node);
        }
        return collection;
    }

}