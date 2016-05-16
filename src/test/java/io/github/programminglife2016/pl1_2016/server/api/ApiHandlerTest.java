package io.github.programminglife2016.pl1_2016.server.api;

import com.sun.net.httpserver.HttpExchange;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import io.github.programminglife2016.pl1_2016.server.api.queries.ApiQuery;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Anstract test class for ApiHandler. Test all methods.
 */
public abstract class ApiHandlerTest {
    private ApiHandler apiHandler;

    /**
     * SEt the to be tested object to a subclass object of ApiHandler
     *
     * @param apiHandler object to be tested
     */
    public void setApiHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }


    /**
     * Add an ApiQuery to the handler, and test if its action is invoked once the correct input
     * query is used.
     *
     * @throws URISyntaxException thrown if the test URI is invalid
     * @throws IOException thrown if resources (ports, etc.) are already in use
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAddQuery() throws URISyntaxException, IOException {
        ApiQuery apiQuery1 = Mockito.mock(ApiQuery.class);
        ApiQuery apiQuery2 = Mockito.mock(ApiQuery.class);
        ApiAction apiAction = Mockito.mock(ApiAction.class);
        HttpExchange httpExchange = Mockito.mock(HttpExchange.class);
        Mockito.when(httpExchange.getRequestURI()).thenReturn(new URI("/testapi2/arg"));
        Mockito.when(httpExchange.getResponseBody()).thenReturn(Mockito.mock(OutputStream.class));
        Mockito.when(apiQuery1.getQuery()).thenReturn("^/testapi1$");
        Mockito.when(apiQuery2.getQuery()).thenReturn("^/testapi2/(.*)$");
        Mockito.when(apiQuery2.getApiAction()).thenReturn(apiAction);
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        Mockito.when(apiAction.response(argument.capture())).thenReturn("hello");
        apiHandler.addQuery(apiQuery1);
        apiHandler.addQuery(apiQuery2);
        apiHandler.handle(httpExchange);
        Mockito.verify(apiAction, Mockito.times(1)).response(Mockito.any(List.class));
        ArrayList<String> expectedArgs = new ArrayList<>();
        expectedArgs.add("arg");
        assertEquals(expectedArgs, argument.getValue());
    }
}
