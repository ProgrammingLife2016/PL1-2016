package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.NodeCollection;

/**
 * Created by ravishivam on 27-4-16.
 */
public interface ClusterHandler {
    NodeCollection retrieveClusters() throws CloneNotSupportedException;
}
