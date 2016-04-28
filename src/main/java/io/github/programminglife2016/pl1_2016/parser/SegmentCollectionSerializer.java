package io.github.programminglife2016.pl1_2016.parser;

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
 * Custom serializer for SegmentCollection. Conforms API.
 */
public class SegmentCollectionSerializer implements JsonSerializer<SegmentCollection> {
        /**
         * Serialize SegmentCollection following API.
         *
         * @param segmentCollection to be serialized
         * @param type ignored
         * @param jsonSerializationContext ignored
         * @return serialized SegmentCollection object
         */
    public JsonElement serialize(SegmentCollection segmentCollection, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        final Gson segmentBuilder = new GsonBuilder().registerTypeAdapter(Segment.class,
                new SegmentSerializer()).create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("status", new JsonPrimitive("success"));
        JsonArray nodes = new JsonArray();
        segmentCollection.getSegments().stream()
                .map(segmentBuilder::toJsonTree)
                .forEach(nodes::add);
        jsonObject.add("nodes", nodes);
        JsonArray edges = new JsonArray();
        for (Segment segment : segmentCollection.getSegments()) {
            for (Segment link : segment.getLinks()) {
                JsonObject edge = new JsonObject();
                edge.add("from", new JsonPrimitive(segment.getId()));
                edge.add("to", new JsonPrimitive(link.getId()));
                edges.add(edge);
            }
        }
        jsonObject.add("edges", edges);
        return jsonObject;
    }
}
