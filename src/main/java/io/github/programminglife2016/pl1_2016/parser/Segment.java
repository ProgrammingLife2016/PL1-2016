package io.github.programminglife2016.pl1_2016.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for representing a DNA sequence
 */
public class Segment implements Node {
    /**
     * Id of DNA segment.
     */
    private int id;

    /**
     * Contents of DNA segment.
     */
    private String data;

    /**
     * z-index of Segment in graph.
     */
    private int column;

    /**
     * Links to other DNA segments in the graph.
     */
    private List<Segment> links;

    /**
     * x position of the segment in the graph.
     */
    private int x;

    /**
     * y position of the segment in the graph.
     */
    private int y;

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
        this.links = new ArrayList<Segment>();
    }

    /**
     * Create segment with id.
     * @param id identifier of this segment.
     */
    public Segment(int id) {
        this.id = id;
        this.links = new ArrayList<Segment>();
    }

    /**
     * Add a link from this segment to other segment.
     * @param other segment to link to.
     */
    public void addLink(Segment other) {
        this.links.add(other);
    }

    /**
     * Get sequence data of this segment.
     * @return string of sequence data.
     */
    public String getData() {
        return data;
    }

    /**
     * Set sequence data of this segment.
     * @param data to set sequence data.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Get links to other segments in the graph.
     * @return list of links.
     */
    public List<Segment> getLinks() {
        return links;
    }

    /**
     * Get the id if this segment.
     * @return id of this segment.
     */
    public int getId() {
        return id;
    }

    /**
     * Get index of column in graph of this DNA segment.
     * @return index of column starting at 0.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Set column index if this DNA segment.
     * @param column index in graph.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Set both the x and the y position of the segment.
     * @param x x-position of the segment.
     * @param y y-position of the segment.
     */
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x value of the segment.
     * @return x value of the segment.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y value of the segment.
     * @return y value of the segment
     */
    public int getY() {
        return y;
    }

    /**
     * Return string representation of segment.
     * @return string representing segment.
     */
    @Override
    public String toString() {
        return String.format("Segment{id=%d, x=%d, y=%d, column=%d}", id, x, y, column);
    }
}
