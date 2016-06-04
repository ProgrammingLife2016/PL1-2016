package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.IOException;
import java.io.InputStream;


public final class BubbleMain {

    private BubbleMain(){}

    public static void main(String[] args) throws IOException {
        InputStream segis = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(segis);

        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
        nodeCollection = dispatcher.getThresholdedBubbles(1);
        nodeCollection.recalculatePositions();
    }
}