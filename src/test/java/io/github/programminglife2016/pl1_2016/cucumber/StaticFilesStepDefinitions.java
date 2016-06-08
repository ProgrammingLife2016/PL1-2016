//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.cucumber;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;
import io.github.programminglife2016.pl1_2016.server.api.RestServerTest;
import io.github.programminglife2016.pl1_2016.server.api.querystrategies.NoDatabaseQueryStrategy;
import io.github.programminglife2016.pl1_2016.server.api.querystrategies.QueryStrategy;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Step definitions to test static file functionality of the server.
 */
public class StaticFilesStepDefinitions {
    private static RestServer restServer;
    private static HttpURLConnection connection;

    private static void setRestServer(RestServer restServer) {
        StaticFilesStepDefinitions.restServer = restServer;
    }

    private static RestServer getRestServer() {
        return restServer;
    }

    private static void setConnection(HttpURLConnection connection) {
        StaticFilesStepDefinitions.connection = connection;
    }

    private static HttpURLConnection getConnection() {
        return connection;
    }

    /**
     * Start the server in a thread.
     *
     * @throws InterruptedException thrown by Thread.sleep
     */
    @Given("^the server is started$")
    public void theServerHasStarted() throws InterruptedException {
        setRestServer(new RestServer(new NoDatabaseQueryStrategy(null, null)));
        RestServerTest.RestServerThread restServerThread =
                new RestServerTest.RestServerThread(getRestServer());
        new Thread(restServerThread).start();
        Thread.sleep(500L);
    }

    /**
     * Verify that uri is available on the server.
     *
     * @param uri file to be requested
     */
    @Given("^the file (\\S+) is available on the server$")
    public void theFileIsAvailableOnTheServer(String uri) {
        String localLocation = String.format("/webapp%s", uri);
        assertNotNull(StaticFilesStepDefinitions.class.getResource(localLocation));
    }

    /**
     * Request the uri from the server.
     *
     * @param uri requested file
     * @throws IOException thrown when there's an issue in setting up the HTTP connection
     */
    @When("^the client requests (\\S+)$")
    public void theClientRequests(String uri) throws IOException {
        String url = String.format("http://localhost:%d%s", RestServer.DEFAULT_PORT, uri);
        setConnection((HttpURLConnection) new URL(url).openConnection());
    }

    /**
     * Verify that the contents on the server are equal to the received contents.
     *
     * @param uri requested file
     * @throws IOException thrown when there's an issue in the connection
     */
    @Then("^the client receives the contents of (\\S+)")
    public void theClientReceivesTheContentsOf(String uri) throws IOException {
        InputStream expected = StaticFilesStepDefinitions.class.getResourceAsStream(
                String.format("/webapp%s", uri));
        assertEquals(IOUtils.toString(expected, "UTF-8"), IOUtils.toString(
                getConnection().getInputStream(), "UTF-8"));
    }

    /**
     * Verify that the HTTP status code is equal to statusCode.
     *
     * @param statusCode expected status code
     * @throws IOException thrown when there's an issue in the connection
     */
    @Then("^the HTTP status code is (\\d+)$")
    public void theHttpStatusCodeIs(int statusCode) throws IOException {
        assertEquals(statusCode, connection.getResponseCode());
    }

    /**
     * Stop the server and close the connection.
     */
    @After
    public void cleanUp() {
        if (getRestServer() != null) {
            getRestServer().stopServer();
        }
        if (connection != null) {
            connection.disconnect();
        }
    }
}
