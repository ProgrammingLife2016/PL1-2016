package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.parser.phylotree.PhyloGeneticTreeParser;
import io.github.programminglife2016.pl1_2016.parser.phylotree.TreeNodeCollection;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;

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
//        for (int i = 1; i < nodeCollection.size(); i++) {
//            if(nodeCollection.get(i).getGenomes().size() == 11){
//                System.out.println(nodeCollection.get(i).getId() + ": " + nodeCollection.get(i).getGenomes());
//            }
//        }
        detector.findLevelBubbles(nodeCollection);
    }
}
