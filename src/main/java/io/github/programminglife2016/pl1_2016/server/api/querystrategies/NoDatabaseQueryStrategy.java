package io.github.programminglife2016.pl1_2016.server.api.querystrategies;

import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.ApiHandler;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetLineageApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetLineageFromDatabaseApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetStaticFileApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetThresholdedBubblesApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.IndividualSegmentDataApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.ReturnAllNodesApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.RootIndexApiQuery;

import java.util.Map;

public class NoDatabaseQueryStrategy implements QueryStrategy {
    private NodeCollection nodeCollection;
    private Map<String, Subject> subjects;

    public NoDatabaseQueryStrategy(NodeCollection nodeCollection, Map<String, Subject> subjects) {
        this.nodeCollection = nodeCollection;
        this.subjects = subjects;
    }

    @Override
    public void addQueries(ApiHandler apiHandler) {
        apiHandler.addQuery(new ReturnAllNodesApiQuery(nodeCollection));
        apiHandler.addQuery(new GetStaticFileApiQuery());
        apiHandler.addQuery(new RootIndexApiQuery());
        apiHandler.addQuery(new IndividualSegmentDataApiQuery(nodeCollection));
        if (subjects != null) {
            apiHandler.addQuery(new GetThresholdedBubblesApiQuery(nodeCollection));
            apiHandler.addQuery(new GetLineageApiQuery(subjects));
        }
    }
}