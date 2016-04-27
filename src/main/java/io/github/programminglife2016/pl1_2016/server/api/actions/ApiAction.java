package io.github.programminglife2016.pl1_2016.server.api.actions;

import java.util.List;

/**
 * An object that computes a response when a GET request is performed.
 */
public interface ApiAction {
    /**
     * Compute a response based on the query arguments.
     *
     * @param args query arguments
     * @return response to the client
     */
    String response(List<String> args);
}
