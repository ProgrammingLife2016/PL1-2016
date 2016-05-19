package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.util.*;

public class Bubble implements Node {
    private int id;
    private int x;
    private int y;
    private Node startNode;
    private Node endNode;
    private transient List<Node> container = new ArrayList<>();
    private transient Set<Node> links = new HashSet<>();
    private transient Set<Node> backLinks = new HashSet<>();
    private int containerid;
    private int level;
    private int containersize;

    public Bubble(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

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
    public Coordinate position(Coordinate coordinate, Node endNode) {
        Coordinate c2 = startNode.position(coordinate, this.endNode);
        if (this == endNode || endNode == null) {
            return c2;
        }
        int height = (links.size() - 1) * 500 / 2;
        List<Coordinate> coordsFront = new ArrayList<>();
        for (Node nodeFront : links) {
            coordsFront.add(nodeFront.position(new Coordinate(c2.getX(), c2.getY() + height), endNode));
            height -= 500;
        }
        int maxX = coordsFront.stream().map(Coordinate::getX).mapToInt(x -> x).max().getAsInt();
        endNode.setXY(maxX, c2.getY());
        return new Coordinate(maxX + 100, c2.getY());
    }

    public String toString() {
        return "Bubble{" +
                "id=" + id +
                ", startNode=" + startNode +
                ", endNode=" + endNode +
                '}';
    }
}
