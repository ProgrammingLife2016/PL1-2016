package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Class to view bubbling changes
 */
public final class BubbleMain {
    private static final int THRESHOLD = 100;

    private BubbleMain() { }

    /**
     * Execute bubbling
     * @param args arguments.
     * @throws IOException thrown when reading the files fails.
     */
    public static void main(String[] args) throws IOException {
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        InputStream mt = BubbleMain.class.getClass().getResourceAsStream("/genomes/metadata.csv");
        InputStream pos = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.positions");
        NodeCollection nodeCollection = new SegmentParser(pos, mt).parse(is);

        //=======================================

//        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
//        NodeCollection nodes = dispatcher.getThresholdedBubbles(1);
//        for (Node node : nodes.values()) {
//            node.getLinks().forEach(x -> System.out.println(node.getId() + " -> " + x.getId()));
//        }

        //=======================================

//        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
//        collapser.collapseBubbles();
//        for (Node node : collapser.getBubbles()) {
//            System.out.println(node.getStartNode().getId());
//        }

        //=======================================

        BubbleDetector detector = new BubbleDetector(nodeCollection);
        detector.findMultiLevelBubbles();
        for (Node node : detector.getBubbleBoundaries()) {
            System.out.println(node.getEndNode().getId());
        }
    }
}
