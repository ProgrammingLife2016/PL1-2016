package io.github.programminglife2016.pl1_2016.server.api;

import com.sun.net.httpserver.HttpExchange;
import io.github.programminglife2016.pl1_2016.server.api.queries.ApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.CompiledApiQuery;
import io.github.programminglife2016.pl1_2016.server.api.queries.EquivalentCompiledApiQuery;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

/**
 * An ApiHandler that manages a RESTful API.
 */
public class RestHandler implements ApiHandler {
    private static final int HTTP_STATUS_OK = 200;
    private static final String ERROR_MESSAGE = "{\"status\": \"error\"}";

    private Collection<CompiledApiQuery> queries;

    /**
     * Construct a RestHandler.
     */
    public RestHandler() {
        queries = new ArrayList<CompiledApiQuery>();
    }

    /**
     * Runs the input query against the added queries. If one matches, call the appropriate action
     * and send the response back to the client.
     *
     * @param httpExchange HttpException
     * @throws IOException thrown when communication failure
     */
    public void handle(HttpExchange httpExchange) throws IOException {
        String input = httpExchange.getRequestURI().toString();
        String message = ERROR_MESSAGE;

        for (CompiledApiQuery q : queries) {
            Matcher m = q.getCompiledQuery().matcher(input);
            if (m.find()) {
                List<String> args = new ArrayList<String>();
                int count = m.groupCount();
                // m.group(0) is the entire matched query, in which we're not interested
                for (int i = 1; i <= count; i++) {
                    args.add(m.group(i));
                }
                message = q.getApiAction().response(args);
                break;
            }
        }
        httpExchange.sendResponseHeaders(HTTP_STATUS_OK, message.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(message.getBytes("UTF-8"));
        os.close();
    }

    /**
     * Add a query to which the API should respond.
     *
     * @param apiQuery a query to which the API should respond
     */
    public void addQuery(ApiQuery apiQuery) {
        queries.add(new EquivalentCompiledApiQuery(apiQuery));
    }
}
