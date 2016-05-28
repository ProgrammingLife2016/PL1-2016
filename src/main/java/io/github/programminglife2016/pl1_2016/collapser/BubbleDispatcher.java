package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


public class BubbleDispatcher {

    public List<Node> bubbleCollection;

    public BubbleDispatcher(NodeCollection collection) {
        BubbleCollapser collapser = new BubbleCollapser(collection);
        collapser.collapseBubbles();
        this.bubbleCollection = collapser.getBubbles();
//        System.out.println("Collapsed:");
//        for (Node bubble : bubbleCollection) {
//            System.out.println("ID: " + bubble.getId() + "  Container: " + bubble.getContainer());
//        }
        initDispatcher();
    }

    private void initDispatcher() {
        for (int i = 0; i < bubbleCollection.size(); i++) {
            Node bubble = bubbleCollection.get(i);
            bubble.setContainerSize(getBubbleSize(bubble));
        }
    }

    public int getBubbleSize(Node bubble) {
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


    public NodeCollection getLevelBubbles(int level, int threshold) {
        Set<Node> filtered = filterBubbles(threshold);
        findNewLinks(filtered);
        return listAsNodeCollection(filtered);
    }

    public Set<Node> filterBubbles(int threshold) {
        List<Integer> containers = new ArrayList<>();
        Set<Node> filtered = new HashSet<>();
        int maxLevel = bubbleCollection.stream().filter(x -> !x.getStartNode().isBubble()).max((b1, b2) -> Integer.compare( b1.getStartNode().getZoomLevel(), b2.getStartNode().getZoomLevel())).get().getStartNode().getZoomLevel();
        for(int i = 1; i < maxLevel; i++) {
            final int currentLevel = i;
            Set<Node> tempFiltered = bubbleCollection
                    .stream()
                    .filter(x ->  filtered.stream().filter(b -> b.getEndNode().equals(x)).count() == 0 &&
                            !containers.contains(x.getContainerId()) && x.getZoomLevel() == currentLevel && x.getContainerSize() <= threshold)
                    .collect(Collectors.toSet());
//            containers.clear();
            for (Node bubble : tempFiltered)
                containers.add(bubble.getId());
            filtered.addAll(tempFiltered);
        }
//        System.out.println("Filtered: ");
//        filtered.forEach(x -> System.out.println(x.getId()));
//        System.out.println(" TotalBubbles: " + bubbleCollection.size());
        return filtered;
    }

    private void findNewLinks(Collection<Node> filteredBubbles) {
        for(Node bubble: filteredBubbles){
            Node primitiveEnd = getPrimitiveEnd(bubble);
            Node prospectiveLink = getExistingAncestor(primitiveEnd, filteredBubbles, (n1, n2) -> n1.getStartNode().equals(n2));// && !n2.equals(n1.getEndNode())
            bubble.getLinks().clear();
            if(prospectiveLink != null && !prospectiveLink.equals(bubble)) {
                bubble.getLinks().add(prospectiveLink);
            }
            else {
                for (Node primLink : primitiveEnd.getLinks()) {
                    Node found = getExistingAncestor(primLink, filteredBubbles, (n1, n2) -> n1.getId() == n2.getContainerId());
                    if(found != null)
                        bubble.getLinks().add(found);
                }
            }
            System.out.print(linksToString(bubble.getId(), bubble.getLinks()));
        }
    }

    private Node getPrimitiveEnd(Node node) {
        if(node.getEndNode().isBubble())
            return getPrimitiveEnd(node.getEndNode());
        return node.getEndNode();
    }

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

    private String linksToString(int bId, Collection<Node> links){
        String result = "";
        for (Node n: links)
            result += bId + " -> " + n.getId() + ";\n";
        return result;
    }

    private NodeCollection listAsNodeCollection(Collection<Node> res) {
        NodeCollection collection = new NodeMap();
        for(Node node: res) {
            collection.put(node.getId(), node);
        }
        return collection;
    }

}
