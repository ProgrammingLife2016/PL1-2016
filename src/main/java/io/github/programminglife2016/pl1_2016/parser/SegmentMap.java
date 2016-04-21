package io.github.programminglife2016.pl1_2016.parser;

import java.util.HashMap;

/**
 * Adapter for segment hashmap
 */
public class SegmentMap extends HashMap<Integer, Segment> implements JsonSerializable {

    public SegmentMap(int initialCapacity) {
        super(initialCapacity);
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{}");
        return sb.toString();
    }
}
