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
        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/testGraph.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
        NodeCollection testCol = dispatcher.getLevelBubbles(0,4);

        PositionHandler ph = new PositionHandler();
        Server server = new RestServer(ph.calculatePositions(ph.calculatePositions(testCol)));
        server.startServer();
    }
}
