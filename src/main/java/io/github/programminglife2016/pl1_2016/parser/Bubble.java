package io.github.programminglife2016.pl1_2016.parser;

import java.util.List;

/**
 * Created by ravishivam on 26-4-16.
 */
public class Bubble implements Node {
    int id;
    int x;
    int y;
    int column;
    List<Node> links;
    List<Node> container;

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
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

    public List<Node> getLinks() {
        return this.links;
    }

    public int getColumn() {
        return this.column;
    }
}
