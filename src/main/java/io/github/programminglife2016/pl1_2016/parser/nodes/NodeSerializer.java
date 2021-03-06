package io.github.programminglife2016.pl1_2016.parser.nodes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * A custom serializer for Node. Conforms API.
 */
public class NodeSerializer implements JsonSerializer<Node> {
    /**
     * Serialize Node into JSON. Create a JSON object with five fields:
     *
     * - id: the id of the node
     * - bubble: whether this node is aggregated
     * - data: the string of nucleotides
     * - x: the x-position of the node
     * - y: the y-position of the node
     *
     * @param node the node to be serialized
     * @param type ignored
     * @param jsonSerializationContext ignored
     * @return the serialized node object
     */
    public final JsonElement serialize(Node node, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(node.getId()));
        jsonObject.add("x", new JsonPrimitive(node.getX()));
        jsonObject.add("y", new JsonPrimitive(node.getY()));
        return jsonObject;
    }
}
