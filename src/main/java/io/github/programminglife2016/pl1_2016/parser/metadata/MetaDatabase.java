package io.github.programminglife2016.pl1_2016.parser.metadata;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeList;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;


public class MetaDatabase {

    private static final String DATABASE_DRIVER = "org.postgresql.Driver";

    private static final String HOST = "jdbc:postgresql://145.94.157.33:5432/pl1";

    private static final String ROLL = "pl";

    private static final String PASSWORD = "visual";

    private Connection connection;

    public MetaDatabase() {
        connectDatabase();
    }

    /**
h
     * Sets up the driver and creates a connection with database with given host and rolls.
     */
    private void connectDatabase() {
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

    /**
     * This method is used for loading the pre-calculated positions from a nodeCollection with the database into a nodeCollection.
     *
     * @param nodes The nodes that get positioned.
     * @param zoomlevel The zoomlevel that is requested (Not used yet)
     * @return The correctly positioned nodes.
     * @throws SQLException
     */
    public NodeCollection getPositions(NodeCollection nodes, int zoomlevel) throws SQLException {
        String sql = "SELECT nodeid, positionx, positiony FROM nodes;";
        PreparedStatement stm = this.connection.prepareStatement(sql);
        ResultSet set = stm.executeQuery();
        NodeList newList = new NodeList(nodes.getNodes().size());
        Collection<Node> list = nodes.getNodes();
        System.out.println(list.size());
        Object[] nodesarray = list.toArray();
        while(set.next()){
            ((Node)nodesarray[Integer.parseInt(set.getString(1))-1]).setXY(Integer.parseInt(set.getString(2)), Integer.parseInt(set.getString(3)));
            newList.put(Integer.parseInt(set.getString(1)), ((Node)nodesarray[Integer.parseInt(set.getString(1))-1]));
        }

        return newList;
    }

}
