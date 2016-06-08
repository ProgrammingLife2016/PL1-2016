package io.github.programminglife2016.pl1_2016.server.api;

import com.sun.net.httpserver.HttpHandler;
import io.github.programminglife2016.pl1_2016.server.api.queries.ApiQuery;

/**
 * A HttpHandler that responds to the query with the appropriate action.
 */
public interface ApiHandler extends HttpHandler {
    /**
     * Add a query to which the API should respond.
     *
     * @param apiQuery a query to which the API should respond
     * @return return reference to this object.
     */
    ApiHandler addQuery(ApiQuery apiQuery);
}
