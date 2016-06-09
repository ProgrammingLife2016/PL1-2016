package io.github.programminglife2016.pl1_2016.parser.nodes;

import javafx.util.Pair;

/**
 * Created by Kamran Tadzjibov on 09.06.2016.
 */
public interface Seeker {
    Pair<Integer, Integer> find(String name, int position);
}
