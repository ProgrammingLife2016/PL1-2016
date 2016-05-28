package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.IOException;
import java.io.InputStream;


public class BubbleMain {
    private static final double NANOSECONDS_PER_SECOND = 1000000000;

    public static void main(String[] args) throws IOException {
//        InputStream segis = BubbleMain.class.getClass().getResourceAsStream("/genomes/tb10_interestingpart.gfa");
//        InputStream dotis = BubbleMain.class.getClass().getResourceAsStream("/genomes/tb10_interestingpart.txt");
//        InputStream segis = BubbleMain.class.getClass().getResourceAsStream("/genomes/output.gfa");
//        InputStream dotis = BubbleMain.class.getClass().getResourceAsStream("/genomes/output.txt");

//        NodeCollection nodeCollection = (new GraphvizParser(new SegmentParser().parse(segis))).parse(dotis);
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
//        BubbleDetector detector = new BubbleDetector(nodeCollection);
//        detector.findMultiLevelBubbles();
//        List<Node> list = detector.getBubbleBoundaries();
//        for (Node node: list) {
//            System.out.println(node.getStartNode().getId() + " " + node.getEndNode().getId() + " " + node.getZoomLevel());
//        }
        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
        nodeCollection = dispatcher.getLevelBubbles(0,4);
//        for(Map.Entry<Integer, Node> entry : nodeCollection.entrySet()) {
//            Node node = entry.getValue();
//            int x = (node.getStartNode().getX() + node.getEndNode().getX())/2;
//            int y = (node.getStartNode().getY() + node.getEndNode().getY())/2;
//            nodeCollection.get(entry.getKey()).setXY(x, y);
//        }
//
//        System.out.println(nodeCollection.size());
//        Server server = new RestServer(dispatcher.getLevelBubbles(0,7));
////        Server server = new RestServer(nodeCollection);
//        server.startServer();
    }
}
