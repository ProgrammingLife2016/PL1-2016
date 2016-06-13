package io.github.programminglife2016.pl1_2016.parser.nodes;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;

import java.io.Serializable;
import java.util.Map;

/**
 * A data structure that represents the segments.
 */
public interface NodeCollection extends Map<Integer, Node>, JsonSerializable, Serializable {

    /**
     * Recalculate positions of the bubbles contained in the collection.
     */
    void recalculatePositions();
}
