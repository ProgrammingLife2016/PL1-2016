package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.parser.metadata.Specimen;
import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenCollection;
import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for creating a database to fetch from database.
 */
public class FetchDatabase implements Database {
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
    public FetchDatabase() {
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
     * @param  threshold threshold of graph that has to be fetched.
     * @return Json array of nodes in database
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public JSONArray fetchNodes(int threshold) throws SQLException {
        Statement stmt = null;
        JSONArray nodes = null;
        String query = "SELECT DISTINCT segments.* FROM segments, "
                + "(SELECT DISTINCT n1.id AS from, n2.id AS to "
                + "FROM segments AS n1 JOIN links ON n1.id = links.from_id "
                + "JOIN segments AS n2 ON n2.id = links.to_id WHERE links.threshold = "
                + threshold + ") sub WHERE sub.from = segments.id OR sub.to = segments.id "
                + "ORDER BY segments.id";


        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            nodes = convertResultNodesSetIntoJSON(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        return nodes;

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
     * fetch links for segments
     * @param threshold threshold of graph that has to be fetched
     * @return nodes
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    public JSONArray fetchLinks(int threshold) throws SQLException {
        Statement stmt = null;
        JSONArray links = null;
        String query = "SELECT DISTINCT n1.id as from, n1.x AS x1, "
                + "n1.y AS y1, n2.id as to, n2.x AS x2, n2.y AS y2 "
                + "FROM segments AS n1 JOIN links ON n1.id = links.from_id "
                + "JOIN segments AS n2 ON n2.id = links.to_id "
                + "WHERE links.threshold = " + threshold;
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            links = convertResultSetGenomesIntoJSON(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        return links;

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
                setSecondaryValuesSpecimen(specimen, rs);
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

    private void setSecondaryValuesSpecimen(Specimen specimen, ResultSet rs) throws SQLException {
        specimen.setCohort(rs.getString("cohort"));
        specimen.setDate(rs.getString("date_of_collection"));
        specimen.setDistrict(rs.getString("study_geographic_district"));
        specimen.setType(rs.getString("specimen_type"));
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
    }


    /**
     * Convert a result set into a JSON Array
     * @param resultSet ResultSet that has to be converted
     * @return a JSONArray
     * @throws Exception thrown if resultset is not valid
     */
    public JSONArray convertResultNodesSetIntoJSON(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int totalColumns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < totalColumns; i++) {
                String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
                Object columnValue = resultSet.getObject(i + 1);
                // if value in DB is null, then we set it to default value
                if (columnValue == null) {
                    columnValue = "null";
                }
                obj.put(columnName, columnValue);
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    /**
     * Convert a result set of genomes into a JSON Array
     * @param resultSet ResultSet that has to be converted
     * @return a JSONArray
     * @throws Exception thrown if resultset is not valid
     */
    public JSONArray convertResultSetGenomesIntoJSON(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int totalColumns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < totalColumns; i++) {
                String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
                Object columnValue = resultSet.getObject(i + 1);
                // if value in DB is null, then we set it to default value
                if (columnValue == null) {
                    columnValue = "null";
                }

                if (columnName.equals("from")) {
                    List<JSONArray> genomes = getGenomes(resultSet.getInt("from"),
                            resultSet.getInt("to"));
                    obj.put("genomes", genomes.get(0));
                    obj.put("lineages", genomes.get(1));
                    continue;
                }
                if (columnName.equals("to")) {
                    continue;
                }
                obj.put(columnName, columnValue);
            }

            jsonArray.put(obj);
        }
        return jsonArray;
    }

    private List<JSONArray> getGenomes(int from, int to) throws SQLException {
        Statement stmt = null;
        JSONArray genomes = new JSONArray();
        JSONArray lineages = new JSONArray();
        String query = "select DISTINCT genomeslinks.genome as genome, "
                + "specimen.lineage as lineage from genomeslinks "
                + "JOIN specimen ON genomeslinks.genome = specimen.specimen_id "
                + "WHERE from_id = " + from
                + " AND to_id = " + to;
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                    int best = rs.getMetaData().getColumnCount();
                    genomes.put(rs.getString(1));
                    lineages.put(rs.getString(2));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        List<JSONArray> list = new ArrayList<>();
        list.add(genomes);
        list.add(lineages);
        return list;

    }

}
