package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

/**
 * Listens to /api/data/[id] and return the data of segment [id].
 */
public class AminoAcidMutationApiQuery implements ApiQuery {
    private FetchDatabase fdb;

    /**
     * Construct the ApiQuery.
     *
     * @param fdb database to retrieve the data information from
     */
    public AminoAcidMutationApiQuery(FetchDatabase fdb) {
        this.fdb = fdb;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/amino/(\\d+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> fdb.fetchAmino(Integer.parseInt(args.get(0))).toString();
    }
}