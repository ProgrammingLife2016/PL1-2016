package io.github.programminglife2016.pl1_2016.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ravishivam on 26-4-16.
 */
public class Bubble extends ArrayList<Node> implements Node, Cloneable {
    private boolean changed;

    /**
     * z-index of Segment in graph.
     */
    private int column;

    /**
     * Id of bubble.
     */
    private int id;

    /**
     * x position of the segment in the graph.
     */
    private int x;

    /**
     * y position of the segment in the graph.
     */
    private int y;

    private Collection<Node> links;

    public Bubble() {
        changed = false;
        links = new ArrayList<Node>();
    }

    /**
     * Calculates the Euclidean distance between the two nodes.
     * @param other The other node to which the distance is calculated.
     * @return The distance between this node and the other node.
     */
    public double distanceTo(Node other) {
        double dist = 0.0;

        double xval = Math.pow(this.x + other.getX(), 2);
        double yval = Math.pow(this.y + other.getY(), 2);

        dist = Math.sqrt(xval + yval);

        return dist;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        if (changed) {
            computeCentroid();
        }
        return this.x;
    }

    public int getY() {
        if (changed) {
            computeCentroid();
        }
        return this.y;
    }

    public void setData(String data) {
        this.id = Integer.parseInt(data);
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void addLink(Node node) {
        this.links.add(node);
    }

    public int getId() {
        return this.id;
    }

    public String getData() {
        return Integer.toString(id);
    }

    public Collection<Node> getLinks() {
        return this.links;
    }

    public int getColumn() {
        return this.column;
    }

    @Override
    /**
     * Overrides List.add, in order to set the changed variable.
     */
    public boolean add(Node fv) {
        changed = true;
        return super.add(fv);
    }
    /**
     * Return a deep cloned object of the bubble
     * @return Deep cloned object of node bubble
     */
    public Bubble clone() {
        return (Bubble) super.clone();
    }

    /**
     * If the bubble did not change since it has calculated its previous centroid,
     * it will return the buffered centroid point. Otherwise it will recalculate the
     * centroid and return that point.
     */
    public void computeCentroid() {
        if (size() == 0) {
            return;
        }
        if (changed == true) {
            double sumX = 0;
            double sumY = 0;
            for (Node node
                    : this) {
                sumX += node.getX();
                sumY += node.getY();
            }
            this.x = (int) sumX/size();
            this.y = (int) sumY/size();
            changed = false;
        }
    }
}
