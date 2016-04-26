package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * A custom serializer for Segment. Instead of serializing entire segments in links, only the ids
 * of the segments are put into JSON. Saves space.
 */
public class SegmentSerializer implements JsonSerializer<Segment> {
    /**
     * Serialize Segment into JSON. Create a JSON object with three fields:
     *
     * - id: the id of the segment
     * - data (optional): the string of nucleotides
     * - links: the id of connected segments
     *
     * @param segment the segment to be serialized
     * @param type ignored
     * @param jsonSerializationContext ignored
     * @return the serialized sprint object
     */
    public JsonElement serialize(Segment segment, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(segment.getId()));
        if (segment.getData() != null) {
            jsonObject.add("data", new JsonPrimitive(segment.getData()));
        }
        JsonArray links = new JsonArray();
        for (Node link : segment.getLinks()) {
            links.add(new JsonPrimitive(link.getId()));
        }
        jsonObject.add("links", links);
        return jsonObject;
    }
}
