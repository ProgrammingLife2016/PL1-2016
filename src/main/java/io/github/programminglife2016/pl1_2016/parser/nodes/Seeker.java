package io.github.programminglife2016.pl1_2016.parser.nodes;

import org.json.JSONObject;

/**
 * @author Kamran Tadzjibov on 09.06.2016.
 */
public interface Seeker {
    /**
     * Find pair of node index and local base position.
     *
     * @param name     Genome name
     * @param position global base position
     * @return pair of node index and local base position.
     */
    JSONObject find(String name, int position);
}
