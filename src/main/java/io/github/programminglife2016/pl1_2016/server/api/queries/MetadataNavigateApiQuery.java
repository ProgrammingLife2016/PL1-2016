package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Seeker;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentSeeker;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 * A query that responds to the /api/metadata/navigate/(\\S+)/(\\d+)$, and returns node and index in this node of the
 * base.
 */
public class MetadataNavigateApiQuery implements ApiQuery {
    private NodeCollection nodeCollection;

    /**
     * Construct the ApiQuery.
     *
     * @param nodeCollection nodeCollection to retrieve the data information from
     */
    public MetadataNavigateApiQuery(NodeCollection nodeCollection) {
        this.nodeCollection = nodeCollection;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/metadata/navigate/(\\S+)/(\\d+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> {
            Seeker sk = new SegmentSeeker(nodeCollection);
            return sk.find(args.get(0), Integer.parseInt(args.get(1))).toString();
        };
    }
}
