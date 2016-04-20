package io.github.programminglife2016.pl1_2016.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A simple server that listens to /nodes at port 8080, outputting the message.
 */
public class BasicServer implements Server {
    public static final int PORT = 8080;

    private MessageHandler messageHandler;
    private HttpServer server;


    /**
     * Create a server that - once started - will initially output the message.
     *
     * @param message the initial message to be outputted
     */
    public BasicServer(String message) {
        this.messageHandler = new SuccessHandler(message);
    }

    /**
     * Change the message to the argument.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.messageHandler.setMessage(message);
    }

    /**
     * Start the server, listening to /nodes at the default port.
     * @throws IOException thrown if the port is in use
     */
    public void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/nodes", messageHandler);
        server.setExecutor(null);
        server.start();
    }

    /**
     * Stop the server (immediately).
     */
    public void stopServer() {
        server.stop(0);
    }
}
