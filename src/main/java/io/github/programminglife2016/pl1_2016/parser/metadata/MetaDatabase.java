package io.github.programminglife2016.pl1_2016.parser.metadata;

import java.sql.*;

/**
 * Created by ravishivam on 8-5-16.
 */
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

    public static void main(String[] argv) throws SQLException {
        MetaDatabase db = new MetaDatabase();
        String sql = "SELECT Specimen_ID, Age, Sex FROM specimen;";
        PreparedStatement stm = db.connection.prepareStatement(sql);
        ResultSet set = stm.executeQuery();
        while(set.next()){
            System.out.println(set.getString(1));
        }
        stm.close();
        db.connection.close();
        }
}
