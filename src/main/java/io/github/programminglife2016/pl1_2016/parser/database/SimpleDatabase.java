package io.github.programminglife2016.pl1_2016.parser.database;

import io.github.programminglife2016.pl1_2016.parser.metadata.Specimen;
import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenCollection;
import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeList;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;

import java.sql.*;
import java.util.Iterator;

public class SimpleDatabase implements Database {

    private static final String DATABASE_DRIVER = "org.postgresql.Driver";

    private static final String HOST = "jdbc:postgresql://localhost:5432/pl1";

    private static final String ROLL = "pl";

    private static final String PASSWORD = "visual";

    private static final String SEGMENT_TABLE = "segments";

    private static final String LINK_TABLE = "links";

    private static final String SPECIMEN_TABLE = "specimen";

    private Connection connection;

    public SimpleDatabase() {
        connect();
    }

    public void connect() {
        try {
            Class.forName(DATABASE_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not included in dependencies. Update Maven!");
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(HOST, ROLL, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = connection;
    }

    public NodeCollection fetchSegments() throws SQLException {
        NodeCollection nodes = new NodeList(95000);
        Statement stmt = null;
        String query = "select segment_id, data, column_index, positionx, " +
                "positiony " +
                "from " + SEGMENT_TABLE;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int segment_id = rs.getInt("segment_id");
                Node node = new Segment(segment_id, rs.getString("data"), rs.getInt("column_index"));
                node.setXY(rs.getInt("positionx"), rs.getInt("positiony"));
                nodes.put(segment_id, node);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            return fetchLinks(nodes);
        }
    }

    private NodeCollection fetchLinks(NodeCollection nodes) throws SQLException {
        Statement stmt = null;
        String query = "select to_id " +
                "from " + LINK_TABLE +
                " WHERE from_id='";
        try {
            stmt = connection.createStatement();
            for (Node node :
                    nodes) {
                ResultSet rs = stmt.executeQuery(query + node.getId() + "'");
                while (rs.next()) {
                    int segment_id = rs.getInt("to_id");
                    node.addLink(nodes.get(segment_id));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            return nodes;
        }
    }

    public Node fetchSegment(int id) throws SQLException {
        Node node = null;
        Statement stmt = null;
        String query = "select segment_id, data, column_index, positionx, " +
                "positiony " +
                "from " + SEGMENT_TABLE +
                " WHERE segment_id='" + id + "'";
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int segment_id = rs.getInt("segment_id");
            node = new Segment(segment_id, rs.getString("data"), rs.getInt("column_index"));
            node.setXY(rs.getInt("positionx"), rs.getInt("positiony"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            return node;
        }
    }

    public void writeSegments(NodeCollection nodes) throws SQLException {
        writeLinks(nodes);
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + SEGMENT_TABLE
                + "(segment_id, data, column_index, positionx, positiony) VALUES"
                + "(?,?,?,?,?)";
        try {
            stmt = connection.prepareStatement(query);
            Iterator<Node> iterator = nodes.getNodes().iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
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

    private void writeLinks(NodeCollection nodes) throws SQLException {
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + LINK_TABLE
                + "(from_id, to_id) VALUES"
                + "(?,?)";
        try {
            stmt = connection.prepareStatement(query);
            Iterator<Node> iterator = nodes.getNodes().iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
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

    public SpecimenCollection fetchSpecimens() throws SQLException {
        SpecimenCollection specimens = new SpecimenMap();
        Statement stmt = null;
        String query = "select specimen_id , age , sex , " +
                "hiv_status , cohort , date_of_collection , " +
                "study_geographic_district , specimen_type , " +
                "microscopy_smear_status , dna_isolation_single_colony_or_nonsingle_colony , " +
                "phenotypic_dst_pattern , capreomycin_10ugml , ethambutol_75ugml , ethionamide_10ugml , " +
                "isoniazid_02ugml_or_1ugml , kanamycin_6ugml , pyrazinamide_nicotinamide_5000ugml_or_pzamgit , " +
                "ofloxacin_2ugml , rifampin_1ugml , streptomycin_2ugml , digital_spoligotype , lineage , " +
                "genotypic_dst_pattern , tugela_ferry_vs_nontugela_ferry_xdr " +
                "from " + SPECIMEN_TABLE;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Specimen specimen = new Specimen();
                specimen.setNameId(rs.getString("specimen_id"));
                if(rs.getString("age").equals("unknown")) {
                    specimen.setAge(0);
                } else {
                    specimen.setAge(Integer.parseInt(rs.getString("age")));
                }
                if(rs.getString("sex").equals("Male")){
                    specimen.setMale(true);
                } else {
                    specimen.setMale(false);
                }
                if(rs.getString("hiv_status").equals("Positive")){
                    specimen.setHivStatus(1);
                } else if(rs.getString("hiv_status").equals("Negative")) {
                    specimen.setHivStatus(-1);
                }else {
                    specimen.setHivStatus(0);
                }
                specimen.setCohort(rs.getString("cohort"));
                specimen.setDate(rs.getString("date_of_collection"));
                specimen.setDistrict(rs.getString("study_geographic_district"));
                specimen.setType(rs.getString("specimen_type"));
                if(rs.getString("microscopy_smear_status").equals("Positive")){
                    specimen.setSmear(1);
                } else if(rs.getString("microscopy_smear_status").equals("Negative")) {
                    specimen.setSmear(-1);
                }else {
                    specimen.setSmear(0);
                }
                if(rs.getString("dna_isolation_single_colony_or_nonsingle_colony").equals("single colony")){
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
                specimen.setPyrazinamide(rs.getString("pyrazinamide_nicotinamide_5000ugml_or_pzamgit").charAt(0));
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
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            return specimens;
        }
    }

    /*
    public void WriteSpecimens(SpecimenCollection specimen) {
        PreparedStatement stmt = null;
        String query = "INSERT INTO " + SPECIMEN_TABLE
                + "(specimen_id , age , sex , " +
                "hiv_status , cohort , date_of_collection , " +
                "study_geographic_district , specimen_type , " +
                "microscopy_smear_status , dna_isolation_single_colony_or_nonsingle_colony , " +
                "phenotypic_dst_pattern , capreomycin_10ugml , ethambutol_75ugml , ethionamide_10ugml , " +
                "isoniazid_02ugml_or_1ugml , kanamycin_6ugml , pyrazinamide_nicotinamide_5000ugml_or_pzamgit , " +
                "ofloxacin_2ugml , rifampin_1ugml , streptomycin_2ugml , digital_spoligotype , lineage , " +
                "genotypic_dst_pattern , tugela_ferry_vs_nontugela_ferry_xdr) " + "VALUES "
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            stmt = connection.prepareStatement(query);
            Iterator<Subject> iterator = specimen.getSpecimen().iterator();
            while (iterator.hasNext()) {
                Subject specimen = iterator.next();
                stmt.setString(1, specimen.getNameId());
                stmt.setInt(2, specimen.getAge());
                if(specimen.isMale()){
                    stmt.setInt(3, specimen.gets);
                } else {

                }
                stmt.setInt(4, node);
                stmt.setInt(5, node);
                stmt.setInt(6, node);
                stmt.setInt(7, node);
                stmt.setInt(8, node);
                stmt.setInt(9, node);
                stmt.setInt(10, node);
                stmt.setInt(11, node);
                stmt.setInt(12, node);
                stmt.setInt(13, node);
                stmt.setInt(14, node);
                stmt.setInt(15, node);
                stmt.setInt(16, node);
                stmt.setInt(17, node);
                stmt.setInt(18, node);
                stmt.setInt(19, node);
                stmt.setInt(20, node);
                stmt.setInt(21, node);
                stmt.setInt(22, node);
                stmt.setInt(23, node);
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
    */

}
