package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;
import io.github.programminglife2016.pl1_2016.server.Server;

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
     * Read the input data and starts the server on the provided port.
     * @param args in the form [port] [dataset] (e.g. 8081 TB10)
     * @throws IOException thrown if the port is in use.
     */
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        String dataset = args[1];
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        InputStream is = Launcher.class.getResourceAsStream(
                String.format("/genomes/%s.gfa", dataset));
        InputStream positions = Launcher.class.getResourceAsStream(
                String.format("/genomes/%s.positions", dataset));
        InputStream metadata = Launcher.class.getResourceAsStream("/genomes/metadata.csv");
        NodeCollection nodeCollection = new SegmentParser(positions, metadata).parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
        Server server = new RestServer(port, nodeCollection);
        server.startServer();
    }
}

