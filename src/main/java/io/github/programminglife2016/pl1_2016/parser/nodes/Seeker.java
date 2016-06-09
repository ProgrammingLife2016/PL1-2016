package io.github.programminglife2016.pl1_2016.parser.nodes;

import javafx.util.Pair;

/**
 * @author Kamran Tadzjibov on 09.06.2016.
 */
public interface Seeker {
    /**
     * Find pair of node index and local base position. 
     * @param name Genome name
     * @param position global base position
     * @return pair of node index and local base position.
     */
    Pair<Integer, Integer> find(String name, int position);
}
