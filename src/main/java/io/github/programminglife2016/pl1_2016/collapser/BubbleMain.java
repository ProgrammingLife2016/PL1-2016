package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class BubbleMain {
    public static void main(String[] args) throws IOException {
        InputStream segis = BubbleMain.class.getClass().getResourceAsStream("/genomes/tb10_interestingpart.gfa");
        InputStream dotis = BubbleMain.class.getClass().getResourceAsStream("/genomes/tb10_interestingpart.txt");
        NodeCollection nodeCollection = new SegmentParser().parse(segis);
        BubbleDetector detector = new BubbleDetector(nodeCollection);
        detector.findMultiLevelBubbles();
        List<Node> list = detector.getBubbleBoundaries();
        for (Node node: list) {
            System.out.println(node.getStartNode().getId() + " " + node.getEndNode().getId() + " " + node.getZoomLevel());
        }
//        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
//        nodeCollection = dispatcher.getLevelBubbles(0,4);
//        Server server = new RestServer(nodeCollection);
//        server.startServer();
    }
}
