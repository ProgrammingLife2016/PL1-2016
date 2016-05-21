package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;

import java.io.IOException;
import java.io.InputStream;


public class BubbleMain {
    private static final double NANOSECONDS_PER_SECOND = 1000000000;

    public static void main(String[] args) throws IOException {
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/testGraphLSEE.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
//        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
//        collapser.collapseBubbles();
        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
//<<<<<<< HEAD
//        Coordinate coord = new Coordinate(0, 0);
//        coord = collapser.bubbles.get(0).position(coord, collapser.bubbles, null, 1);
//        for (Node bubble : dispatcher.bubbleCollection) {
//            bubble.setXY(bubble.getStartNode().getX(), bubble.getStartNode().getY());
//        }

        Server server = new RestServer(dispatcher.getLevelBubbles(0, 4));
//=======
//        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
//        collapser.collapseBubbles();
//        Coordinate coord = new Coordinate(0, 0);
//        coord = collapser.bubbles.get(0).position(coord, collapser.bubbles, null, 1);
//        for (Node bubble : dispatcher.bubbleCollection) {
//            bubble.setXY(bubble.getStartNode().getX(), bubble.getStartNode().getY());
//        }
//        for (Node node : nodeCollection.values()) {
//            Iterator<Node> linkIterator = node.getLinks().iterator();
//            while (linkIterator.hasNext()) {
//                Node nodeLink = linkIterator.next();
//                if (nodeLink.getX() < node.getX()) {
//                    linkIterator.remove();
//                }
//            }
//        }
//        Server server = new RestServer(nodeCollection);
//>>>>>>> origin/feature/graphparser
        server.startServer();
    }
}
