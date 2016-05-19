package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravishivam on 19-5-16.
 */
public class BubbleDispatcher {

    private List<Bubble> bubbleCollection;

    private List<NodeCollection> levelCollection;

    public BubbleDispatcher(NodeCollection collection) {
        BubbleCollapser collapser = new BubbleCollapser(collection);
        collapser.collapseBubbles();
        this.bubbleCollection = collapser.getBubbles();
        levelCollection = new ArrayList<>();
        initDispatcher();
    }

    private void initDispatcher() {
        for (int i = 0; i < bubbleCollection.size(); i++) {
            Bubble bubble = bubbleCollection.get(i);
            int currlevel = bubble.getLevel();
            if (levelCollection.get(i)==null) {
                levelCollection.set(currlevel, new NodeMap());
            }
            levelCollection.get(currlevel).put(bubble.getId(), bubble);
        }
    }

    public NodeCollection getLevelBubbles(int level) {
        return levelCollection.get(level);
    }
}
