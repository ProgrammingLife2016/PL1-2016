package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.collapser.BubbleDispatcher;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.sql.*;

public class SetupDatabase implements Database{

    /**
     * Driver for the database.
     */
    private static final String DATABASE_DRIVER = "org.postgresql.Driver";
    /**
     * Host for the database.
     */
    private static final String HOST = "jdbc:postgresql://localhost:5432/pl1";
    /**
     * Roll for the database.
     */
    private static final String ROLL = "pl";
    /**
     * Password for database.
     */
    private static final String PASSWORD = "visual";
    /**
     * Name of segments table.
     */
    private static final String NODES_TABLE = "segments";
    /**
     * Name of links table.
     */
    private static final String LINK_TABLE = "links";
    /**
     * Name of specimen table.
     */
    private static final String Nodes_TABLE = "specimen";
    /**
     * The connection to the database.
     */
    private Connection connection;
    /**
     * The connection to the database.
     */
    private int[] thresholds = {1, 4, 16, 32, 64, 128};

    /**
     * Constructor to construct a database.
     */
    public SetupDatabase() {
        connect();
    }

    /**
     * Connect to database.
     */
    private void connect() {
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

    public void setup(NodeCollection nodes) throws SQLException {
        if (!isSetup()) {
            clearTable(LINK_TABLE);
            clearTable(NODES_TABLE);
            BubbleDispatcher dispatcher = new BubbleDispatcher(nodes);
            for (int i = 0; i < thresholds.length; i++) {
                System.out.println("Writing to database nodes with threshold: " + thresholds[i]);
                NodeCollection nodesToWrite = dispatcher.getThresholdedBubbles(thresholds[i]);
                nodesToWrite.recalculatePositions();
                writeNodes(nodesToWrite, thresholds[i]);
            }
        }

    }

    private boolean isSetup() {
        return false;
    }

    /**
     * Write a collection of segments into the database
     *
     * @param nodes the collection to write
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public void writeNodes(NodeCollection nodes, int threshold) throws SQLException {
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + NODES_TABLE
                + "(id, data, x, y, isbubble) VALUES"
                + "(?,?,?,?,?) ON CONFLICT DO NOTHING";

        try {
            stmt = connection.prepareStatement(query);
            for (Node node : nodes.values()) {
                stmt.setInt(1, node.getId());
                if (node.getStartNode().getId() == node.getEndNode().getId()){
                    stmt.setString(2, node.getStartNode().getData());
                    stmt.setBoolean(5, false);

                } else {
                    stmt.setString(2, node.getData());
                    stmt.setBoolean(5, node.isBubble());
                }

                stmt.setInt(3, node.getX());
                stmt.setInt(4, node.getY());

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
            if (stmt == null) {
                stmt.close();
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

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
                    stmt.setInt(3, threshold);
                    stmt.executeUpdate();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
