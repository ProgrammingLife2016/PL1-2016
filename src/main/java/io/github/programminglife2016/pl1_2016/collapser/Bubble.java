package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.util.*;

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
        this.links.addAll(segment.getLinks());
        this.backLinks.addAll(segment.getBackLinks());
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

    @Override
    public List<Node> getContainer() {
        return container;
    }

    @Override
    public Coordinate position(Coordinate coordinate, List<Node> bubbles, Node endNode, int level) {
        if (container.isEmpty() || container.get(0) instanceof Segment) {
            Coordinate c2 = startNode.position(coordinate, bubbles, this.endNode, level + 1);
            if (links.iterator().next() instanceof Segment) {
                return c2;
            } else {
                return links.iterator().next().position(c2, bubbles, endNode, level);
            }
        } else {
            startNode.setXY(coordinate.getX(), coordinate.getY());
            int height = (startNode.getLinks().size() - 1) * 500 / 2 / level;
            List<Coordinate> coords = new ArrayList<>();
            for (Node nodeFront : startNode.getLinks()) {
                coords.add(getBubble(bubbles, nodeFront.getContainerId()).position(new Coordinate(coordinate.getX() + 100, coordinate.getY() + height), bubbles, endNode, level + 1));
                height -= 500 / level;
            }
            Coordinate penultimate = new Coordinate(coords.stream().map(Coordinate::getX).mapToInt(x -> x).max().getAsInt() + 100, coordinate.getY());
            this.endNode.setXY(penultimate.getX(), penultimate.getY());
            return new Coordinate(penultimate.getX() + 100, penultimate.getY());
        }
    }

    public static Node getBubble(List<Node> bubbles, int containerId) {
        return bubbles.stream().filter(x -> x.getId() == containerId).findFirst().get();
    }

    /**
     * Return highest level bubble container of the leaf node in the given bubble if it exists,
     * else creates new bubble with the startNode == endNode.
     * @param newId new Id that will be assigned to the bubble if it is just created
     * @param leaf
     * @param bubbles
     * @param boundZoom
     * @return
     */
    public static Node getBestParentNode(int newId, Node leaf, Collection<Node> bubbles, int boundZoom){
        if(leaf instanceof Segment) {
            final int leafId = leaf.getId();
            Optional<Node> bubble = bubbles.stream().filter(x -> x.getStartNode().getId() == leafId
            ).findFirst();
            if(bubble.isPresent())
                leaf = bubble.get();
            else {
                leaf = new Bubble(newId, boundZoom, (Segment) leaf);
                newId++;
            }
        }
        Node bestParent = leaf;
        for (Node newCont : bubbles){
            if(newCont.getId() == bestParent.getContainerId()) {
                if(newCont.getZoomLevel() >= boundZoom)
                    bestParent = newCont;
            }
        }
        if(bestParent.getId() != leaf.getId())
            return getBestParentNode(newId, bestParent, bubbles, boundZoom);
        return bestParent;
    }

    public String toString() {
        return "Bubble{" +
                "id=" + id +
                ", startNode=" + startNode +
                ", endNode=" + endNode +
                ", containerSize=" + containersize +
                '}';
    }
}
