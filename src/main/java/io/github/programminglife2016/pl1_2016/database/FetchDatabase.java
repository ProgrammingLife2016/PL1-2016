package io.github.programminglife2016.pl1_2016.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class for creating a database to fetch from database.
 */
public class FetchDatabase implements Database {
    private static final String[] SPECIMEN_COLUMNS = {"age", "sex", "hiv_status", "cohort", "date_of_collection",
            "study_geographic_district", "specimen_type", "microscopy_smear_status",
            "dna_isolation_single_colony_or_nonsingle_colony", "phenotypic_dst_pattern", "capreomycin_10ugml",
            "ethambutol_75ugml", "ethionamide_10ugml", "isoniazid_02ugml_or_1ugml", "kanamycin_6ugml",
            "pyrazinamide_nicotinamide_5000ugml_or_pzamgit", "ofloxacin_2ugml", "rifampin_1ugml",
            "streptomycin_2ugml", "digital_spoligotype", "lineage", "genotypic_dst_pattern",
            "tugela_ferry_vs_nontugela_ferry_xdr"};
    private static final int ANNOTATION_THRESHOLD = 4;

    /**
     * The connection to the database.
     */
    private Connection connection;
    private String dataset;
    private HashMap<String, String> genomeToLineage;

    /**
     * Constructor to construct a database.
     *
     * @param dataset dataset.
     */
    public FetchDatabase(String dataset) {
        this.dataset = dataset;
        connect();
        this.genomeToLineage = getGenomeToLineages();
    }

    private HashMap<String, String> getGenomeToLineages() {
        Statement stmt = null;
        HashMap<String, String> lineages = new HashMap<>();
        String query = String.format("SELECT specimen_id, lineage FROM %s", SPECIMEN_TABLE);
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String genome = rs.getString(1);
                String lineage = rs.getString(2);
                lineages.put(genome, lineage);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lineages;
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
     * Fetch all nodes in database.
     *
     * @param threshold        threshold of graph that has to be fetched.
     * @param x1               the left bounding x.
     * @param x2               the right bounding x.
     * @param minContainerSize minimal container size of nodes to be displayed
     * @return Json array of nodes in database
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    private JSONArray fetchNodes(int threshold, int x1, int x2, int minContainerSize) throws SQLException {
        Statement stmt = null;
        JSONArray nodes = null;
        String query = String.format("SELECT DISTINCT %s.* " + "FROM %s, (SELECT DISTINCT n1.id AS from, n2.id AS to "
                + "" + "" + "" + "FROM " + "%s AS n1 JOIN %s ON n1.id = %s.from_id " + "JOIN %s AS n2 ON n2.id = %s"
                + ".to_id " + "WHERE" + " %s" + ".threshold =" + " %d AND" + " ((n1.x >= %d AND n1.x <= %d) " + "OR "
                + "(n2.x >= %d " + "AND n2.x <=" + " %d)))" + "sub " + "WHERE (sub.from = %s" + ".id OR sub.to =" + ""
                + " %s.id) AND %s" + ".containersize > " + "%d" + " ORDER" + " BY %s.id", NODES_TABLE, NODES_TABLE,
                NODES_TABLE, LINK_TABLE, LINK_TABLE, NODES_TABLE, LINK_TABLE, LINK_TABLE, threshold, x1, x2, x1, x2,
                NODES_TABLE, NODES_TABLE, NODES_TABLE, minContainerSize, NODES_TABLE);
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            nodes = convertResultSetIntoJSON(rs);
            rs.close();
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
     * @param threshold        level of treshold.
     * @param x1               the left bounding x.
     * @param x2               the right bounding x.
     * @param minContainerSize minimal container size of the node to be displayed
     * @param items            items
     * @return JSON response.
     */
    public final JSONObject getNodes(int threshold, int x1, int x2, int minContainerSize, HashMap<Integer,
            ArrayList<String>> items) {
        JSONObject result = new JSONObject();
        result.put("status", "success");
        try {
            result.put("nodes", fetchNodes(threshold, x1, x2, minContainerSize));
            if (threshold <= ANNOTATION_THRESHOLD) {
                result.put("annotations", fetchAnnotations());
            } else {
                result.put("annotations", new JSONArray());
            }
            result.put("edges", fetchLinks(threshold, x1, x2, items));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Convert data fetched from the server to JSON.
     *
     * @return JSON response.
     */
    public final JSONObject getAnnotations() {
        JSONObject result = new JSONObject();
        result.put("status", "success");
        result.put("annotations", fetchAnnotations());
        return result;
    }

    public JSONObject fetchAmino(int bubbleId) {
        JSONObject jsonObject = new JSONObject();
        try {
            String query = String.format("SELECT DISTINCT id FROM %s WHERE startin LIKE '%%/%d/%%'",
                PRIMITIVES_TABLE, bubbleId);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                query = "SELECT data FROM aminos WHERE segid = " + rs.getInt(1);
                statement = connection.createStatement();
                rs = statement.executeQuery(query);
                 if (rs.next()) {
                     jsonObject.put("data", rs.getString(1));
                 } else {
                     jsonObject.put("data", "no mutation in amino acids");
                 }
            } else {
                jsonObject.put("data", "no mutation in amino acids");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
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

    public final JSONObject getMetadatas() {
        JSONObject result = new JSONObject();
        result.put("status", "success");
        result.put("subjects", fetchMetadatas());
        return result;
    }

    private JSONArray fetchMetadata(String genome) {
        Statement stmt;
        String query = String.format("select * from %s WHERE specimen_id='%s'", SPECIMEN_TABLE, genome);
        ResultSet rs;
        JSONArray res = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            res = convertResultSetMetadataIntoJSON(rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private JSONArray fetchMetadatas() {
        Statement stmt;
        String query = String.format("select * from %s", SPECIMEN_TABLE);
        ResultSet rs;
        JSONArray res = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            res = convertResultSetMetadataIntoJSON(rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private JSONArray convertResultSetMetadataIntoJSON(ResultSet resultSet) {
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
                    columnValue = getObject(columnName, columnValue);
                    obj.put(columnName, columnValue);
                }
                jsonArray.put(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private Object getObject(String columnName, Object columnValue) {
        if (columnName.equals("dna_isolation_single_colony_or_nonsingle_colony")) {
            if (columnValue.equals("true")) {
                columnValue = "single colony";
            } else if (columnValue.equals("false")) {
                columnValue = "non single_colony";
            } else {
                columnValue = "Unknown";
            }
        }
        if (columnName.equals("hiv_status") | columnName.equals("microscopy_smear_status")) {
            if (columnValue.equals("1")) {
                columnValue = "Positive";
            } else if (columnValue.equals("-1")) {
                columnValue = "Negative";
            } else {
                columnValue = "Unknown";
            }
        }
        if (columnName.equals("sex")) {
            if (columnValue.equals("true")) {
                columnValue = "Male";
            } else if (columnValue.equals("false")) {
                columnValue = "Female";
            } else {
                columnValue = "Unknown";
            }
        }
        return columnValue;
    }

    /**
     * fetch links for nodes
     *
     * @param threshold threshold of graph that has to be fetched
     * @param items     items
     * @return nodes
     * @throws SQLException thrown if SQL connection or query is not valid
     */
    private JSONArray fetchLinks(int threshold, int x1, int x2, HashMap<Integer, ArrayList<String>> items) throws
            SQLException {
        Statement stmt = null;
        JSONArray links = null;
        String query = String.format("SELECT DISTINCT n1.x AS x1, n1.y AS y1, n2.x AS x2," + " n2.y AS y2, %s" + "" +
                ".genomes" + " " + "FROM %s AS n1 JOIN %s ON n1.id = %s.from_id JOIN %s AS n2 ON n2.id = %s" + "" +
                ".to_id " +
                "WHERE %s" + ".threshold = %d " + "AND ((n1.x >= %d AND n1.x <= %d) OR (n2.x >= %d AND n2.x " + "<= "
                + "%d) OR (n1.x <= %d AND n2.x >= %d))", LINK_TABLE, NODES_TABLE, LINK_TABLE, LINK_TABLE,
                NODES_TABLE, LINK_TABLE, LINK_TABLE, threshold, x1, x2, x1, x2, x1, x2);
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            links = convertResultSetGenomesIntoJSON(rs, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stmt != null) {
            stmt.close();
        }
        return links;

    }

    private JSONArray fetchAnnotations() {
        String query = String.format("SELECT * FROM %s", ANNOTATIONS_TABLE);
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            return convertResultSetIntoJSON(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public JSONObject fetchPrimitives(int bubbleId) {
        String startQuery = String.format("SELECT DISTINCT data FROM %s WHERE startin LIKE '%%/%d/%%'",
                PRIMITIVES_TABLE, bubbleId);
        String endQuery = String.format("SELECT DISTINCT data FROM %s WHERE endin LIKE '%%/%d/%%'", PRIMITIVES_TABLE,
                bubbleId);
        String contQuery = String.format("SELECT DISTINCT data FROM %s WHERE contin LIKE '%%/%d/%%'",
                PRIMITIVES_TABLE, bubbleId);
        Statement stmt;
        JSONObject jsonObject = new JSONObject();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(startQuery);
            rs.next();
            jsonObject.put("startdata", rs.getString(1).equals("NULL") ? "" : rs.getString(1));

            stmt = connection.createStatement();
            rs = stmt.executeQuery(endQuery);
            if (rs.next()) {
                jsonObject.put("enddata", rs.getString(1).equals("NULL") ? "" : rs.getString(1));
            }

            stmt = connection.createStatement();
            rs = stmt.executeQuery(contQuery);
            JSONArray container = new JSONArray();
            while (rs.next()) {
                container.put(rs.getString(1));
            }
            if (container.length() == 1) {
                container.put("");
            }
            jsonObject.put("contdata", container);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Convert a result set into a JSON Array
     *
     * @param resultSet ResultSet that has to be converted
     * @return a JSONArray
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
     * Convert a result set of genomes into a JSON Array
     *
     * @param resultSet ResultSet that has to be converted
     * @param items     items
     * @return a JSONArray
     * @throws Exception thrown if resultset is not valid
     */
    private JSONArray convertResultSetGenomesIntoJSON(ResultSet resultSet, HashMap<Integer, ArrayList<String>> items)
            throws Exception {
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

                if (columnName.equals("genomes")) {
                    String genoms = ((String) columnValue);
                    genoms = genoms.substring(1, genoms.length() - 1);
                    JSONArray gens = new JSONArray();
                    ArrayList<String> mostCommon = new ArrayList<>();
                    for (String lingen : genoms.split(",")) {
                        lingen = lingen.trim();
                        mostCommon.add(genomeToLineage.getOrDefault(lingen, ""));
                    }

                    for (int key : items.keySet()) {
                        for (String genome : items.get(key)) {
                            if (genoms.toLowerCase().contains(genome.toLowerCase())) {
                                gens.put(key);
                                break;
                            }
                        }
                    }
                    obj.put("genomes", mostCommon.size());
                    obj.put("highlight", gens);
                    obj.put("lineages", mostCommonElement(mostCommon));
                    continue;
                }
                obj.put(columnName, columnValue);
            }

            jsonArray.put(obj);
        }
        return jsonArray;
    }


    private static String mostCommonElement(List<String> list) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String aList : list) {
            Integer frequency = map.get(aList);
            if (frequency == null) {
                map.put(aList, 1);
            } else {
                map.put(aList, frequency + 1);
            }
        }

        String mostCommonKey = null;
        int maxValue = -1;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            if (entry.getValue() > maxValue) {
                mostCommonKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }

        return mostCommonKey;
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

    private JSONObject fetchOptions() throws SQLException {
        JSONObject options = new JSONObject();

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            String query;
            for (String column : SPECIMEN_COLUMNS) {
                JSONArray values = new JSONArray();
                query = String.format("SELECT DISTINCT %s FROM specimen", column);
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Object value = rs.getObject(column);
                    value = getObject(column, value);
                    values.put(value);
                }
                options.put(column, values);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                rs.close();
                stmt.close();
            }
        }
        return options;
    }

    public JSONObject getOptions() {
        JSONObject options = new JSONObject();
        try {
            options.put("options", fetchOptions());
            options.put("status", "success");
        } catch (SQLException e) {
            options.put("status", "error");
            e.printStackTrace();
        }
        return options;
    }
}
