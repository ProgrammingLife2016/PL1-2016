package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.parser.*;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;
import io.github.programminglife2016.pl1_2016.server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Reads the input and launches the server.
 */
public final class Launcher {
    private Launcher() {
    }
    /**
     * Read the input data and starts the server on the default port.
     * @param args ignored
     * @throws IOException thrown if the port is in use.
     */
    public static void main(String[] args) throws IOException {
        InputStream is = Launcher.class.getResourceAsStream("/genomes/TB328.gfa");
        NodeCollection nodeCollection = new SimpleParser().parse(is);
        InputStream nwk = Launcher.class.getResourceAsStream("/genomes/340tree.rooted.TKK.nwk");
        TreeNodeCollection treeNodeCollection = new PhyloGeneticTreeParser().parse(nwk);
        Server server = new RestServer(nodeCollection, treeNodeCollection.getRoot());
        server.startServer();
    }
}

