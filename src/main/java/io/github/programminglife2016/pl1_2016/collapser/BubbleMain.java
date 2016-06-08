package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class to view bubbling changes
 */
public final class BubbleMain {
    private static final int THRESHOLD = 100;

    private BubbleMain() { }

    /**
     * Execute bubbling
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB328.gfa");
        InputStream mt = BubbleMain.class.getClass().getResourceAsStream("/genomes/metadata.csv");
        InputStream pos = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.positions");
        NodeCollection nodeCollection = new SegmentParser(pos, mt).parse(is);

        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
        collapser.collapseBubbles();
//        BubbleDetector detector = new BubbleDetector(nodeCollection);
//        detector.findMultiLevelBubbles();
        for (Node node : collapser.getBubbles()) {
            if (node.getStartNode().getId() == node.getEndNode().getId())
                System.out.println(node);
        }
    }
}
