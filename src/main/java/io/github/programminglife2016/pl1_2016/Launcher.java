package io.github.programminglife2016.pl1_2016;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;
import io.github.programminglife2016.pl1_2016.server.api.querystrategies.DatabaseQueryStrategy;
import io.github.programminglife2016.pl1_2016.server.api.querystrategies.NoDatabaseQueryStrategy;
import io.github.programminglife2016.pl1_2016.server.api.querystrategies.QueryStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

/**
 * Reads the input and launches the server.
 */
public final class Launcher {
    private static final double NANOSECONDS_PER_SECOND = 1000000000.0;

    private Launcher() {
    }
    /**
     * Read the input data and starts the server on the provided port.
     * @param args in the form [port] [dataset] [database] (e.g. 8081 TB10 database)
     * @throws IOException thrown if the port is in use.
     * @throws SQLException thrown if the port is in use.
     */
    public static void main(String[] args) throws IOException, SQLException {
        int port = Integer.parseInt(args[0]);
        String dataset = args[1];
        QueryStrategy queryStrategy = getQueryStrategy(dataset, args[2].equals("database"));
        Server server = new RestServer(queryStrategy);
        server.startServer();
    }

    private static QueryStrategy getQueryStrategy(String dataset, boolean useDatabase) {
        System.out.println("Started loading.");
        long startTime = System.nanoTime();
        QueryStrategy queryStrategy = parseDataAndCreateQueryStrategy(dataset, useDatabase);
        long endTime = System.nanoTime();
        System.out.println(String.format("Loading time: %f s.", (endTime - startTime)
                / NANOSECONDS_PER_SECOND));
        return queryStrategy;
    }

    private static QueryStrategy parseDataAndCreateQueryStrategy(String dataset,
                                                                 boolean useDatabase) {
        InputStream is = Launcher.class.getResourceAsStream(
                String.format("/genomes/%s.gfa", dataset));
        InputStream positions = Launcher.class.getResourceAsStream(
                String.format("/genomes/%s.positions", dataset));
        InputStream metadata = Launcher.class.getResourceAsStream("/genomes/metadata.csv");
        SegmentParser segmentParser = new SegmentParser(positions, metadata);
        NodeCollection nodeCollection = segmentParser.parse(is);
        Map<String, Subject> subjects = segmentParser.getSubjects();
        if (useDatabase) {
            FetchDatabase fdb = new FetchDatabase();
            return new DatabaseQueryStrategy(fdb, nodeCollection, subjects);
        } else {
            return new NoDatabaseQueryStrategy(nodeCollection, subjects);
        }
    }
}

