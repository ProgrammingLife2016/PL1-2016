package io.github.programminglife2016.pl1_2016.server;

import java.io.IOException;

/**
 * A server that listens to user requests.
 */
public interface Server {
    /**
     * Start the server.
     *
     * @throws IOException thrown if the server cannot obtain resources (e.g. ports).
     */
    void startServer() throws IOException;
}
