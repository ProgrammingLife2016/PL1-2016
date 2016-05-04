package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import io.github.programminglife2016.pl1_2016.server.api.actions.GetStaticFileApiAction;

import java.util.ArrayList;
import java.util.List;

/**
 * When accessing /, return /static/index.html
 */
public class RootIndexApiQuery implements ApiQuery {
    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> {
            List<String> arg = new ArrayList<>(1);
            arg.add("index.html");
            return new GetStaticFileApiAction().response(arg);
        };
    }
}
