package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.parser.Parser;
import io.github.programminglife2016.pl1_2016.parser.SimpleParser;
import io.github.programminglife2016.pl1_2016.server.BasicServer;
import io.github.programminglife2016.pl1_2016.server.Server;

import java.io.IOException;

/**
 * Reads the input and launches the server.
 */
public final class Launcher {
    private Launcher() {
    }
    /**
     * Read the input data and startsthe server on the default port.
     * @param args ignored
     * @throws IOException thrown if the port is in use.
     */
    public static void main(String[] args) throws IOException {
        Parser parser = new SimpleParser();
        String json = parser.parse(Launcher.class.getResourceAsStream("/genomes/TB10_.gfa")).toJson();
        Server server = new BasicServer(json);
        server.startServer();
    }
}

