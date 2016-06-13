package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.parser.metadata.Specimen;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for creating a database to fetch from database.
 */
public class FetchDatabase implements Database {

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
     * Fetch all nodes in database.
     *
     * @param x1        the left bounding x.
     * @param x2        the right bounding x.
     * @param threshold threshold of graph that has to be fetched.
     * @return Json array of nodes in database
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    private JSONArray fetchNodes(int threshold, int x1, int x2) throws SQLException {
        Statement stmt = null;
        JSONArray nodes = null;
        String query = String.format("SELECT DISTINCT %s.* FROM %s, (SELECT DISTINCT n1.id AS from, n2.id AS to FROM "
                + "" + "%s AS n1 JOIN %s ON n1.id = %s.from_id JOIN %s AS n2 ON n2.id = %s.to_id WHERE %s.threshold ="
                + " %d AND" + " ((n1.x >= %d AND n1.x <= %d) OR (n2.x >= %d AND n2.x <= %d)))sub WHERE sub.from = %s"
                + ".id OR sub.to =" + " %s.id ORDER BY %s.id", NODES_TABLE, NODES_TABLE, NODES_TABLE, LINK_TABLE,
                LINK_TABLE, NODES_TABLE, LINK_TABLE, LINK_TABLE, threshold, x1, x2, x1, x2, NODES_TABLE, NODES_TABLE,
                NODES_TABLE);
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            nodes = convertResultSetIntoJSON(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        return nodes;
    }

    /**
     * Convert data fetched from the database to JSON.
     *
     * @param id id of node.
     * @return JSON response.
     */
    public JSONArray getData(int id) {
        return null;
    }

    /**
     * Convert data fetched from the server to JSON.
     *
     * @param threshold level of treshold.
     * @return JSON response.
     */
    public final JSONObject getNodes(int threshold, int x1, int x2) {
        JSONObject result = new JSONObject();
        result.put("status", "success");
        try {
            result.put("nodes", fetchNodes(threshold, x1, x2));
            result.put("edges", fetchLinks(threshold, x1, x2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Convert data fetched from the server to JSON.
     *
     * @param genome id of genome.
     * @param index  index of base.
     * @return JSON response.
     */
    public final JSONObject getXOnNavigate(String genome, int index) {
        JSONObject result = new JSONObject();
        result.put("status", "success");
        result.put("x", "NOT IMPLEMENTED YET");
        return result;
    }

    /**
     * Convert metadata fetched from the server to JSON.
     *
     * @param genome id of genome.
     * @return JSON response.
     */
    public final JSONObject getMetadataOfGenome(String genome) {
        JSONObject result = new JSONObject();
        result.put("status", "success");
        result.put("subject", fetchMetadata(genome));
        return result;
    }

    private JSONObject fetchMetadata(String genome) {
        Statement stmt;
        String query = String.format("select * from %s WHERE specimen_id='%s'", SPECIMEN_TABLE, genome);
        ResultSet rs;
        JSONObject res = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            res = convertResultSetIntoJSONString(rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * fetch links for nodes
     *
     * @param threshold threshold of graph that has to be fetched
     * @return nodes
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    private JSONArray fetchLinks(int threshold, int x1, int x2) throws SQLException {
        Statement stmt = null;
        JSONArray links = null;
        String query = String.format("SELECT DISTINCT n1.id as from, n1.x AS x1, n1.y AS y1, n2.id as to, n2.x AS x2,"
                + "" + " n2.y AS y2 FROM %s AS n1 JOIN %s ON n1.id = %s.from_id JOIN %s AS n2 ON n2.id = %s.to_id "
                + "WHERE %s" + ".threshold = %d AND ((n1.x >= %d AND n1.x <= %d) OR (n2.x >= %d AND n2.x <= %d))",
                NODES_TABLE, LINK_TABLE, LINK_TABLE, NODES_TABLE, LINK_TABLE, LINK_TABLE, threshold, x1, x2, x1, x2);
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            links = convertResultSetGenomesIntoJSON(rs);
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
    public final Map<String, Subject> fetchSpecimens() throws SQLException {
        Map<String, Subject> specimens = new HashMap<>();
        Statement stmt = null;
        String query = getFetchQuery();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Specimen specimen = new Specimen();
                specimen.setNameId(rs.getString("specimen_id"));
                setSpecimenAge(rs, specimen);
                specimen.setMale(rs.getString("sex").equals("Male"));
                setSpecimenHivStatus(rs, specimen);
                setSpecimenSmearStatus(rs, specimen);
                specimen.setSingleColony(rs.getString("dna_isolation_single_colony_or_nonsingle_colony").equals
                        ("single colony"));
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

    private void setSpecimenSmearStatus(ResultSet rs, Specimen specimen) throws SQLException {
        if (rs.getString("microscopy_smear_status").equals("Positive")) {
            specimen.setSmear(1);
        } else if (rs.getString("microscopy_smear_status").equals("Negative")) {
            specimen.setSmear(-1);
        } else {
            specimen.setSmear(0);
        }
    }

    private void setSpecimenHivStatus(ResultSet rs, Specimen specimen) throws SQLException {
        if (rs.getString("hiv_status").equals("Positive")) {
            specimen.setHivStatus(1);
        } else if (rs.getString("hiv_status").equals("Negative")) {
            specimen.setHivStatus(-1);
        } else {
            specimen.setHivStatus(0);
        }
    }

    private void setSpecimenAge(ResultSet rs, Specimen specimen) throws SQLException {
        if (rs.getString("age").equals("unknown")) {
            specimen.setAge(0);
        } else {
            specimen.setAge(Integer.parseInt(rs.getString("age")));
        }
    }

    private String getFetchQuery() {
        return String.format("select specimen_id , age , sex , hiv_status , cohort , date_of_collection , "
                + "study_geographic_district , specimen_type , microscopy_smear_status , "
                + "dna_isolation_single_colony_or_nonsingle_colony , phenotypic_dst_pattern , capreomycin_10ugml , "
                + "ethambutol_75ugml , ethionamide_10ugml , isoniazid_02ugml_or_1ugml , kanamycin_6ugml , "
                + "pyrazinamide_nicotinamide_5000ugml_or_pzamgit , ofloxacin_2ugml , rifampin_1ugml , "
                + "streptomycin_2ugml , digital_spoligotype , lineage , genotypic_dst_pattern , "
                + "tugela_ferry_vs_nontugela_ferry_xdr from %s", SPECIMEN_TABLE);
    }

    private void setSecondaryValuesSpecimen(Specimen specimen, ResultSet rs) throws SQLException {
        specimen.setCohort(rs.getString("cohort")).setDate(rs.getString("date_of_collection")).setDistrict(rs
                .getString("study_geographic_district")).setType(rs.getString("specimen_type")).setPdstpattern(rs
                .getString("phenotypic_dst_pattern")).setCapreomycin(rs.getString("capreomycin_10ugml"))
                .setEthambutol(rs.getString("ethambutol_75ugml")).setEthionamide(rs.getString("ethionamide_10ugml"))
                .setIsoniazid(rs.getString("isoniazid_02ugml_or_1ugml")).setKanamycin(rs.getString("kanamycin_6ugml")
        ).setPyrazinamide(rs.getString("pyrazinamide_nicotinamide_5000ugml_or_pzamgit")).setOfloxacin(rs.getString
                ("ofloxacin_2ugml")).setRifampin(rs.getString("rifampin_1ugml")).setStreptomycin(rs.getString
                ("streptomycin_2ugml")).setSpoligotype(rs.getString("digital_spoligotype")).setLineage(rs.getString
                ("lineage")).setGdstPattern(rs.getString("genotypic_dst_pattern")).setXdr(rs.getString
                ("tugela_ferry_vs_nontugela_ferry_xdr"));
    }


    /**
     * Convert a result set into a JSON Array
     *
     * @param resultSet ResultSet that has to be converted
     * @return a JSONArray
     * @throws Exception thrown if resultset is not valid
     */
    private JSONArray convertResultSetIntoJSON(ResultSet resultSet) {
        JSONArray jsonArray = new JSONArray();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * Convert a result set into a JSON Array
     *
     * @param resultSet ResultSet that has to be converted
     * @return a JSONArray
     * @throws Exception thrown if resultset is not valid
     */
    private JSONObject convertResultSetIntoJSONString(ResultSet resultSet) {
        JSONObject jsonObject = new JSONObject();
        try {
            while (resultSet.next()) {
                String specimen = "";
                int totalColumns = resultSet.getMetaData().getColumnCount();
                JSONObject obj = new JSONObject();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < totalColumns; i++) {

                    String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);
                    // if value in DB is null, then we set it to default value
                    if (columnValue == null) {
                        columnValue = "null";
                    }
                    if (columnName.equals("specimen_id")) {
                        specimen = (String) columnValue;
                    }
                    sb.append(columnName + ":" + columnValue +", ");
                }
                jsonObject.put(specimen, sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Convert a result set of genomes into a JSON Array
     *
     * @param resultSet ResultSet that has to be converted
     * @return a JSONArray
     * @throws Exception thrown if resultset is not valid
     */
    private JSONArray convertResultSetGenomesIntoJSON(ResultSet resultSet) throws Exception {
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
                    List<JSONArray> genomes = getGenomes(resultSet.getInt("from"), resultSet.getInt("to"));
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
        String query = String.format("select DISTINCT %s.genome as genome, specimen.lineage as lineage from %s JOIN "
                + "specimen ON %s.genome = specimen.specimen_id WHERE from_id = %d AND to_id = %d",
                LINK_GENOMES_TABLE, LINK_GENOMES_TABLE, LINK_GENOMES_TABLE, from, to);
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                genomes.put(rs.getString(1));
                lineages.put(rs.getString(2));
            }


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

    /**
     * Get the lineage from the database with a specific specimen id.
     *
     * @param genome the speciment id.
     * @return lineage
     * @throws SQLException thrown if query fails.
     */
    public final String getLineage(String genome) throws SQLException {
        String query = String.format("SELECT lineage FROM specimen WHERE specimen_id = '%s'", genome);
        Statement stmt = null;
        ResultSet rs;
        String lineage = "";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            rs.next();
            lineage = rs.getString("lineage");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return lineage;
    }

}
