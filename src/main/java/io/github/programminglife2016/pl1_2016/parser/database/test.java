package io.github.programminglife2016.pl1_2016.parser.database;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeList;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.sql.SQLException;

/**
 * Created by adam on 5/12/16.
 */
public class test {
    public static void main(String[] args) throws SQLException {
        SimpleDatabase db = new SimpleDatabase();
        NodeCollection a = new NodeList(1);
        a.put(1, new Segment(1,"a", 1));
        db.writeSegments(a);
        System.out.println(db.fetchSegments().getNodes().iterator().next().getX());
    }
}
