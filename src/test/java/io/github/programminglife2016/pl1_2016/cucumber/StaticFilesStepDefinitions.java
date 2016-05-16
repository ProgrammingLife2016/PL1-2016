package io.github.programminglife2016.pl1_2016.cucumber;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;
import io.github.programminglife2016.pl1_2016.server.api.RestServerTest;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class StaticFilesStepDefinitions {
    private static RestServer restServer;
    private static HttpURLConnection connection;

    @Given("^the server is started$")
    public void theServerHasStarted() throws InterruptedException {
        restServer = new RestServer(mock(NodeCollection.class));
        RestServerTest.RestServerThread restServerThread = new RestServerTest.RestServerThread(restServer);
        new Thread(restServerThread).start();
        Thread.sleep(500L);
    }

    @Given("^the file (\\S+) is available on the server$")
    public void theFileIsAvailableOnTheServer(String uri) {
        String localLocation = String.format("/webapp%s", uri);
        assertNotNull(StaticFilesStepDefinitions.class.getResource(localLocation));
    }

    @When("^the client requests (\\S+)$")
    public void theClientRequests(String uri) throws IOException {
        String url = String.format("http://localhost:%d%s", RestServer.PORT, uri);
        connection = (HttpURLConnection) new URL(url).openConnection();
    }

    @Then("^the client receives the contents of (\\S+)")
    public void theClientReceivesTheContentOf(String uri) throws IOException {
        InputStream expected = StaticFilesStepDefinitions.class.getResourceAsStream(String.format("/webapp%s", uri));
        assertEquals(IOUtils.toString(expected, "UTF-8"), IOUtils.toString(connection.getInputStream(), "UTF-8"));
    }

    @Then("^the HTTP status code is (\\d+)$")
    public void theHttpStatusCodeIs(int statusCode) throws IOException {
        assertEquals(statusCode, connection.getResponseCode());
    }

    @After
    public void cleanUp() {
        if (restServer != null) {
            restServer.stopServer();
        }
        if (connection != null) {
            connection.disconnect();
        }
    }
}
