package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.collapser.BubbleDispatcher;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 * Listens to /api/nodes/[threshold] and return the data of segment [threshold].
 */
public class GetThresholdedBubblesApiQuery implements ApiQuery {
    private BubbleDispatcher bubbleDispatcher;

    /**
     * Construct the ApiQuery.
     *
     * @param nodeCollection node collection to retrieve the data information from
     */
    public GetThresholdedBubblesApiQuery(NodeCollection nodeCollection) {
        this.bubbleDispatcher = new BubbleDispatcher(nodeCollection);
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/nodes/(\\d+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> bubbleDispatcher
                .getThresholdedBubbles(Integer.parseInt(args.get(0)))
                .toJson();
    }
}
