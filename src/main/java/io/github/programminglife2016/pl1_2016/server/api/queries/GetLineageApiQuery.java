package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

import java.util.Map;

/**
 * Listens to /api/lineage/[genome] and return the lineage of [genome].
 */
public class GetLineageApiQuery implements ApiQuery {
    private Map<String, Subject> subjects;

    /**
     * Construct the ApiQuery.
     *
     * @param subjects node collection to retrieve the data information from
     */
    public GetLineageApiQuery(Map<String, Subject> subjects) {
        this.subjects = subjects;
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
        return args -> subjects.get(args.get(0)).getLineage();
    }
}
