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

/**
 * Serialize annotation objects.
 */
public class AnnotationSerializer implements JsonSerializer<Annotation> {
    private Seeker sk;

    /**
     * Create instance of annotation serializer.
     * @param nodeCollection collection of nodes from the graph.
     */
    public AnnotationSerializer(NodeCollection nodeCollection) {
        this.sk = new SegmentSeeker(nodeCollection);
    }

    @Override
    public JsonElement serialize(Annotation annotation,
                                 Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("id", new JsonPrimitive(annotation.getId()));
            jsonObject.add("displayname", new JsonPrimitive(annotation.getDisplayName()));

            JSONObject jsonObject1 = sk.find(annotation.getSeqId() + ".ref", annotation.getStart());
            JSONObject jsonObject2 = sk.find(annotation.getSeqId() + ".ref", annotation.getEnd());

            jsonObject.add("startx", new JsonPrimitive(jsonObject1.getInt("x")));
            jsonObject.add("starty", new JsonPrimitive(jsonObject1.getInt("y")));
            jsonObject.add("endx", new JsonPrimitive(jsonObject2.getInt("x")));
            jsonObject.add("endy", new JsonPrimitive(jsonObject2.getInt("y")));
            return jsonObject;
        } catch (JSONException ignored) {
            return null;
        }
    }
}
