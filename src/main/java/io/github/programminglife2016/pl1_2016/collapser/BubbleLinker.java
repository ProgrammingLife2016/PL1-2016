package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Linker class creates links between the bubbles of the same level, and sets level to the bubbles if it is undefined.
 * Created by Kamran Tadzjibov on 22.05.2016.
 */
public class BubbleLinker {
    List<Node> bubbles;
    int lastId;
    int bubblesListSize;

    /**
     *
     * @param bubbles list of all collapsedSegments bubbles
     */
    public BubbleLinker(List<Node> bubbles){
        this.bubbles = bubbles;
        lastId = bubbles.stream().max((b1, b2) -> Integer.compare( b1.getId(), b2.getId())).get().getId();
        bubblesListSize = bubbles.size();
        setCorrectLevelsToNodes();
    }

    private void setCorrectLevelsToNodes(){
        setLevelsToNestedNodes();
        setLevelsToSingleNodes();
        while(needZoomEqualizing())
            setLevelsToNestedNodes();

        for(int i = 0; i < bubbles.size(); i++)
            if(bubbles.get(i).getZoomLevel() == -1)
                throw new RuntimeException("Not single not nested zoomlevel = " + bubbles.get(i).getZoomLevel() + ": " + bubbles.get(i));
    }

    private void setLevelsToNestedNodes(){
        for(int i = 0; i < bubbles.size(); i++){
            if(bubbles.get(i).getContainer().size() > 0){
                int level = bubbles.get(i).getContainer().stream().max((b1, b2) -> Integer.compare( b1.getZoomLevel(), b2.getZoomLevel())).get().getZoomLevel();
                level = level >= bubbles.get(i).getStartNode().getZoomLevel() ? level :bubbles.get(i).getStartNode().getZoomLevel();
                level = level >= bubbles.get(i).getEndNode().getZoomLevel() ? level :bubbles.get(i).getEndNode().getZoomLevel();
                if(level <= 1)
                    level = bubbles.get(i).getZoomLevel()+1;
                deepLevelModifier(bubbles.get(i), level);
            }
        }
    }

    private void setLevelsToSingleNodes(){
        for(int i = 0; i < bubbles.size(); i++){
            if(bubbles.get(i).getZoomLevel() == -1 &&  bubbles.get(i).getContainer().size() == 0) {
                bubbles.get(i).setZoomLevel(1);
                deepLevelModifier(bubbles.get(i), 2);
            }
        }
    }

    private boolean needZoomEqualizing(){
        for(int i = 0; i < bubbles.size(); i++){
            if(bubbles.get(i).getContainer().size() > 0){
                int level = bubbles.get(i).getContainer().get(0).getZoomLevel();
                for(Node node : bubbles.get(i).getContainer()){
                    if(node.getZoomLevel() != level)
                        return true;
                }
                if(bubbles.get(i).getStartNode().getZoomLevel() != level || bubbles.get(i).getEndNode().getZoomLevel() != level)
                    return true;
            }
        }
        return false;
    }

    private void deepLevelModifier(Node node, int level){
        if(!node.getContainer().isEmpty()) {
            node.getContainer().forEach(x -> {x.setZoomLevel(level); deepLevelModifier(x, level+1);});
        }
        if(node.getStartNode().getId() != node.getId()){
            node.getStartNode().setZoomLevel(level);
            deepLevelModifier(node.getStartNode(), level+1);
            node.getEndNode().setZoomLevel(level);
            deepLevelModifier(node.getEndNode(), level+1);
        }

    }

    /**
     * Connect all bubbles per level with each other to get representative graph.
     */
    public void addLinks(){
        for (int i = 0; i < bubblesListSize; i++){
            addForwardLinks(bubbles.get(i));
        }
    }



    private void addLinkToBubble(Node bubble){
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
