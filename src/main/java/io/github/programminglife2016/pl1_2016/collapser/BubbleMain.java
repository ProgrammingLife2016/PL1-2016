package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.GraphvizParser;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public class BubbleMain {
    public static void main(String[] args) throws IOException {

//        InputStream segis = BubbleMain.class.getClass().getResourceAsStream("/genomes/tb10_interestingpart.gfa");
//        InputStream dotis = BubbleMain.class.getClass().getResourceAsStream("/genomes/tb10_interestingpart.txt");
        InputStream segis = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
//        InputStream dotis = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.txt");

//        NodeCollection nodeCollection = (new GraphvizParser(new SegmentParser().parse(segis))).parse(dotis);
        NodeCollection nodeCollection = new SegmentParser().parse(segis);
        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
        nodeCollection = dispatcher.getThresholdedBubbles(1);
        nodeCollection.recalculatePositions();

//        System.out.println(nodeCollection.size());
//        Server server = new RestServer(dispatcher.getThresholdedBubbles(0,7));
////        Server server = new RestServer(nodeCollection);
//        server.startServer();
    }
}