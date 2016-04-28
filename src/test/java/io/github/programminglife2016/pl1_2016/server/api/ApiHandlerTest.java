package io.github.programminglife2016.pl1_2016.server.api;

import com.sun.net.httpserver.HttpExchange;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import io.github.programminglife2016.pl1_2016.server.api.queries.ApiQuery;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void testAddQuery() throws URISyntaxException, IOException {
        ApiQuery apiQuery1 = mock(ApiQuery.class);
        ApiQuery apiQuery2 = mock(ApiQuery.class);
        ApiAction apiAction = mock(ApiAction.class);
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestURI()).thenReturn(new URI("/testapi2/arg"));
        when(httpExchange.getResponseBody()).thenReturn(mock(OutputStream.class));
        when(apiQuery1.getQuery()).thenReturn("^/testapi1$");
        when(apiQuery2.getQuery()).thenReturn("^/testapi2/(.*)$");
        when(apiQuery2.getApiAction()).thenReturn(apiAction);
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        when(apiAction.response(argument.capture())).thenReturn("hello");
        apiHandler.addQuery(apiQuery1);
        apiHandler.addQuery(apiQuery2);
        apiHandler.handle(httpExchange);
        verify(apiAction, times(1)).response(any(List.class));
        ArrayList<String> expectedArgs = new ArrayList<String>();
        expectedArgs.add("arg");
        assertEquals(expectedArgs, argument.getValue());
    }
}
