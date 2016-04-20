//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the BasicServer class
 */
public class BasicServerTest {
    private BasicServer basicServer;

    /**
     * Start the server, that only returns the message "hi" upon request.
     *
     * @throws InterruptedException thrown when the sleep is interrupted.
     */
    @Before
    public void setUp() throws InterruptedException {
        basicServer = new BasicServer("hi");
        BasicServerThread basicServerThread = new BasicServerThread(basicServer);
        new Thread(basicServerThread).start();
        // Sleep such that the server has a chance to actually start.
        Thread.sleep(500);
    }

    /**
     * Stop the server.
     */
    @After
    public void breakDown() {
        basicServer.stopServer();
    }

    /**
     * Send a single GET request to the server.
     *
     * @throws IOException thrown when the TCP connection refuses
     */
    @Test
    public void testSingleRequestToServer() throws IOException {
        String ret = httpRequest(new URL(String.format("http://localhost:%d/nodes",
                BasicServer.PORT)));
        assertEquals("hi", ret);
    }

    /**
     * Issue multiple GET requests to the server. Test whether changing the message is propagated.
     *
     * @throws IOException thrown when the TCP connection refuses
     */
    @Test
    public void testMultipleRequestsWithChangingMessages() throws IOException {
        String ret = httpRequest(new URL(String.format("http://localhost:%d/nodes",
                BasicServer.PORT)));
        assertEquals("hi", ret);

        basicServer.setMessage("hi2");
        String ret2 = httpRequest(new URL(String.format("http://localhost:%d/nodes",
                BasicServer.PORT)));
        assertEquals("hi2", ret2);
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
     * A Thread that contains a BasicServer. Used because the server is blocking.
     */
    private static class BasicServerThread implements Runnable {
        private BasicServer basicServer;

        BasicServerThread(BasicServer basicServer) {
            this.basicServer = basicServer;
        }

        public void run() {
            try {
                basicServer.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
