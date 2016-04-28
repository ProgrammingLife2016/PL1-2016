package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 * An object that specifies a query that it should respond to, and a corresponding action that
 * executes once the query is requested.
 */
public interface ApiQuery {
    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    String getQuery();

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    ApiAction getApiAction();
}
