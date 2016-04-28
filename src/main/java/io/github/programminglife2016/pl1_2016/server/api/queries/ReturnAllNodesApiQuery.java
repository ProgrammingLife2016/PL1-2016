package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import io.github.programminglife2016.pl1_2016.server.api.actions.ReturnAllNodesApiAction;

/**
 * A query that responds to the root of the API, and returns all the nodes (without collapsing) as
 * the action.
 */
public class ReturnAllNodesApiQuery implements ApiQuery {
    private JsonSerializable jsonSerializable;

    /**
     * Construct an ApiQuery that responds with the JSON representation of the argument.
     *
     * @param jsonSerializable object to be serialized and responded with
     */
    public ReturnAllNodesApiQuery(JsonSerializable jsonSerializable) {
        this.jsonSerializable = jsonSerializable;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    public String getQuery() {
        return "^/api/nodes$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    public ApiAction getApiAction() {
        return new ReturnAllNodesApiAction(jsonSerializable);
    }
}
