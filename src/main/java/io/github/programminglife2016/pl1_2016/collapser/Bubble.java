package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.*;
import java.util.stream.Collectors;

public class Bubble implements Node {
    private int id;
    private int x;
    private int y;
    private transient final Boolean isBubble = true;
    private transient Node startNode;
    private transient Node endNode;
    private transient List<Node> container = new ArrayList<>();
    private transient Set<Node> links = new HashSet<>();
    private transient Set<Node> backLinks = new HashSet<>();
    private transient int containerid;
    private transient int level;
    private transient String data = "";
    private transient int containersize;

    public Bubble(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public Bubble(int id, Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.id = id;

    }

    public Bubble (int newId, int zoomLvl, Segment segment){
        this.id = newId;
        this.startNode = segment;
        this.endNode = segment;
        this.level = zoomLvl;
        this.containerid = segment.getContainerId();
        segment.setContainerId(id);
//        this.links.addAll(segment.getLinks());
//        this.backLinks.addAll(segment.getBackLinks());
    }

    @Override
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return (startNode.getX() + endNode.getX()) / 2;
    }

    @Override
    public int getY() {
        return (startNode.getY() + endNode.getY()) / 2;
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
    public void addGenomes(Collection<Subject> genomes) {

    }

    @Override
    public Set<String> getGenomes() {
        return this.startNode.getGenomes();
    }
    @Override
    public Set<Subject> getSubjects() {
        return this.startNode.getSubjects();
    }

    /**
     * Make a shallow clone of this node. Only the links are cloned one level more.
     *
     * @return the cloned node.
     */
    @Override
    public Node clone() {
        return null;
    }

    /**
     * Return the id of the container the node resides in.
     *
     * @return id of the container.
     */
    @Override
    public int getContainerId() {
        return this.containerid;
    }

    /**
     * Set the id of the container the node resides in.
     *
     * @param containerid id of the bubble in which current node is located
     */
    @Override
    public void setContainerId(int containerid) {
        this.containerid = containerid;
    }

    /**
     * Return the zoomlevel the node resides in.
     *
     * @return the depth of the level.
     */
    @Override
    public int getZoomLevel() {
        return this.level;
    }

    /**
     * Set the zoom level of the node
     */
    @Override
    public void setZoomLevel(int level) {
        this.level = level;
    }

    /**
     * Returns the size of the container.
     *
     * @return size of the container.
     */
    @Override
    public int getContainerSize() {
        return this.containersize;
    }

    /**
     * Sets the set size of the bubble.
     *
     * @param size size of the bubble.
     */
    @Override
    public void setContainerSize(int size) {
        this.containersize = size;
    }

    /**
     * Return whether the node is a bubble.
     *
     * @return true if it is a bubble.
     */
    @Override
    public Boolean isBubble(){
        return isBubble;
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
    public void setEndNode(Node node) {
        this.endNode = node;
    }

    public void setStartNode(Node node) {
        this.startNode = node;
    }

    /**
     * Get the nodes if the nodes has a container.
     *
     * @return List of the nodes the node contains.
     */
    @Override
    public List<Node> getContainer() {
        return container;
    }


    /** Check whether a certian bubble equals this bubble.
     * @param o Object compared to
     * @return true if this equals the object, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bubble bubble = (Bubble) o;
        return id == bubble.id;
    }

    /**
     * Returns string represtation of the bubble.
     * @return String representation of Bubble.
     */
    public String toString() {
        return "Bubble{"
                + "id=" + id
                + ", startNode=" + startNode
                + ", container=" + container.stream()
                                    .map(x -> x.getId()).collect(Collectors.toList())
                + ", endNode=" + endNode
                + ", containerSize=" + containersize
                + '}';
    }
}