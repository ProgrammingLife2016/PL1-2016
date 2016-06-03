package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.collapser.BubbleDispatcher;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for creating a database to setup the database.
 */
public class SetupDatabase implements Database {

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
    private static final String SPECIMEN_TABLE = "specimen";
    /**
     * Name of specimen table.
     */
    private static final String LINK_GENOMES_TABLE = "genomeslinks";
    /**
     * The connection to the database.
     */
    private Connection connection;
    /**
     * The connection to the database.
     */
    private static final int[] THRESHOLDS = {1, 4, 16, 32, 64, 128};

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

    /**
     * Setup the database if it is not setup already.
     * @param nodes The nodes that will be used for setup
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public void setup(NodeCollection nodes) throws SQLException {
        if (!isSetup()) {
            clearTable(LINK_TABLE);
            clearTable(NODES_TABLE);
            clearTable(LINK_GENOMES_TABLE);
            BubbleDispatcher dispatcher = new BubbleDispatcher(nodes);
            for (int i = 0; i < THRESHOLDS.length; i++) {
                System.out.println("Writing to database nodes with threshold: " + THRESHOLDS[i]);
                NodeCollection nodesToWrite = dispatcher.getThresholdedBubbles(THRESHOLDS[i]);
                nodesToWrite.recalculatePositions();
                writeNodes(nodesToWrite, THRESHOLDS[i]);
            }
        }

    }

    private boolean isSetup() {
        return false;
    }

    /**
     * Write a collection of segments into the database
     * @param  threshold threshold of graph that has to be written.
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
                if (node.getStartNode().getId() == node.getEndNode().getId()) {
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
                    stmt.setInt(3, threshold);
                    stmt.executeUpdate();
                }
            }
            writeLinksGenomes(nodes, threshold);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    @SuppressWarnings("checkstyle:magicnumber")
    private void writeLinksGenomes(NodeCollection nodes, int threshold) throws SQLException {
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
                        stmtgenomes.setString(3, genome.trim().replace(" ", "_").split("\\.")[0]);
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
