package io.github.programminglife2016.pl1_2016.parser.nodes;

import java.util.*;

/**
 * Data structure for representing a DNA sequence
 */
public class Segment implements Node {
    private int id;
    private int x;
    private int y;
    private transient String data;
    private transient int column;
    private transient Set<Node> links = new HashSet<>();
    private transient Set<Node> backLinks = new HashSet<>();
    private transient static final Boolean ISBUBBLE = false;
    private transient Set<String> genomes = new HashSet<>();
    private transient int containerid;
    private transient int level;
    private transient static final int CONTAINERSIZE = 1;
    /**
     * Create segment with id and sequence data.
     * @param id identifier of this segment.
     * @param data sequence data of this segment.
     * @param column index of this segment.
     */
    public Segment(int id, String data, int column) {
        this.id = id;
        this.data = data;
        this.column = column;
    }

    /**
     * Create segment with id.
     * @param id identifier of this segment.
     */
    public Segment(int id) {
        this.id = id;
    }

    /**
     * Add a link from this segment to other segment.
     * @param other segment to link to.
     */
    public final void addLink(Node other) {
        this.links.add(other);
    }

    /**
     * Add a predecessor node.
     *
     * @param node connected node
     */
    @Override
    public final void addBackLink(Node node) {
        backLinks.add(node);
    }

    /**
     * Get sequence data of this segment.
     * @return string of sequence data.
     */
    public final String getData() {
        return data;
    }

    /**
     * Set sequence data of this segment.
     * @param data to set sequence data.
     */
    public final void setData(String data) {
        this.data = data;
    }

    /**
     * Get links to other segments in the graph.
     * @return list of links.
     */
    public final Collection<Node> getLinks() {
        return links;
    }

    /**
     * Get the predecessors of this node.
     *
     * @return links of this node
     */
    @Override
    public final Collection<Node> getBackLinks() {
        return backLinks;
    }

    /**
     * Get the id if this segment.
     * @return id of this segment.
     */
    public final int getId() {
        return id;
    }

    /**
     * Get index of column in graph of this DNA segment.
     * @return index of column starting at 0.
     */
    public final int getColumn() {
        return column;
    }

    /**
     * Add the genomes this segment belongs to.
     *
     * @param genomes the genomes this segment belongs to
     */
    @Override
    public final void addGenomes(Collection<String> genomes) {
        this.genomes.addAll(genomes);
    }

    /**
     * Get the genomes this segment belongs to.
     *
     * @return the genomes this segment belongs to
     */
    @Override
    public final Set<String> getGenomes() {
        return genomes;
    }

    /**
     * Set column index if this DNA segment.
     * @param column index in graph.
     */
    public final void setColumn(int column) {
        this.column = column;
    }

    /**
     * Set both the x and the y position of the segment.
     * @param x x-position of the segment.
     * @param y y-position of the segment.
     */
    public final void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x value of the segment.
     * @return x value of the segment.
     */
    public final int getX() {
        return x;
    }

    /**
     * Get the y value of the segment.
     * @return y value of the segment
     */
    public final int getY() {
        return y;
    }

    /**
     * Return string representation of segment.
     * @return string representing segment.
     */
    @Override
    public final String toString() {
        return String.format("Segment{id=%d, x=%d, y=%d, containerid=%d}", id, x, y, containerid);
    }

    @Override
    public final Segment clone() {
        try {
            Segment segment = (Segment) super.clone();
            segment.links = new HashSet<>(links);
            segment.backLinks = new HashSet<>(backLinks);
            segment.genomes = new HashSet<>(genomes);
            return segment;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            assert false : "Clone should be supported";
            throw new RuntimeException(e);
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Segment segment = (Segment) o;
        return id == segment.id;
    }

    @Override
    public final int getContainerId() {
        return this.containerid;
    }


    @Override
    public final void setContainerId(int containerid) {
        this.containerid = containerid;
    }

    @Override
    public final int getZoomLevel() {
        return this.level;
    }

    @Override
    public final List<Node> getContainer() {
        return new ArrayList<>();
    }

    @Override
    public final int getContainerSize() {
        return CONTAINERSIZE;
    }

    @Override
    public final Boolean isBubble(){
        return ISBUBBLE;
    }

    @Override
    public void setContainerSize(int size) {
    }

    @Override
    public final void setZoomLevel(int level) {
        this.level = level;
    }

    @Override
    public final int hashCode() {
        return id;
    }

    public final Node getStartNode() {
        return this;
    }

    @Override
    public final Node getEndNode() {
        return this;
    }

    @Override
    public void setEndNode(Node node) {
    }
    @Override
    public void setStartNode(Node node) {
    }
}