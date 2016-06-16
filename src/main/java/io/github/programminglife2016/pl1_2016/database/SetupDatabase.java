package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.collapser.BubbleDispatcher;
import io.github.programminglife2016.pl1_2016.parser.ObjectSerializer;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
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
    private static final int[] THRESHOLDS = {4, 16, 64, 256, 1024, 4096};
    private static final int FIVE = 5;
    private static final int FOUR = 4;
    private static final int THREE = 3;
    private Collection splist;
    private String dataset;
    /**
     * Constructor to construct a database.
     */
    public SetupDatabase(String dataset, Collection<Subject> values) {
        this.dataset = dataset;
        this.splist = values;
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
            connection = DriverManager.getConnection(HOST + dataset.toLowerCase(), ROLL, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup the database if it is not setup already.
     *
     * @param nodes The nodes that will be used for setup
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public final void setup(NodeCollection nodes) {
        if (!isSetup()) {
            clearTable(SPECIMEN_TABLE);
            clearTable(LINK_TABLE);
            clearTable(NODES_TABLE);
            clearTable(LINK_GENOMES_TABLE);
            try {
                writeSpecimen(this.splist);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < THRESHOLDS.length; i++) {
                BubbleDispatcher dispatcher = new BubbleDispatcher(nodes, dataset);
                System.out.println("Writing to database nodes with threshold: " + THRESHOLDS[i]);
                NodeCollection nodesToWrite = dispatcher.getThresholdedBubbles(THRESHOLDS[i], false);
                nodesToWrite.recalculatePositions();
                try {
                    writeNodes(nodesToWrite, THRESHOLDS[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean isSetup() {
        Statement stmt = null;
        String query = "SELECT count(*) FROM " + NODES_TABLE;
        boolean res = false;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if (rs.getInt("count") != 0) {
                res = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * Write a collection of nodes into the database
     *
     * @param threshold threshold of graph that has to be written.
     * @param nodes     the collection to write
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private void writeNodes(NodeCollection nodes, int threshold) throws SQLException {
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + NODES_TABLE + "(id, data, x, y, isbubble, containersize) VALUES" + "(?,?,?,?,"
                + "" + "?,?) ON CONFLICT DO NOTHING";

        try {
            stmt = connection.prepareStatement(query);
            int i = 0;
            for (Node node : nodes.values()) {
                stmt.setInt(1, node.getId());
                if (node.getStartNode().getId() == node.getEndNode().getId() && !node.getStartNode().isBubble()) {
                    stmt.setString(2, node.getStartNode().getData());
                    stmt.setBoolean(FIVE, false);

                } else {
                    stmt.setString(2, node.getData());
                    stmt.setBoolean(FIVE, node.isBubble());
                }
                stmt.setInt(THREE, node.getX());
                stmt.setInt(FOUR, node.getY());
                stmt.setInt(6, node.getContainerSize());
                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == nodes.values().size()) {
                    stmt.executeBatch();
                }
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
        String query = "INSERT INTO " + LINK_TABLE + "(from_id, to_id, threshold, genomes) VALUES" + "(?,?,?,?)";

        try {
            stmt = connection.prepareStatement(query);

            int i = 0;
            for (Node node : nodes.values()) {
                for (Node link : node.getLinks()) {
                    Set<String> intersection = new HashSet<String>(node.getGenomes());
                    System.out.println(link.getGenomes());
                    intersection.retainAll(link.getGenomes());
                    stmt.setInt(1, node.getId());
                    stmt.setInt(2, link.getId());
                    stmt.setInt(THREE, threshold);
                    stmt.setString(4, intersection.toString());
                    stmt.addBatch();
                    i++;

                    if (i % 1000 == 0) {
                        stmt.executeBatch();
                    }
                }
            }
            stmt.executeBatch();
//            writeLinksGenomes(nodes);
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
        String querygenomes = "INSERT INTO " + LINK_GENOMES_TABLE + "(from_id, to_id, genome) VALUES" + "(?,?,?) ON "
                + "CONFLICT DO NOTHING";
        try {
            stmtgenomes = connection.prepareStatement(querygenomes);
            int i = 0;
            for (Node node : nodes.values()) {
                for (Node link : node.getLinks()) {
                    Set<String> intersection = new HashSet<String>(node.getGenomes());
                    intersection.retainAll(link.getGenomes());

                    for (String genome : intersection) {
                        stmtgenomes.setInt(1, node.getId());
                        stmtgenomes.setInt(2, link.getId());
                        stmtgenomes.setString(THREE, genome.trim().replaceAll(" ", "-").replaceAll("_", "-"));
                        stmtgenomes.addBatch();
                        i++;
                        if (i % 1000 == 0) {
                            stmtgenomes.executeBatch();
                        }
                    }
                }
            }
            stmtgenomes.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmtgenomes != null) {
                stmtgenomes.close();
            }
        }
    }

    public void writeSpecimen(Collection<Subject> splist) throws SQLException {
        clearTable(SPECIMEN_TABLE);
        PreparedStatement stmtgenomes = null;
        String querygenomes = String.format("INSERT INTO %s(" +
                " specimen_id , age , sex , hiv_status , cohort , date_of_collection , " + " microscopy_smear_status "
                + "" + "" + "" + ", dna_isolation_single_colony_or_nonsingle_colony , phenotypic_dst_pattern , " +
                "capreomycin_10ugml , " + "" + "ethambutol_75ugml , ethionamide_10ugml , isoniazid_02ugml_or_1ugml "
                + ", kanamycin_6ugml , " +
                "pyrazinamide_nicotinamide_5000ugml_or_pzamgit , ofloxacin_2ugml , rifampin_1ugml , " +
                "streptomycin_2ugml , digital_spoligotype , lineage , genotypic_dst_pattern , " +
                "tugela_ferry_vs_nontugela_ferry_xdr, study_geographic_district, specimen_type" + ") VALUES(?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING", SPECIMEN_TABLE);
        try {
            stmtgenomes = connection.prepareStatement(querygenomes);
            int i = 0;
            for (Subject specimen : splist) {
                if (specimen.getNameId().equals("MT_H37RV_BRD_V5.ref")) {
                    continue;
                }
                stmtgenomes.setString(1, specimen.getNameId().replaceAll("-", "_").replaceAll(" ", "_"));
                stmtgenomes.setInt(2, specimen.getAge());
                stmtgenomes.setBoolean(3, specimen.isMale());
                stmtgenomes.setInt(4, specimen.getHivStatus());
                stmtgenomes.setString(5, specimen.getCohort());
                stmtgenomes.setString(6, specimen.getDate());
                stmtgenomes.setInt(7, specimen.getSmear());
                stmtgenomes.setBoolean(8, specimen.isSingleColony());
                stmtgenomes.setString(9, specimen.getPdstpattern());
                stmtgenomes.setString(10, specimen.getCapreomycin());
                stmtgenomes.setString(11, specimen.getEthambutol());
                stmtgenomes.setString(12, specimen.getEthionamide());
                stmtgenomes.setString(13, specimen.getIsoniazid());
                stmtgenomes.setString(14, specimen.getKanamycin());
                stmtgenomes.setString(15, specimen.getPyrazinamide());
                stmtgenomes.setString(16, specimen.getOfloxacin());
                stmtgenomes.setString(17, specimen.getRifampin());
                stmtgenomes.setString(18, specimen.getStreptomycin());
                stmtgenomes.setString(19, specimen.getSpoligotype());
                stmtgenomes.setString(20, specimen.getLineage());
                stmtgenomes.setString(21, specimen.getGdstPattern());
                stmtgenomes.setString(22, specimen.getXdr());
                stmtgenomes.setString(23, specimen.getDistrict());
                stmtgenomes.setString(24, specimen.getType());
                stmtgenomes.executeUpdate();
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
