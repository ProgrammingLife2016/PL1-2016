package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.collapser.BubbleDispatcher;
import io.github.programminglife2016.pl1_2016.parser.metadata.Annotation;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Seeker;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentSeeker;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for creating a database to setup the database.
 */
public class SetupDatabase implements Database {
    /**
     * The connection to the database.
     */
    private Connection connection;
    /**
     * The connection to the database.
     */
    private static final int[] THRESHOLDS = {1, 4, 16, 32, 64, 128};

    private static final int FIVE = 5;
    private static final int FOUR = 4;
    private static final int THREE = 3;

    /**
     * Constructor to construct a database.
     */
    public SetupDatabase() {
        connect();
    }

    /**
     * Connect to database.
     */
    public void connect() {
        try {
            Class.forName(DATABASE_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not included in dependencies. Update Maven!");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(HOST, ROLL, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup the database if it is not setup already.
     * @param nodes The nodes that will be used for setup
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public final void setup(NodeCollection nodes) throws SQLException {
        clearTable(LINK_TABLE);
        clearTable(NODES_TABLE);
        clearTable(LINK_GENOMES_TABLE);
        clearTable(ANNOTATIONS_TABLE);
        writeAnnotations(nodes);
        BubbleDispatcher dispatcher = new BubbleDispatcher(nodes);
        for (int THRESHOLD : THRESHOLDS) {
            System.out.println("Writing to database nodes with threshold: " + THRESHOLD);
            NodeCollection nodesToWrite = dispatcher.getThresholdedBubbles(THRESHOLD);
            nodesToWrite.recalculatePositions();
            writeNodes(nodesToWrite, THRESHOLD);
        }
    }

    private void writeAnnotations(NodeCollection nodes) throws SQLException {
        String query = String.format("INSERT INTO %s (id, displayName, startX, startY, endX, endY) VALUES (?,?,?,?,?,?) ON CONFLICT DO NOTHING", ANNOTATIONS_TABLE);
        PreparedStatement stmt = connection.prepareStatement(query);
        Seeker sk = new SegmentSeeker(nodes);

        for (Annotation annotation : nodes.getAnnotations()) {
            JSONObject jsonObject1 = sk.find(annotation.getSeqId() + ".ref", annotation.getStart());
            JSONObject jsonObject2 = sk.find(annotation.getSeqId() + ".ref", annotation.getEnd());
            stmt.setString(1, annotation.getId());
            stmt.setString(2, annotation.getDisplayName());
            stmt.setInt(3, jsonObject1.getInt("x"));
            stmt.setInt(4, jsonObject1.getInt("y"));
            stmt.setInt(5, jsonObject2.getInt("x"));
            stmt.setInt(6, jsonObject2.getInt("y"));
            stmt.executeUpdate();
        }
        stmt.close();
    }

    /**
     * Write a collection of nodes into the database
     * @param  threshold threshold of graph that has to be written.
     * @param nodes the collection to write
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private void writeNodes(NodeCollection nodes, int threshold) throws SQLException {
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + NODES_TABLE
                + "(id, data, x, y, isbubble) VALUES"
                + "(?,?,?,?,?) ON CONFLICT DO NOTHING";

        try {
            stmt = connection.prepareStatement(query);
            for (Node node : nodes.values()) {
                stmt.setInt(1, node.getId());
                if (node.getStartNode().getId() == node.getEndNode().getId()) {
                    stmt.setString(2, node.getStartNode().getData());
                    stmt.setBoolean(FIVE, false);

                } else {
                    stmt.setString(2, node.getData());
                    stmt.setBoolean(FIVE, node.isBubble());
                }

                stmt.setInt(THREE, node.getX());
                stmt.setInt(FOUR, node.getY());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        writeLinks(nodes, threshold);
    }

    private void clearTable(String tableName) {
        Statement stmt;
        try {
            stmt = connection.createStatement();
            String query = "DELETE FROM " + tableName;
            stmt.execute(query);
            stmt.close();
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }
    @SuppressWarnings("checkstyle:magicnumber")
    private void writeLinks(NodeCollection nodes, int threshold) throws SQLException {
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + LINK_TABLE
                + "(from_id, to_id, threshold) VALUES"
                + "(?,?,?)";

        try {
            stmt = connection.prepareStatement(query);

            for (Node node : nodes.values()) {
                for (Node link : node.getLinks()) {
                    stmt.setInt(1, node.getId());
                    stmt.setInt(2, link.getId());
                    stmt.setInt(THREE, threshold);
                    stmt.executeUpdate();
                }
            }
            writeLinksGenomes(nodes);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    @SuppressWarnings("checkstyle:magicnumber")
    private void writeLinksGenomes(NodeCollection nodes) throws SQLException {
        PreparedStatement stmtgenomes = null;
             String querygenomes = "INSERT INTO " + LINK_GENOMES_TABLE
                + "(from_id, to_id, genome) VALUES"
                + "(?,?,?) ON CONFLICT DO NOTHING";
        try {
            stmtgenomes = connection.prepareStatement(querygenomes);

            for (Node node : nodes.values()) {
                for (Node link : node.getLinks()) {
                    Set<String> intersection = new HashSet<String>(node.getGenomes());
                    intersection.retainAll(link.getGenomes());

                    for (String genome : intersection) {
                        stmtgenomes.setInt(1, node.getId());
                        stmtgenomes.setInt(2, link.getId());
                        stmtgenomes.setString(
                                THREE,
                                genome.trim().replace(" ", "_").split("\\.")[0]);
                        stmtgenomes.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmtgenomes != null) {
                stmtgenomes.close();
            }
        }
    }


}
