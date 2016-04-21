package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import io.github.programminglife2016.pl1_2016.parser.Parser;
import io.github.programminglife2016.pl1_2016.parser.SimpleParser;
import io.github.programminglife2016.pl1_2016.server.BasicServer;
import io.github.programminglife2016.pl1_2016.server.Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
//        Parser parser = new Parser() {
//            public JsonSerializable parse(InputStream inputStream) {
//                return new JsonSerializable() {
//                    public String toJson() {
//                        return "Hello, world!";
//                    }
//                };
//            }
//        };
//        Server server = new BasicServer(parser.parse(null).toJson());
//        server.startServer();
        SimpleParser parser = new SimpleParser();
        parser.parse(new FileInputStream("data/TB10_.gfa"));
    }
}
