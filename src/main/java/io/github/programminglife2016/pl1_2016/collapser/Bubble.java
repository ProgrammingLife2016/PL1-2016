package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A bubble represents a higher level node. A bubble can contain multiple segments, or multiple
 * bubbles.
 */
public class Bubble implements Node {
    private int id;
    private int x;
    private int y;
    private transient Node startNode;
    private transient Node endNode;
    private transient List<Node> container = new ArrayList<>();
    private transient Set<Node> links = new HashSet<>();
    private transient Set<Node> backLinks = new HashSet<>();
    private int containerid;
    private int level;
    private String data = "";
    private int containersize;

    /**
     * Create a bubble that encompasses the nodes between startNode and endNode.
     *
     * @param id id of the bubble
     * @param startNode first node of the bubble
     * @param endNode last node of the bubble
     */
    public Bubble(int id, Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.id = id;
    }

    @Override
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setData(String data) {
    }

    @Override
    public void setColumn(int column) {
    }

    @Override
    public void addLink(Node node) {
        endNode.addLink(node);
    }

    @Override
    public void addBackLink(Node node) {
        startNode.addBackLink(node);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getData() {
        return null;
    }

    @Override
    public Collection<Node> getLinks() {
        return links;
    }

    @Override
    public Collection<Node> getBackLinks() {
        return backLinks;
    }

    @Override
    public int getColumn() {
        return 0;
    }

    @Override
    public void addGenomes(Collection<String> genomes) {
    }

    @Override
    public Set<String> getGenomes() {
        return this.startNode.getGenomes();
    }

    @Override
    public Node clone() {
        return null;
    }

    @Override
    public int getContainerId() {
        return this.containerid;
    }

    @Override
    public void setContainerId(int containerid) {
        this.containerid = containerid;
    }

    @Override
    public int getZoomLevel() {
        return this.level;
    }

    @Override
    public void setZoomLevel(int level) {
        this.level = level;
    }

    @Override
    public int getContainerSize() {
        return this.containersize;
    }

    @Override
    public void setContainerSize(int size) {
        this.containersize = size;
    }

    @Override
    public Boolean isBubble() {
        return true;
    }

    @Override
    public Node getStartNode() {
        return startNode;
    }

    @Override
    public Node getEndNode() {
        return endNode;
    }

    @Override
    public List<Node> getContainer() {
        return container;
    }

    @Override
    public String toString() {
        return String.format("Bubble{id=%d, startNode=%s, endNode=%s}", id, startNode.toString(),
                endNode.toString());
    }
}
