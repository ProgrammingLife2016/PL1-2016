package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.parser.metadata.Specimen;
import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenCollection;
import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for creating a database.
 */
public class SimpleDatabase implements Database {
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
    private static final String SEGMENT_TABLE = "segments";
    /**
     * Name of links table.
     */
    private static final String LINK_TABLE = "links";
    /**
     * Name of specimen table.
     */
    private static final String SPECIMEN_TABLE = "specimen";
    /**
     * The connection to the database.
     */
    private Connection connection;

    /**
     * Constructor to construct a database.
     */
    public SimpleDatabase() {
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
     * Fetch all segments in database.
     *
     * @return collection of nodes in database
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public NodeCollection fetchSegments() throws SQLException {
        NodeCollection nodes = new NodeMap();
        Statement stmt = null;
        String query = "select segment_id, data, column_index, positionx, "
                + "positiony "
                + "from " + SEGMENT_TABLE;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int segmentid = rs.getInt("segment_id");
                Node node = new Segment(segmentid,
                        rs.getString("data"), rs.getInt("column_index"));
                node.setXY(rs.getInt("positionx"), rs.getInt("positiony"));
                nodes.put(segmentid, node);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        return fetchLinks(nodes);

    }

    /**
     * Fetch all positions of segments in nodeCollection in database.
     * @param  nodeCollection The nodecollection for which the positions should be fetched
     * @return collection of nodes in database
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public NodeCollection fetchPositions(NodeCollection nodeCollection) throws SQLException {
        for (Node node: nodeCollection.values()) {
            List<Integer> positions = fetchPosition(node.getId());
            node.setXY(positions.get(0), positions.get(1));
        }
        return nodeCollection;

    }

    /**
     * Fetch position of segment based on id of the segment.
     *
     * @param id the id of the segment
     * @return the positions as List<Integer>
     */
    private List<Integer> fetchPosition(int id) {
        Statement stmt = null;
        String query = "select positionx, "
                + "positiony "
                + "from " + SEGMENT_TABLE
                + " WHERE segment_id='" + id + "'";
        List<Integer> res = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                int x = rs.getInt("positionx");
                int y = rs.getInt("positiony");
                res.addAll(Arrays.asList(x, y));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * Update the positions of the nodeCollection into the database
     * @param nodeCollection the nodecollection with the correct positions
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public void updatePositions(NodeCollection nodeCollection) throws SQLException {
        for (Node node : nodeCollection.values()) {
            updatePosition(node.getId(), node.getX(), node.getY());
        }
    }
    /**
     * Update the positions of the node whit id id into the database
     * @param id the id of the node to update
     * @param x the correct x for the node
     * @param y the correct y for the node
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public void updatePosition(int id, int x, int y) throws SQLException {
        Statement stmt = null;
        String query = "UPDATE " + SEGMENT_TABLE
                + " SET positionx = '"
                + x + "', positiony = '"
                + y + "' WHERE segment_id = '"
                + id + "'";
        try {
            System.out.println(query);
            stmt = connection.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    /**
     * fetch links for segments
     *
     * @param nodes nodes to which the links will be added
     * @return nodes
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    private NodeCollection fetchLinks(NodeCollection nodes) throws SQLException {
        Statement stmt = null;
        String query = "select to_id "
                + "from " + LINK_TABLE
                + " WHERE from_id='";
        try {
            stmt = connection.createStatement();
            for (Node node : nodes.values()) {
                ResultSet rs = stmt.executeQuery(query + node.getId() + "'");
                while (rs.next()) {
                    int segmentid = rs.getInt("to_id");
                    node.addLink(nodes.get(segmentid));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        return nodes;

    }

    /**
     * Fetch segment by id
     *
     * @param id id of the node to be fetched
     * @return the node with id id
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public Node fetchSegment(int id) throws SQLException {
        Node node = null;
        Statement stmt = null;
        String query = "select segment_id, data, column_index, positionx, "
                + "positiony "
                + "from " + SEGMENT_TABLE
                + " WHERE segment_id='" + id + "'";
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                int segmentid = rs.getInt("segment_id");
                node = new Segment(segmentid, rs.getString("data"), rs.getInt("column_index"));
                node.setXY(rs.getInt("positionx"), rs.getInt("positiony"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        return node;
    }

    /**
     * Write a collection of segments into the database
     *
     * @param nodes the collection to write
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public void writeSegments(NodeCollection nodes) throws SQLException {
        clearTable(SEGMENT_TABLE);
        writeLinks(nodes);
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + SEGMENT_TABLE
                + "(segment_id, data, column_index, positionx, positiony) VALUES"
                + "(?,?,?,?,?)";
        try {
            stmt = connection.prepareStatement(query);
            for (Node node : nodes.values()) {
                stmt.setInt(1, node.getId());
                stmt.setString(2, node.getData());
                stmt.setInt(3, node.getColumn());
                stmt.setInt(4, node.getX());
                stmt.setInt(5, node.getY());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
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

    private void writeLinks(NodeCollection nodes) throws SQLException {
        clearTable(LINK_TABLE);
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + LINK_TABLE
                + "(from_id, to_id) VALUES"
                + "(?,?)";
        try {
            stmt = connection.prepareStatement(query);
            for (Node node : nodes.values()) {
                for (Node link : node.getLinks()) {
                    stmt.setInt(1, node.getId());
                    stmt.setInt(2, link.getId());
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

    /**
     * Fetch all specimen from database
     *
     * @return the collection of specimen
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    @SuppressWarnings("checkstyle:methodlength")
    public SpecimenCollection fetchSpecimens() throws SQLException {
        SpecimenCollection specimens = new SpecimenMap();
        Statement stmt = null;
        String query = "select specimen_id , age , sex , "
                + "hiv_status , cohort , date_of_collection , "
                + "study_geographic_district , specimen_type , "
                + "microscopy_smear_status , dna_isolation_single_colony_or_nonsingle_colony , "
                + "phenotypic_dst_pattern , capreomycin_10ugml , "
                + "ethambutol_75ugml , ethionamide_10ugml , "
                + "isoniazid_02ugml_or_1ugml , kanamycin_6ugml , "
                + "pyrazinamide_nicotinamide_5000ugml_or_pzamgit , "
                + "ofloxacin_2ugml , rifampin_1ugml , streptomycin_2ugml , "
                + "digital_spoligotype , lineage , "
                + "genotypic_dst_pattern , tugela_ferry_vs_nontugela_ferry_xdr "
                + "from " + SPECIMEN_TABLE;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Specimen specimen = new Specimen();
                specimen.setNameId(rs.getString("specimen_id"));
                if (rs.getString("age").equals("unknown")) {
                    specimen.setAge(0);
                } else {
                    specimen.setAge(Integer.parseInt(rs.getString("age")));
                }
                if (rs.getString("sex").equals("Male")) {
                    specimen.setMale(true);
                } else {
                    specimen.setMale(false);
                }
                if (rs.getString("hiv_status").equals("Positive")) {
                    specimen.setHivStatus(1);
                } else if (rs.getString("hiv_status").equals("Negative")) {
                    specimen.setHivStatus(-1);
                } else {
                    specimen.setHivStatus(0);
                }
                specimen.setCohort(rs.getString("cohort"));
                specimen.setDate(rs.getString("date_of_collection"));
                specimen.setDistrict(rs.getString("study_geographic_district"));
                specimen.setType(rs.getString("specimen_type"));
                if (rs.getString("microscopy_smear_status").equals("Positive")) {
                    specimen.setSmear(1);
                } else if (rs.getString("microscopy_smear_status").equals("Negative")) {
                    specimen.setSmear(-1);
                } else {
                    specimen.setSmear(0);
                }
                if (rs.getString("dna_isolation_single_colony_or_nonsingle_colony")
                        .equals("single colony")) {
                    specimen.setSingleColony(true);
                } else {
                    specimen.setSingleColony(false);
                }
                specimen.setPdstpattern(rs.getString("phenotypic_dst_pattern"));
                specimen.setCapreomycin(rs.getString("capreomycin_10ugml").charAt(0));
                specimen.setEthambutol(rs.getString("ethambutol_75ugml").charAt(0));
                specimen.setEthionamide(rs.getString("ethionamide_10ugml").charAt(0));
                specimen.setIsoniazid(rs.getString("isoniazid_02ugml_or_1ugml").charAt(0));
                specimen.setKanamycin(rs.getString("kanamycin_6ugml").charAt(0));
                specimen.setPyrazinamide(rs
                        .getString("pyrazinamide_nicotinamide_5000ugml_or_pzamgit")
                        .charAt(0));
                specimen.setOfloxacin(rs.getString("ofloxacin_2ugml").charAt(0));
                specimen.setRifampin(rs.getString("rifampin_1ugml").charAt(0));
                specimen.setStreptomycin(rs.getString("streptomycin_2ugml").charAt(0));
                specimen.setSpoligotype(rs.getString("digital_spoligotype").charAt(0));
                specimen.setLineage(rs.getString("lineage").charAt(0));
                specimen.setGdstPattern(rs.getString("genotypic_dst_pattern"));
                specimen.setXdr(rs.getString("tugela_ferry_vs_nontugela_ferry_xdr"));
                specimens.put(specimen.getNameId(), specimen);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        return specimens;
    }

}
