package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import io.github.programminglife2016.pl1_2016.server.api.actions.GetStaticFileApiAction;

/**
 * Listen to /static/<path> and return /resources/webapp/<path>.
 */
public class GetStaticFileApiQuery implements ApiQuery {
    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/static/(.*)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return new GetStaticFileApiAction();
    }
}
