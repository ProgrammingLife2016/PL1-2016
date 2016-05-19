package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class BubbleMain {
    private static final double NANOSECONDS_PER_SECOND = 1000000000;

    public static void main(String[] args) throws IOException {
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
        Node firstNode = dispatcher.getLevelBubbles(1).get(8729);
        firstNode.getLinks().clear();
        firstNode.getLinks().add(dispatcher.getLevelBubbles(1).get(8730));
        for (int i = 0; i < dispatcher.bubbleCollection.size(); i++) {
            Node bubble = dispatcher.bubbleCollection.get(i);
            if (bubble.getLinks().contains(bubble)) {
                bubble.getLinks().remove(bubble);
                try {
                    bubble.getLinks().add(dispatcher.bubbleCollection.get(i + 1));
                } catch (IndexOutOfBoundsException e) {
                    
                }
            }
        }
        Coordinate coord = new Coordinate(0, 0);
        coord = dispatcher.getLevelBubbles(1).get(8730).position(coord, dispatcher.bubbleCollection, null, 1);
        for (Node node : nodeCollection.values()) {
            Iterator<Node> linkIterator = node.getLinks().iterator();
            while (linkIterator.hasNext()) {
                Node nodeLink = linkIterator.next();
                if (nodeLink.getX() < node.getX()) {
                    linkIterator.remove();
                }
            }
        }
        Server server = new RestServer(nodeCollection);
        server.startServer();
    }
}
