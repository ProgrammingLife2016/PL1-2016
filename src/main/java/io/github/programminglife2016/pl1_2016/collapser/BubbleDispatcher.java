package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;

import java.util.*;
import java.util.stream.Collectors;


public class BubbleDispatcher {

    public List<Node> bubbleCollection;

    private HashMap<Node, Node> endToBubble;

//    private Map<Integer, NodeCollection> levelCollection;

    private NodeCollection collection;

    public BubbleDispatcher(NodeCollection collection) {
        BubbleCollapser collapser = new BubbleCollapser(collection);
        collapser.collapseBubbles();
        this.bubbleCollection = collapser.getBubbles();
//        System.out.println("Collapsed:");
//        for (Node bubble : bubbleCollection) {
//            System.out.println("ID: " + bubble.getId() + "  Container: " + bubble.getContainer());
//        }
        this.collection = collection;
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
        Set<Node> filtered = bubbleCollection.stream().filter(x -> x.getContainerSize() <= threshold).collect(Collectors.toSet());
        int lastId = bubbleCollection.stream().max((b1, b2) -> Integer.compare( b1.getId(), b2.getId())).get().getId();
        Set<Node> tempList = new HashSet<>();
        Node tempBubble;
        for (Node bubble : filtered) {
            if (needReplace(bubble.getLinks(), filtered)) {
                Collection<Node> newlinks = new HashSet<>();
                for (Node endNodelink : bubble.getEndNode().getLinks()) {
//                    Optional<Node> link = filtered.stream().filter(y -> y.getId() == endNodelink.getContainerId()).findFirst();
//                    if(link.isPresent())
                    lastId++;
                    tempBubble = Bubble.getBestParentNode(lastId, endNodelink, filtered, bubble.getZoomLevel(), true);
                            //new Bubble(lastId, bubble.getZoomLevel(), (Segment) endNodelink);
                    tempBubble.getLinks().clear();
                    Set<Node> tbl = new HashSet<>();
//                    tbl.retainAll(endNodelink.getLinks());
                    for(Node link : bubble.getLinks())
                        tbl.addAll(link.getLinks());
                    tempBubble.getLinks().addAll(tbl);//getContainer(bubble.getLinks(), endNodelink.getLinks()));
                    newlinks.add(tempBubble);//link.get());

                }
                for (Node link : bubble.getStartNode().getLinks()) {
                    if(link instanceof Bubble) {
                        for (Node linkSegment : link.getContainer()){
                            linkSegment.getLinks().clear();
                            for(Node oldLink : bubble.getLinks()) {
                                linkSegment.getLinks().addAll(oldLink.getLinks());
                            }
                        }
                        newlinks.addAll(link.getContainer());
                    }
                }
                if(!newlinks.isEmpty()) {
                    bubble.getLinks().clear();
                    bubble.getLinks().addAll(newlinks);
                }
            }
            List<Node> intersection = new ArrayList<>(filtered);
            intersection.retainAll(bubble.getLinks());
            if(intersection.size() == 0){
                List<Node> x = getMissingNodes(bubble.getLinks());
                tempList.addAll(x);
            }
//            System.out.println("Id: " + bubble.getId() + " Contains:" + bubble.getContainer().stream().map(x -> x.getId()).collect(Collectors.toList()));
//            System.out.println("Links: " + bubble.getLinks().stream().collect(Collectors.toList()));
//            System.out.println("StartNode: " + bubble.getStartNode());
//            System.out.println("EndNode: " + bubble.getEndNode());
//            System.out.println();

        }
        filtered.addAll(tempList);
//        for(Node bubble: filtered)
//            System.out.print(linksToString(bubble.getId(), bubble.getLinks()));

        System.out.println("Filtered: " + filtered + " TotalBubbles: " + bubbleCollection.size());
        return listAsNodeCollection(filtered);
    }

    /**
     * Returns existing bubble-containers of the given leafs
     * TODO: fix deeper for levels
     * @param bubbleLinks
     * @param leafLinks
     * @return
     */
    private Set<Node> getContainer(Collection<Node> bubbleLinks, Collection<Node> leafLinks){
        Set<Node> result = new HashSet<>();
        for(Node bubbleL : bubbleLinks){
            for(Node leafL : leafLinks) {
                if (bubbleL.getContainer().stream().filter(x -> x.getId() == leafL.getId()).count() > 0)
                    result.add(bubbleL);
            }
        }
        return result;
    }

    private String linksToString(int bId, Collection<Node> links){
        String result = "";
        for (Node n: links)
            result += bId + " -> " + n.getId() + "\n";
        return result;
    }

    private List<Node> getMissingNodes(Collection<Node> links){
        return links.stream().filter(node -> bubbleCollection.contains(node) == false).collect(Collectors.toList());
    }

    private boolean needReplace(Collection<Node> links, Collection<Node> bubble) {
        for (Node link : links) {
            if (bubble.contains(link)) {
                continue;
            }
            return true;
        }
        return false;
    }

    private NodeCollection listAsNodeCollection(Collection<Node> res) {
        NodeCollection collection = new NodeMap();
        for(Node node: res) {
            collection.put(node.getId(), node);
        }
        return collection;
    }

}
