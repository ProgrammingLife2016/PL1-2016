package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.phylotree.TreeNode;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import io.github.programminglife2016.pl1_2016.server.api.actions.GetLeveledBoundedNodesApiAction;
import io.github.programminglife2016.pl1_2016.server.api.actions.GetLeveledNodesApiAction;

public class GetLeveledBoundedNodesApiQuery implements ApiQuery {
    private NodeCollection nodeCollection;
    private TreeNode rootNode;

    public GetLeveledBoundedNodesApiQuery(NodeCollection nodeCollection, TreeNode rootNode) {
        this.nodeCollection = nodeCollection;
        this.rootNode = rootNode;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/nodes/(\\d+)/(\\d+)/(\\d+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return new GetLeveledBoundedNodesApiAction(nodeCollection, rootNode);
    }
}
