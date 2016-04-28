package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Data is sent by the server in JSON format. Classes extending this method can convert their
 * representation to JSON.
 */
public interface JsonSerializable {
    /**
     * Convert the representation to JSON.
     *
     * @return JSON representation of this object.
     */
    default String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(NodeList.class, new NodeCollectionSerializer())
                .registerTypeAdapter(NodeMap.class, new NodeCollectionSerializer()).create();
        return gson.toJson(this);
    }
}
