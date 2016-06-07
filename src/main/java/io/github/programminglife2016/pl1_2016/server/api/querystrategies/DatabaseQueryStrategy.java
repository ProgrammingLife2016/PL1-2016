package io.github.programminglife2016.pl1_2016.server.api.querystrategies;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.ApiHandler;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetLineageFromDatabaseApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetStaticFileApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.GetThresholdedBubblesFromDatabaseApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.IndividualSegmentDataApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.ReturnAllNodesFromDatabaseApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.RootIndexApiQuery;

import java.util.Map;

public class DatabaseQueryStrategy implements QueryStrategy {
    private FetchDatabase fdb;
    private NodeCollection nodeCollection;
    private Map<String, Subject> subjects;

    public DatabaseQueryStrategy(FetchDatabase fdb, NodeCollection nodeCollection,
                                 Map<String, Subject> subjects) {
        this.fdb = fdb;
        this.nodeCollection = nodeCollection;
        this.subjects = subjects;
    }

    @Override
    public void addQueries(ApiHandler apiHandler) {
        apiHandler.addQuery(new ReturnAllNodesFromDatabaseApiQuery(fdb));
        apiHandler.addQuery(new GetStaticFileApiQuery());
        apiHandler.addQuery(new RootIndexApiQuery());
        apiHandler.addQuery(new IndividualSegmentDataApiQuery(nodeCollection));
        if (subjects != null) {
            apiHandler.addQuery(new GetThresholdedBubblesFromDatabaseApiQuery(fdb));
            apiHandler.addQuery(new GetLineageFromDatabaseApiQuery(fdb));
        }
    }
}