package io.github.programminglife2016.pl1_2016.server.api.querystrategies;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.server.api.ApiHandler;
import io.github.programminglife2016.pl1_2016.server.api.queries.*;

import java.util.Map;

/**
 * Implementation of Api using the database.
 */
public class DatabaseQueryStrategy implements QueryStrategy {
    private FetchDatabase fdb;
    private NodeCollection nodeCollection;
    private Map<String, Subject> subjects;

    /**
     * Create strategy which uses the database for the api queries.
     * @param fdb database object.
     * @param nodeCollection collection of all the nodes in the graph.
     * @param subjects metadata of the data in the graph.
     */
    public DatabaseQueryStrategy(FetchDatabase fdb, NodeCollection nodeCollection,
                                 Map<String, Subject> subjects) {
        this.fdb = fdb;
        this.nodeCollection = nodeCollection;
        this.subjects = subjects;
    }

    @Override
    public void addQueries(ApiHandler apiHandler) {
        apiHandler.addQuery(new ReturnAllNodesFromDatabaseApiQuery(fdb))
                  .addQuery(new GetStaticFileApiQuery())
                  .addQuery(new RootIndexApiQuery())
                  .addQuery(new MetadataNavigateApiQuery(nodeCollection))
                  .addQuery(new MetadataInfoFromDatabaseQuery(fdb))
                  .addQuery(new IndividualSegmentDataFromDatabaseApiQuery(fdb))
                  .addQuery(new OptionsOfGenomesFromDatabase(fdb))
                  .addQuery(new MetadataInfoAnnotationsFromDatabaseQuery(fdb))
                  .addQuery(new GetDataFromMutationApiQuery(fdb))
                  .addQuery(new MetadataInfosFromDatabaseQuery(fdb));
        if (subjects != null) {
            apiHandler.addQuery(new GetThresholdedBubblesFromDatabaseApiQuery(fdb))
                      .addQuery(new GetLineageFromDatabaseApiQuery(fdb));
        }
    }
}