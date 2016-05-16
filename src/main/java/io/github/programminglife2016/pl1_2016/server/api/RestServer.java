package io.github.programminglife2016.pl1_2016.server.api;

import com.sun.net.httpserver.HttpServer;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.phylotree.TreeNode;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.queries.*;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A RESTful API server.
 */
public class RestServer implements Server {
    public static final int PORT = 8081;

    private HttpServer server;
    private NodeCollection nodeCollection;
    private TreeNode rootNode;

    public RestServer(NodeCollection nodeCollection, TreeNode rootNode) {
        this.nodeCollection = nodeCollection;
        this.rootNode = rootNode;
    }

    /**
     * Start the server.
     *
     * @throws IOException thrown if the server cannot obtain resources (e.g. ports).
     */
    public void startServer() throws IOException {
        ApiHandler apiHandler = new RestHandler();
        apiHandler.addQuery(new ReturnAllNodesApiQuery(nodeCollection));
        apiHandler.addQuery(new GetStaticFileApiQuery());
        apiHandler.addQuery(new RootIndexApiQuery());
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", apiHandler);
        server.setExecutor(null);
        server.start();
    }

    /**
     * Stop the server.
     */
    public void stopServer() {
        server.stop(0);
    }
}
