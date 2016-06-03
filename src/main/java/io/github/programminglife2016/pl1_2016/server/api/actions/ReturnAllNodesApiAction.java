package io.github.programminglife2016.pl1_2016.server.api.actions;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * ApiAction that returns all nodes, without collapsing them.
 */
public class ReturnAllNodesApiAction implements ApiAction {
    private JSONObject jsonObject;

    /**
     * Construct an ApiAction that responds with the JSON representation of the argument.
     *
     * @param jsonArray object to be responded with
     */
    public ReturnAllNodesApiAction(JSONObject jsonArray) {
        this.jsonObject = jsonArray;
    }

    /**
     * Compute a response based on the query arguments.
     *
     * @param args query arguments
     * @return response to the client
     */
    public String response(List<String> args) {
        return jsonObject.toString();
    }
}
