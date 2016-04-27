package io.github.programminglife2016.pl1_2016.parser;

import io.github.programminglife2016.pl1_2016.Launcher;

/**
 * Created by ravishivam on 26-4-16.
 */
public class BubbleGraph implements Graph {

    private NodeCollection fullgraph;

    private NodeCollection currentGraph;

    public BubbleGraph() throws CloneNotSupportedException {
        Parser parser = new SimpleParser();
        parser.parse(Launcher.class.getResourceAsStream("/genomes/TB10_.gfa"));
        this.fullgraph = parser.getSegmentCollection();
        this.currentGraph =  generateGraph(fullgraph.clone());
    }

    public NodeCollection retrieveBoundedGraph(int startx, int endx, int starty, int endy) {
        NodeCollection boundedGraph = new NodeList(currentGraph.size());
        for (Node node:
                (Node[]) currentGraph.getCollection()) {
            if (node == null) {
                continue;
            }
            int thisx = node.getX();
            int thisy = node.getY();
            if(thisx >= startx && thisx <= endx && thisy >= starty && thisy <= endy) {
                boundedGraph.put(node.getId(), node);
            }
        }
        this.currentGraph = generateGraph(boundedGraph);
        return boundedGraph;
    }

    public NodeCollection generateGraph(NodeCollection graph) {

        return null;
    }

    public NodeCollection getFullgraph() {
        return fullgraph;
    }

    public NodeCollection getCurrentGraph() {
        return currentGraph;
    }

    public void setFullgraph(NodeCollection fullgraph) {
        this.fullgraph = fullgraph;
    }

    public void setCurrentGraph(NodeCollection currentGraph) {
        this.currentGraph = currentGraph;
    }
}
