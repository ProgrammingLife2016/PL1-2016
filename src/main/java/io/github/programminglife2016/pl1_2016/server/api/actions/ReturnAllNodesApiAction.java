package io.github.programminglife2016.pl1_2016.server.api.actions;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;

import java.util.List;

/**
 * ApiAction that returns all nodes, without collapsing them.
 */
public class ReturnAllNodesApiAction implements ApiAction {
    private JsonSerializable jsonSerializable;

    /**
     * Construct an ApiAction that responds with the JSON representation of the argument.
     *
     * @param jsonSerializable object to be serialized and responded with
     */
    public ReturnAllNodesApiAction(JsonSerializable jsonSerializable) {
        this.jsonSerializable = jsonSerializable;
    }

    /**
     * Compute a response based on the query arguments.
     *
     * @param args query arguments
     * @return response to the client
     */
    public String response(List<String> args) {
        return jsonSerializable.toJson();
    }
}
