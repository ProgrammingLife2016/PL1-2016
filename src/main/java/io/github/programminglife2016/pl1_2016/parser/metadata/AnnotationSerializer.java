package io.github.programminglife2016.pl1_2016.parser.metadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Seeker;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentSeeker;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class AnnotationSerializer implements JsonSerializer<Annotation> {
    private Seeker sk;

    public AnnotationSerializer(NodeCollection nodeCollection) {
        this.sk = new SegmentSeeker(nodeCollection);
    }

    @Override
    public JsonElement serialize(Annotation annotation, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("id", new JsonPrimitive(annotation.getId()));
            jsonObject.add("displayName", new JsonPrimitive(annotation.getDisplayName()));

            JSONObject jsonObject1 = sk.find(annotation.getSeqId() + ".ref", annotation.getStart());
            JSONObject jsonObject2 = sk.find(annotation.getSeqId() + ".ref", annotation.getEnd());

            jsonObject.add("startX", new JsonPrimitive(jsonObject1.getInt("x")));
            jsonObject.add("startY", new JsonPrimitive(jsonObject1.getInt("y")));
            jsonObject.add("endX", new JsonPrimitive(jsonObject2.getInt("x")));
            jsonObject.add("endY", new JsonPrimitive(jsonObject2.getInt("y")));
            return jsonObject;
        } catch (JSONException ignored) {
            return null;
        }
    }
}
