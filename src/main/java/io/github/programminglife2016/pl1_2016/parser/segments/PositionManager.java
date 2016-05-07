package io.github.programminglife2016.pl1_2016.parser.segments;

/**
 * Manager for calculating postions of nodes. Classes implementing this interface
 * can calculate the positions of nodes in a graph.
 */
public interface PositionManager {
    /**
     * Method for calculating the positions of nodes in the graph.
     */
    void calculatePositions();
}
