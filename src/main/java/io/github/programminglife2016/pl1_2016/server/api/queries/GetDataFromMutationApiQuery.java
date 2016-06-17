package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

import java.util.Map;

/**
 * Listens to /api/data/mutation/[bubbleid] and return the segment data of [bubbleid]
 */
public class GetDataFromMutationApiQuery implements ApiQuery {
    private FetchDatabase fetchDatabase;

    /**
     * Construct the ApiQuery.
     *
     * @param fetchDatabase node collection to retrieve the data information from
     */
    public GetDataFromMutationApiQuery(FetchDatabase fetchDatabase) {
        this.fetchDatabase = fetchDatabase;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/data/mutation/(\\d+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> fetchDatabase.fetchPrimitives(Integer.parseInt(args.get(0))).toString();
    }
}
