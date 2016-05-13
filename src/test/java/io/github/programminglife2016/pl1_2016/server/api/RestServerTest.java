//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.server.api;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

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
        restServer = new RestServer(nodeCollection, null);
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
        String ret = httpRequest(new URL(String.format("http://localhost:%d/api/nodes",
                RestServer.PORT)));
        assertEquals("{\"hi\": 2}", ret);
    }

    /**
     * Send a single GET request to a static file.
     *
     * @throws IOException thrown when the TCP connection refuses
     */
    @Test
    public void testSingleRequestToStaticFile() throws IOException {
        String ret = httpRequest(new URL(String.format("http://localhost:%d/static/statictestfile",
                RestServer.PORT)));
        assertEquals("hello-from-file", ret.trim());
    }

    /**
     * Issue a simple GET request to the url, and return the response body.
     *
     * @param url destination url
     * @return response body
     * @throws IOException thrown when the connection refuses
     */
    private String httpRequest(URL url) throws IOException {
        InputStream is = url.openStream();
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        if (s.hasNext()) {
            return s.next();
        } else {
            return "";
        }
    }

    /**
     * A Thread that contains a RestServer. Used because the server is blocking.
     */
    private static class RestServerThread implements Runnable {
        private RestServer restServer;

        RestServerThread(RestServer restServer) {
            this.restServer = restServer;
        }

        public void run() {
            try {
                restServer.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
