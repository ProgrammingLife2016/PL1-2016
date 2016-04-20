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

    public Segment(int id, String data) {
        this.id = id;
        this.data = data;
        this.links = new ArrayList<Segment>();
    }

    public Segment(int id) {
        this.id = id;
        this.links = new ArrayList<Segment>();
    }

    /**
     * Add a link from this segment to other segment.
     * @param other
     */
    public void addLink(Segment other) {
        this.links.add(other);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Segment> getLinks() {
        return links;
    }

    public void setLinks(List<Segment> links) {
        this.links = links;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
