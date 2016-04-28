package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.NodeCollection;

/**
 * Created by ravishivam on 26-4-16.
 */
public interface Graph {

    NodeCollection retrieveBoundedGraph(int startx, int endx, int starty, int endy) throws CloneNotSupportedException;

    NodeCollection generateZoomedGraph(NodeCollection collection) throws CloneNotSupportedException;

    NodeCollection getFullGraph();

    NodeCollection getCurrentGraph();

}
