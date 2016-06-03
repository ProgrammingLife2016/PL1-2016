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

    @Override
    public List<Node> getContainer() {
        return container;
    }

    public static Node getBubble(List<Node> bubbles, int containerId) {
        return bubbles.stream().filter(x -> x.getId() == containerId).findFirst().get();
    }


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

    public String toString() {
        return "Bubble{" +
                "id=" + id +
                ", startNode=" + startNode +
                ", container=" + container.stream().map(x -> x.getId()).collect(Collectors.toList()) +
                ", endNode=" + endNode +
                ", containerSize=" + containersize +
                '}';
    }
}