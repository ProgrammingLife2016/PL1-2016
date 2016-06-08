//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.server.api;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.querystrategies.NoDatabaseQueryStrategy;
import io.github.programminglife2016.pl1_2016.server.api.querystrategies.QueryStrategy;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
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
        QueryStrategy queryStrategy = new NoDatabaseQueryStrategy(nodeCollection, null);
        restServer = new RestServer(queryStrategy);
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
