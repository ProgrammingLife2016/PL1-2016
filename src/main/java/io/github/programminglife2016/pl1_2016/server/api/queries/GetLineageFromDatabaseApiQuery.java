package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

import java.sql.SQLException;

/**
 * Listens to /api/lineage/[genome] and return the lineage of [genome].
 */
public class GetLineageFromDatabaseApiQuery implements ApiQuery {
    private FetchDatabase fdb;

    /**
     * Construct the ApiQuery.
     *
     * @param fdb database to retrieve the data information from
     */
    public GetLineageFromDatabaseApiQuery(FetchDatabase fdb) {
        this.fdb = fdb;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/lineage/(.+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> {
            String lineage = "";
            try {
                lineage = fdb.getLineage(args.get(0));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return lineage;
        };
    }
}
