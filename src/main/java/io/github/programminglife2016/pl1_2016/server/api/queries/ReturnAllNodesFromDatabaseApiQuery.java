package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 * A query that responds to the root of the API, and returns all the nodes (without collapsing) as
 * the action.
 */
public class ReturnAllNodesFromDatabaseApiQuery implements ApiQuery {
    private FetchDatabase fdb;

    /**
     * Construct an ApiQuery that responds with the JSON representation of the argument.
     *
     * @param fdb object to be serialized and responded with
     */
    public ReturnAllNodesFromDatabaseApiQuery(FetchDatabase fdb) {
        this.fdb = fdb;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    public final String getQuery() {
        return "^/api/nodes$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    public final ApiAction getApiAction() {
        return args -> fdb.toJson(1).toString();
    }
}
