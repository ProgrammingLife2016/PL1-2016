package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;


public class BubbleMain {
    private static final double NANOSECONDS_PER_SECOND = 1000000000;

    public static void main(String[] args) throws IOException {
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10_200.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
//        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
//        collapser.collapseBubbles();

//        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
        collapser.collapseBubbles();
        Coordinate coord = new Coordinate(0, 0);
        coord = collapser.bubbles.get(0).position(coord, collapser.bubbles, null, 1);
        for (Node bubble : collapser.bubbles) {
            bubble.setXY(bubble.getStartNode().getX(), bubble.getStartNode().getY());
        }
        for (Node node : nodeCollection.values()) {
            Iterator<Node> linkIterator = node.getLinks().iterator();
            while (linkIterator.hasNext()) {
                Node nodeLink = linkIterator.next();
                if (nodeLink.getX() < node.getX()) {
                    linkIterator.remove();
                }
            }
        }
        NodeCollection nodeCollection1 = new NodeMap();
        for (Node node : collapser.bubbles) {
            nodeCollection1.put(node.getId(), node);
        }
        Server server = new RestServer(nodeCollection1);
        server.startServer();
    }
}
