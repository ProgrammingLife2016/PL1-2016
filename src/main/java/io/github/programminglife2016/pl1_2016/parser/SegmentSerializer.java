package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by ali on 4/22/16.
 */
public class SegmentSerializer implements JsonSerializer<Segment> {
    public JsonElement serialize(Segment segment, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(segment.getId()));
        if (segment.getData() != null) {
            jsonObject.add("data", new JsonPrimitive(segment.getData()));
        }
        JsonArray links = new JsonArray();
        for (Segment link : segment.getLinks()) {
            links.add(new JsonPrimitive(link.getId()));
        }
        jsonObject.add("links", links);
        return jsonObject;
    }
}
