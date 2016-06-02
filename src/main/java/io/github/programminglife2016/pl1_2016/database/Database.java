package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import org.json.JSONArray;

import java.sql.SQLException;
import java.util.Map;

/**
 * Interface for creating a database.
 */
public interface Database {

    /**
     * Fetch all segments in database.
     * @return collection of nodes in database
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    JSONArray fetchNodes(int threshold, int x, int y) throws SQLException;
    /**
     * Fetch segment by id.
     * @param id the id of the node to find
     * @return the node with id id
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    Node fetchSegment(int id) throws SQLException;

    /**
     * Write a collection of segments into the database.
     * @param nodes the collection to write
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    void writeSegments(NodeCollection nodes) throws SQLException;

    /**
     * Fetch all specimen from database.
     * @return the collection of specimen
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    Map<String, Subject> fetchSpecimens() throws SQLException;

}
