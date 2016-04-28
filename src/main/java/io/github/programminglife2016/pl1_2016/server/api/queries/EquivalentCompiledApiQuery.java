package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

import java.util.regex.Pattern;

/**
 * A CompiledApiQuery that takes an ApiQuery in the constructor, and returns the same query and
 * action as the original ApiQuery (hence the name Equivalent). In addition to that, the query is
 * also compiled once and returned upon request.
 */
public class EquivalentCompiledApiQuery implements CompiledApiQuery {
    private ApiQuery apiQuery;
    private Pattern pattern;

    /**
     * Construct a CompiledApiQuery, using the same ApiQuery as the parameter, and a compiled regex
     * string.
     *
     * @param apiQuery ApiQuery to be based upon
     */
    public EquivalentCompiledApiQuery(ApiQuery apiQuery) {
        this.apiQuery = apiQuery;
        this.pattern = Pattern.compile(apiQuery.getQuery());
    }

    /**
     * Return the compiled regex string.
     *
     * @return the compiled regex string
     */
    public Pattern getCompiledQuery() {
        return pattern;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    public String getQuery() {
        return apiQuery.getQuery();
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    public ApiAction getApiAction() {
        return apiQuery.getApiAction();
    }
}
