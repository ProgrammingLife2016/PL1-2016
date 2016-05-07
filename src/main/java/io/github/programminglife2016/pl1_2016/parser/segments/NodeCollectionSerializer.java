package io.github.programminglife2016.pl1_2016.parser.segments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Custom serializer for NodeCollection. Conforms API.
 */
public class NodeCollectionSerializer implements JsonSerializer<NodeCollection> {
        /**
         * Serialize NodeCollection following API.
         *
         * @param nodeCollection to be serialized
         * @param type ignored
         * @param jsonSerializationContext ignored
         * @return serialized NodeCollection object
         */
    public JsonElement serialize(NodeCollection nodeCollection, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        final Gson nodeBuilder = new GsonBuilder()
                .registerTypeAdapter(Segment.class, new NodeSerializer()).create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("status", new JsonPrimitive("success"));
        JsonArray nodes = new JsonArray();
        nodeCollection.getNodes().stream()
                .map(nodeBuilder::toJsonTree)
                .forEach(nodes::add);
        jsonObject.add("nodes", nodes);
        JsonArray edges = new JsonArray();
        for (Node node : nodeCollection.getNodes()) {
            for (Node link : node.getLinks()) {
                JsonObject edge = new JsonObject();
                edge.add("from", new JsonPrimitive(node.getId()));
                edge.add("to", new JsonPrimitive(link.getId()));
                edges.add(edge);
            }
        }
        jsonObject.add("edges", edges);
        return jsonObject;
    }
}
