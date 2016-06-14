package io.github.programminglife2016.pl1_2016.parser.nodes;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import io.github.programminglife2016.pl1_2016.parser.metadata.Annotation;

import java.util.List;
import java.util.Map;

/**
 * A data structure that represents the segments.
 */
public interface NodeCollection extends Map<Integer, Node>, JsonSerializable {

    /**
     * Recalculate positions of the bubbles contained in the collection.
     */
    void recalculatePositions();

    void setAnnotations(List<Annotation> annotations);

    List<Annotation> getAnnotations();
}
