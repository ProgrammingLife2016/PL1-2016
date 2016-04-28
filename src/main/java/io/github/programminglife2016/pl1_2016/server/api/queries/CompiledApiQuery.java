package io.github.programminglife2016.pl1_2016.server.api.queries;

import java.util.regex.Pattern;

/**
 * An ApiQuery, that along with the regex string and the action, also contains a compiled regex
 * query. This is useful when a pattern needs to be matched multiple times, as the regex string
 * only has to be compiled once.
 */
public interface CompiledApiQuery extends ApiQuery {
    /**
     * Return the compiled regex string.
     *
     * @return the compiled regex string
     */
    Pattern getCompiledQuery();
}
