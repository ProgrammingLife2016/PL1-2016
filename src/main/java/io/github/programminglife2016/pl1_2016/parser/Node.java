package io.github.programminglife2016.pl1_2016.parser;

import java.util.Collection;
import java.util.List;

/**
 * Created by ravishivam on 26-4-16.
 */
public interface Node {

    double distanceTo(Node other);

    void setXY(int x, int y);

    int getX();

    int getY();

    void setData(String data);

    void setColumn(int column);

    void addLink(Node node);

    int getId();

    String getData();

    Collection<Node> getLinks();

    int getColumn();
}
