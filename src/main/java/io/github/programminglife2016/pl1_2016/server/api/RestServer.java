package io.github.programminglife2016.pl1_2016.server.api;

import com.sun.net.httpserver.HttpServer;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.querystrategies.QueryStrategy;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A RESTful API server.
 */
public class RestServer implements Server {
    public static final int DEFAULT_PORT = 8081;

    private HttpServer server;
    private int port = DEFAULT_PORT;
    private QueryStrategy queryStrategy;

    /**
     * Construct a RestServer, that passes nodeCollection to the appropriate API queries.
     *
     * @param queryStrategy which API queries to use
     */
    public RestServer(QueryStrategy queryStrategy) {
        this.queryStrategy = queryStrategy;
    }

    /**
     * Construct a RestServer, that uses QueryStrategy for the appropriate API queries.
     *
     * @param port TCP port to run the server on
     * @param queryStrategy which API queries to use
     */
    public RestServer(int port, QueryStrategy queryStrategy) {
        this.port = port;
        this.queryStrategy = queryStrategy;
    }

    /**
     * Start the server.
     *
     * @throws IOException thrown if the server cannot obtain resources (e.g. ports).
     */
    public void startServer() throws IOException {
        ApiHandler apiHandler = new RestHandler();
        queryStrategy.addQueries(apiHandler);
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
