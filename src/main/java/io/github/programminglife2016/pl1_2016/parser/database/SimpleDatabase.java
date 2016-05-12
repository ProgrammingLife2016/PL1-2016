package io.github.programminglife2016.pl1_2016.parser.database;

import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeList;
import io.github.programminglife2016.pl1_2016.parser.nodes.Segment;
import sun.java2d.pipe.SpanShapeRenderer;

import java.sql.*;
import java.util.Iterator;

public class SimpleDatabase implements Database{

    private static final String DATABASE_DRIVER = "org.postgresql.Driver";

    private static final String HOST = "jdbc:postgresql://localhost:5432/pl1";

    private static final String ROLL = "pl";

    private static final String PASSWORD = "visual";

    private static final String SEGMENT_TABLE= "segments";

    private Connection connection;

    public SimpleDatabase(){connect();}

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
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (stmt != null) { stmt.close(); }
            return nodes;
        }
    }

    public void writeSegments(NodeCollection nodes) throws SQLException {
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
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    public SpecimenCollection fetchSpecimen() {
        return null;
    }

    public void WriteSpecimens(SpecimenCollection specimen) {

    }

    public static void main(String[] args){
        SimpleDatabase db = new SimpleDatabase();
        System.out.println();
    }
}
