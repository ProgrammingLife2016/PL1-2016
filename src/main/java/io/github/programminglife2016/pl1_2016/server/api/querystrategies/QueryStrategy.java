package io.github.programminglife2016.pl1_2016.server.api.querystrategies;

import io.github.programminglife2016.pl1_2016.server.api.ApiHandler;

/**
 * An object that handles the response to queries.
 */
public interface QueryStrategy {
    /**
     * Add a query to the handler.
     * @param apiHandler handler for a particular query.
     */
    void addQueries(ApiHandler apiHandler);
}
