//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.server.api;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test requests to the REstServer.
 */
public class RestServerTest {
    private RestServer restServer;

    /**
     * Start the server, that only returns the message "{"hi": 2}" upon request.
     *
     * @throws InterruptedException thrown when the sleep is interrupted.
     */
    @Before
    public void setUp() throws InterruptedException {
        NodeCollection nodeCollection = mock(NodeCollection.class);
        when(nodeCollection.toJson()).thenReturn("{\"hi\": 2}");
        restServer = new RestServer(nodeCollection);
        RestServerThread restServerThread = new RestServerThread(restServer);
        new Thread(restServerThread).start();
        // Sleep such that the server has a chance to actually start.
        Thread.sleep(500);
    }

    /**
     * Stop the server.
     */
    @After
    public void breakDown() {
        restServer.stopServer();
    }

    /**
     * Send a single GET request to the server.
     *
     * @throws IOException thrown when the TCP connection refuses
     */
    @Test
    public void testSingleRequestToServer() throws IOException {
        String ret = IOUtils.toString(new URL(String.format("http://localhost:%d/api/nodes",
                RestServer.PORT)), "UTF-8");
        assertEquals("{\"hi\": 2}", ret);
    }

    /**
     * A Thread that contains a RestServer. Used because the server is blocking.
     */
    public static class RestServerThread implements Runnable {
        private RestServer restServer;

        /**
         * Construct the RestServer thread.
         *
         * @param restServer RestServer to eventually start
         */
        public RestServerThread(RestServer restServer) {
            this.restServer = restServer;
        }

        /**
         * Start the RestServer.
         */
        public void run() {
            try {
                restServer.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
