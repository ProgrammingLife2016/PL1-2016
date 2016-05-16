package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.InputStream;

/**
 * Created by ravishivam on 15-5-16.
 */
public class BubbleMain {
    private static final double NANOSECONDS_PER_SECOND = 1000000000;

    public static void main(String[] args) {
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
        BubbleDetector detector = new BubbleDetector(nodeCollection);
        detector.findLevelBubbles(nodeCollection);
        for (int i = 0; i < detector.getBubbleBoundaries().size(); i++) {
                System.out.println("Id: " + i + " Bubble detected between: " + detector.getBubbleBoundaries().get(i).getStartNode() + " and " + detector.getBubbleBoundaries().get(i).getEndNode());
        }

        //1454
        BubbleCollapser collapser = new BubbleCollapser();
        collapser.collapseBubbles(detector.getBubbleBoundaries());
    }
}
