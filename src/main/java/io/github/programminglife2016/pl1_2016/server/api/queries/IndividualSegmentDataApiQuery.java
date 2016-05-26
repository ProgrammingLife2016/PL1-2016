package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

import java.io.IOException;
import java.util.List;

public class IndividualSegmentDataApiQuery implements ApiQuery {
    private NodeCollection nodeCollection;

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
        return new ApiAction() {
            @Override
            public String response(List<String> args) throws IOException {
                return nodeCollection.get(Integer.parseInt(args.get(0))).getData();
            }
        };
    }
}
