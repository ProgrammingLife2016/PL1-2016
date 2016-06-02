package io.github.programminglife2016.pl1_2016.parser.nodes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.programminglife2016.pl1_2016.collapser.Bubble;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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
                .registerTypeAdapter(Segment.class, new NodeSerializer())
                .registerTypeAdapter(Bubble.class, new NodeSerializer()).create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("status", new JsonPrimitive("success"));
        JsonArray nodes = new JsonArray();
        nodeCollection.values().stream()
                .map(nodeBuilder::toJsonTree)
                .forEach(nodes::add);
        jsonObject.add("nodes", nodes);
        List<JsonObject> edges = new ArrayList<>();
        for (Node node : nodeCollection.values()) {
            for (Node link : node.getLinks()) {
                JsonObject edge = new JsonObject();
                edge.add("x1", new JsonPrimitive(node.getX()));
                edge.add("y1", new JsonPrimitive(node.getY()));
                edge.add("x2", new JsonPrimitive(link.getX()));
                edge.add("y2", new JsonPrimitive(link.getY()));
                List<Subject> subjects = new ArrayList<>(node.getSubjects());
                subjects.retainAll(link.getSubjects());
                edge.add("gens", new JsonPrimitive(subjects.size()));
                JsonArray jsonGenomes = new JsonArray();
                subjects.stream().map(Subject::getNameId).distinct().forEach(jsonGenomes::add);
                edge.add("genomes", jsonGenomes);
                JsonArray jsonLineages = new JsonArray();
                subjects.stream().map(Subject::getLineage).distinct().forEach(jsonLineages::add);
                edge.add("lineages", jsonLineages);
                edges.add(edge);
            }
        }
        Collections.sort(edges, (t1, t2) -> Integer.compare(t1.get("gens").getAsInt(),
                t2.get("gens").getAsInt()));
        JsonArray jsonArray = new JsonArray();
        edges.stream().forEach(jsonArray::add);
        jsonObject.add("edges", jsonArray);
        return jsonObject;
    }
}
