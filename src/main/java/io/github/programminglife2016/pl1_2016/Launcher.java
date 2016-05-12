package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import io.github.programminglife2016.pl1_2016.parser.TreeNodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.parser.phylotree.PhyloGeneticTreeParser;
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
     * Read the input data and starts the server on the default port.
     * @param args ignored
     * @throws IOException thrown if the port is in use.
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        InputStream is = Launcher.class.getResourceAsStream("/genomes/TB10.gfa");
        NodeCollection nodeCollection = new SegmentParser().parse(is);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
        InputStream nwk = Launcher.class.getResourceAsStream("/genomes/TB10.nwk");
        TreeNodeCollection treeNodes = new PhyloGeneticTreeParser().parse(nwk);
        Server server = new RestServer(nodeCollection, treeNodes.getRoot());
        server.startServer();
    }
}

