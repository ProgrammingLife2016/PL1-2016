package io.github.programminglife2016.pl1_2016.parser.nodes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
                .registerTypeAdapter(Node.class, new NodeSerializer()).create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("status", new JsonPrimitive("success"));
        JsonArray nodes = new JsonArray();
        nodeCollection.values().stream()
                .map(nodeBuilder::toJsonTree)
                .forEach(nodes::add);
        jsonObject.add("nodes", nodes);
        JsonArray edges = new JsonArray();
        for (Node node : nodeCollection.values()) {
            for (Node link : node.getLinks()) {
                JsonObject edge = new JsonObject();
                edge.add("x1", new JsonPrimitive(node.getX()));
                edge.add("y1", new JsonPrimitive(node.getY()));
                edge.add("x2", new JsonPrimitive(link.getX()));
                edge.add("y2", new JsonPrimitive(link.getY()));
                List<String> genomes = new ArrayList<>(node.getGenomes());
                genomes.retainAll(link.getGenomes());
                JsonArray genomeArray = new JsonArray();
                genomes.stream().forEach(genomeArray::add);
                edge.add("genomes", genomeArray);
                edges.add(edge);
            }
        }
        jsonObject.add("edges", edges);
        return jsonObject;
    }
}
