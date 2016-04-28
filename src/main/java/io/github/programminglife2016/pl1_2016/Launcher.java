package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import io.github.programminglife2016.pl1_2016.parser.SimpleParser;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;
import io.github.programminglife2016.pl1_2016.server.Server;

import java.io.IOException;
import java.io.InputStream;

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
        InputStream is = Launcher.class.getResourceAsStream("/genomes/TB10_200.gfa");
        JsonSerializable jsonSerializable = new SimpleParser().parse(is);
        Server server = new RestServer(jsonSerializable);
        server.startServer();
    }
}

