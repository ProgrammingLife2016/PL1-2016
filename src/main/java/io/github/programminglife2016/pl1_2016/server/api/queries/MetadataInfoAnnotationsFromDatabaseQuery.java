package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 * Query that responds to /api/metadata/info/<genome> and returns a subject
 */
public class MetadataInfoAnnotationsFromDatabaseQuery implements ApiQuery {
    private FetchDatabase fdb;

    /**
     * Construct the ApiQuery.
     *
     * @param fdb database to retrieve the data information from
     */
    public MetadataInfoAnnotationsFromDatabaseQuery(FetchDatabase fdb) {
        this.fdb = fdb;
    }
    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/metadata/annotations$";
    }
    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> fdb.getAnnotations().toString();
    }
}
