package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 * A query that responds to the root of the API, and returns all the nodes (without collapsing) as
 * the action.
 */
public class ReturnAllNodesApiQuery implements ApiQuery {
    private NodeCollection nodeCollection;

    /**
     * Construct an ApiQuery that responds with the JSON representation of the argument.
     *
     * @param nodeCollection object to be serialized and responded with
     */
    public ReturnAllNodesApiQuery(NodeCollection nodeCollection) {
        this.nodeCollection = nodeCollection;
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
        return args -> nodeCollection.toJson();
    }
}
