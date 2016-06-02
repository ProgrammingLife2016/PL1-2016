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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                List<Subject> genomes = new ArrayList<>(node.getSubjects());
                genomes.retainAll(link.getSubjects());
                edge.add("gens", new JsonPrimitive(genomes.size()));
                edges.add(edge);
                List<String> lineages = node.getSubjects().stream().map(Subject::getLineage).collect(Collectors.toList());
                lineages.retainAll(link.getSubjects().stream().map(Subject::getLineage).collect(Collectors.toList()));
                JsonArray jsonLineages = new JsonArray();
                lineages.stream().forEach(jsonLineages::add);
                edge.add("lineages", jsonLineages);
                edge.add("mostFreqLineage", new JsonPrimitive(mostFrequentLineage(lineages)));
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

    private String mostFrequentLineage(Collection<String> lineages) {
        if (lineages.isEmpty()) {
            return "";
        }
        Map<String, Integer> occurences = new HashMap<>();
        for (String lineage : lineages) {
            occurences.put(lineage, occurences.getOrDefault(lineage, 0));
        }
        Map.Entry<String, Integer> mostFrequent = occurences.entrySet().iterator().next();
        for (Map.Entry<String, Integer> entry : occurences.entrySet()) {
            if (entry.getValue() > mostFrequent.getValue()) {
                mostFrequent = entry;
            }
        }
        return mostFrequent.getKey();
    }
}
