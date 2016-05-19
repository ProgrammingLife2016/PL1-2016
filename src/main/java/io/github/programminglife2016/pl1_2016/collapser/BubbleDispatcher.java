package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BubbleDispatcher {

    public List<Node> bubbleCollection;

    private Map<Integer, NodeCollection> levelCollection;

    public BubbleDispatcher(NodeCollection collection) {
        BubbleCollapser collapser = new BubbleCollapser(collection);
        collapser.collapseBubbles();
        this.bubbleCollection = collapser.getBubbles();
        levelCollection = new HashMap<>();
        initDispatcher();
    }

    private void initDispatcher() {
        for (int i = 0; i < bubbleCollection.size(); i++) {
            Node bubble = bubbleCollection.get(i);
//            System.out.println("reached");
//            bubble.setContainerSize(getBubbleSize(bubble));
//            System.out.println("reached");
            int currlevel = bubble.getZoomLevel();
            if (!levelCollection.containsKey(currlevel)) {
                levelCollection.put(currlevel, new NodeMap());
            }
            levelCollection.get(currlevel).put(bubble.getId(), bubble);
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

    public NodeCollection getLevelBubbles(int level) {
        return levelCollection.get(level);
    }
}
