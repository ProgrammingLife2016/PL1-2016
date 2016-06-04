package io.github.programminglife2016.pl1_2016.server.api;

import com.sun.net.httpserver.HttpServer;
import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetLineageApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetStaticFileApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetThresholdedBubblesApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.IndividualSegmentDataApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.ReturnAllNodesApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.RootIndexApiQuery;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * A RESTful API server.
 */
public class RestServer implements Server {
    public static final int DEFAULT_PORT = 8081;

    private HttpServer server;
    private NodeCollection nodeCollection;
    private Map<String, Subject> subjects;
    private int port = DEFAULT_PORT;
    private FetchDatabase fdb;
    /**
     * Construct a RestServer, that passes nodeCollection to the appropriate API queries.
     *
     * @param nodeCollection NodeCollection to be used for API queries
     */
    public RestServer(NodeCollection nodeCollection) {
        this.nodeCollection = nodeCollection;
    }

    /**
     * Construct a RestServer, that passes nodeCollection to the appropriate API queries.
     *
     * @param port TCP port to run the server on
     * @param nodeCollection NodeCollection to be used for API queries
     */
    public RestServer(int port, NodeCollection nodeCollection) {
        this.nodeCollection = nodeCollection;
        this.port = port;
    }

    /**
     * Construct a RestServer, that passes nodeCollection to the appropriate API queries.
     * This constructor also works with GetLineageApiQuery.
     *
     * @param port TCP port to run the server on
     * @param nodeCollection NodeCollection to be used for API queries
     * @param subjects metadata information
     */
    public RestServer(int port, FetchDatabase fdb, NodeCollection nodeCollection, Map<String, Subject> subjects) {
        this.nodeCollection = nodeCollection;
        this.port = port;
        this.subjects = subjects;
        this.fdb = fdb;
    }

    /**
     * Start the server.
     *
     * @throws IOException thrown if the server cannot obtain resources (e.g. ports).
     */
    public final void startServer() throws IOException {
        ApiHandler apiHandler = new RestHandler();
        apiHandler.addQuery(new ReturnAllNodesApiQuery(fdb));
        apiHandler.addQuery(new GetStaticFileApiQuery());
        apiHandler.addQuery(new RootIndexApiQuery());
        apiHandler.addQuery(new IndividualSegmentDataApiQuery(nodeCollection));
        if (subjects != null) {
            apiHandler.addQuery(new GetThresholdedBubblesApiQuery(fdb));
            apiHandler.addQuery(new GetLineageApiQuery(fdb));
        }
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", apiHandler);
        server.setExecutor(null);
        server.start();
    }

    /**
     * Stop the server.
     */
    public final void stopServer() {
        server.stop(0);
    }
}
