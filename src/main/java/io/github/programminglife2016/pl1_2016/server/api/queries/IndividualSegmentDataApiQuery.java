package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 * Listens to /api/data/[id] and return the data of segment [id].
 */
public class IndividualSegmentDataApiQuery implements ApiQuery {
    private NodeCollection nodeCollection;

    /**
     * Construct the ApiQuery.
     *
     * @param nodeCollection nodeCollection to retrieve the data information from
     */
    public IndividualSegmentDataApiQuery(NodeCollection nodeCollection) {
        this.nodeCollection = nodeCollection;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/data/(\\d+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> nodeCollection.get(Integer.parseInt(args.get(0))).getData();
    }
}
