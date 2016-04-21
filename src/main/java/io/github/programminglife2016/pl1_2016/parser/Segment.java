package io.github.programminglife2016.pl1_2016.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for representing a DNA sequence
 */
public class Segment {
    /**
     * Id of DNA segment.
     */
    private int id;

    /**
     * Contents of DNA segment.
     */
    private String data;

    /**
     * Links to other DNA segments in the graph.
     */
    private List<Segment> links;

    /**
     * Create segment with id and sequence data.
     * @param id identifier of this segment.
     * @param data sequence data of this segment.
     */
    public Segment(int id, String data) {
        this.id = id;
        this.data = data;
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
}
