package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.collapser.BubbleDispatcher;
import io.github.programminglife2016.pl1_2016.collapser.PositionHandler;
import io.github.programminglife2016.pl1_2016.collapser.PositionManager;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads the input and launches the server.
 */
public final class Launcher {
    private static final double NANOSECONDS_PER_SECOND = 1000000000.0;
    private Launcher() {
    }
    /**
     * Read the input data and starts the server on the default port.
     * @param args ignored
     * @throws IOException thrown if the port is in use.
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        InputStream is = Launcher.class.getResourceAsStream("/genomes/testGraph.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
        PositionManager positioner = new PositionHandler();
        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
//        System.out.println(nodeCollection.size());
//        Server server = new RestServer(positioner.calculatePositions());
        Server server = new RestServer(positioner.calculatePositions(dispatcher.getThresholdedBubbles(4)));
        server.startServer();
    }
}

