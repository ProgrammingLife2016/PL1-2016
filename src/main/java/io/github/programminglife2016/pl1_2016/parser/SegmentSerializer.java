package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * A custom serializer for Segment. Conforms API.
 */
public class SegmentSerializer implements JsonSerializer<Node> {
    /**
     * Serialize Segment into JSON. Create a JSON object with five fields:
     *
     * - id: the id of the segment
     * - bubble: whether this node is aggregeated
     * - data: the string of nucleotides
     * - x: the x-position of the node
     * - y: the y-position of the node
     *
     * @param node the segment to be serialized
     * @param type ignored
     * @param jsonSerializationContext ignored
     * @return the serialized segment object
     */
    public JsonElement serialize(Node node, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(node.getId()));
        // TODO: check if node is a bubble
        jsonObject.add("bubble", new JsonPrimitive(false));
        if (node.getData() != null) {
            jsonObject.add("data", new JsonPrimitive(node.getData()));
        } else {
            jsonObject.add("data", new JsonPrimitive(""));
        }
        jsonObject.add("x", new JsonPrimitive(node.getX()));
        jsonObject.add("y", new JsonPrimitive(node.getY()));
        return jsonObject;
    }
}
