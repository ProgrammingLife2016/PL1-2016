package io.github.programminglife2016.pl1_2016.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

/**
 * Adapter for segment hashmap
 */
public class SegmentMap extends HashMap<Integer, Segment> implements SegmentCollection {

    /**
     * Create segment hashmap.
     * @param initialCapacity capacity of the hashmap.
     */
    public SegmentMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Convert hashmap to JSON representation and return as string.
     * @return hashmap converted to JSON string.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Segment.class, new SegmentSerializer())
                .create();
        return gson.toJson(this);
    }
}
