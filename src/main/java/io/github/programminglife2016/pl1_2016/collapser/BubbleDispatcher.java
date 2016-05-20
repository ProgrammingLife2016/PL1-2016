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
//        levelCollection = new HashMap<>();
//        endToBubble = new HashMap<>();
        this.collection = collection;
        initDispatcher();
    }

    private void initDispatcher() {
        for (int i = 0; i < bubbleCollection.size(); i++) {
            Node bubble = bubbleCollection.get(i);
            bubble.setContainerSize(getBubbleSize(bubble));
//            int currlevel = bubble.getZoomLevel();
//            if (!levelCollection.containsKey(currlevel)) {
//                levelCollection.put(currlevel, new NodeMap());
            }
//            levelCollection.get(currlevel).put(bubble.getId(), bubble);
//        }
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
        List<Node> filtered = bubbleCollection.stream().filter(x -> x.getContainerSize() <= threshold).collect(Collectors.toList());
        List<Node> tempList = new ArrayList<>();
        for (Node bubble : filtered) {
            if (needReplace(bubble.getLinks(), filtered)) {
                Collection<Node> newlinks = new HashSet<>();
                for (Node endNodelink : bubble.getEndNode().getLinks()) {
                    Optional<Node> link = filtered.stream().filter(y -> y.getId() == endNodelink.getContainerId()).findFirst();
                    if(link.isPresent())
                        newlinks.add(link.get());
                }
                for (Node link : bubble.getLinks()) {
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
        }
        filtered.addAll(tempList);
        System.out.println(filtered);
        return listAsNodeCollection(filtered);
    }

    private List<Node> getMissingNodes(Collection<Node> links){
        return links.stream().filter(node -> bubbleCollection.contains(node) == false).collect(Collectors.toList());
    }

    private boolean needReplace(Collection<Node> links, List<Node> bubble) {
        for (Node link : links) {
            if (bubble.contains(link)) {
                continue;
            }
            return true;
        }
        return false;
    }
//    public NodeCollection getLevelBubbles(int level, int threshold) {
//        List<Node> res = new ArrayList<>(levelCollection.get(level).values());
//        List<Node> nextLevel = new ArrayList<>(levelCollection.get(level+1).values());
//
//        for(Node node : res) {
//            if (node.getContainerSize() > threshold) {
//                Node start = node.getStartNode();
//                for (Node startchildren : start.getLinks()) {
//                    int idcontainer = startchildren.getContainerId();
//
//                }
//
//                res.remove(node);
//            }
//        }
//        return listAsNodeCollection(res);
////        return levelCollection.get(level);
//    }

    private NodeCollection listAsNodeCollection(List<Node> res) {
        NodeCollection collection = new NodeMap();
        for(Node node: res) {
            collection.put(node.getId(), node);
        }
        return collection;
    }

}
