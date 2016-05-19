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


/**
 * Created by ravishivam on 15-5-16.
 */
public class BubbleMain {
    private static final double NANOSECONDS_PER_SECOND = 1000000000;

    public static void main(String[] args) throws IOException {
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/testGraph.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
//        Node node = dispatcher.getLevelBubbles(1).get(9000);
//        System.out.println(node);
        Node firstNode = dispatcher.getLevelBubbles(1).get(16);
        firstNode.getLinks().clear();
        firstNode.getLinks().add(dispatcher.getLevelBubbles(1).get(17));
        Coordinate coord = new Coordinate(0, 0);
        List<Node> aziList = new ArrayList<>(dispatcher.getLevelBubbles(1).values());
        aziList.addAll(dispatcher.getLevelBubbles(2).values());
        for (int i = 0; i <= 4; i++) {
            Node node = aziList.get(i);
            coord = node.getStartNode().position(coord, node.getEndNode());
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
        Server server = new RestServer(nodeCollection);
        server.startServer();
    }
}
