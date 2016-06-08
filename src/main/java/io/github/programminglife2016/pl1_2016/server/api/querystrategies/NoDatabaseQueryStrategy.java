package io.github.programminglife2016.pl1_2016.server.api.querystrategies;

import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.ApiHandler;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetLineageApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetStaticFileApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetThresholdedBubblesApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.IndividualSegmentDataApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.ReturnAllNodesApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.RootIndexApiQuery;

import java.util.Map;
/**
 * Implementation of Api using the database.
 */
public class NoDatabaseQueryStrategy implements QueryStrategy {
    private NodeCollection nodeCollection;
    private Map<String, Subject> subjects;

    /**
     * Create strategy which uses internal data stuctures for the api queries.
     * @param nodeCollection collection of all the nodes in the graph.
     * @param subjects metadata of the data in the graph.
     */
    public NoDatabaseQueryStrategy(NodeCollection nodeCollection, Map<String, Subject> subjects) {
        this.nodeCollection = nodeCollection;
        this.subjects = subjects;
    }

    @Override
    public void addQueries(ApiHandler apiHandler) {
        apiHandler.addQuery(new ReturnAllNodesApiQuery(nodeCollection))
                  .addQuery(new GetStaticFileApiQuery())
                  .addQuery(new RootIndexApiQuery())
                  .addQuery(new IndividualSegmentDataApiQuery(nodeCollection));
        if (subjects != null) {
            apiHandler.addQuery(new GetThresholdedBubblesApiQuery(nodeCollection))
                      .addQuery(new GetLineageApiQuery(subjects));
        }
    }
}