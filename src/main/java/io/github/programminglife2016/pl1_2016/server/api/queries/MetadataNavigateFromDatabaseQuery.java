package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 *  A query that responds to the /api/metadata/navigate/(\\S+)/(\\d+)$, and returns node and index in this node of the base.
 */
public class MetadataNavigateFromDatabaseQuery implements ApiQuery {
    private FetchDatabase fdb;

    /**
     * Construct the ApiQuery.
     *
     * @param fdb database to retrieve the data information from
     */
    public MetadataNavigateFromDatabaseQuery(FetchDatabase fdb) {
            this.fdb = fdb;
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
        return args -> fdb.getXOnNavigate(args.get(0), Integer.parseInt(args.get(1))).toString();
    }
}
