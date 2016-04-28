package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.NodeCollection;

/**
 * Created by ravishivam on 26-4-16.
 */
public interface Graph {

    NodeCollection retrieveBoundedGraph(int startx, int endx, int starty, int endy);

    NodeCollection generateZoomedGraph(NodeCollection collection);

    NodeCollection getFullGraph();

    NodeCollection getCurrentGraph();

}
