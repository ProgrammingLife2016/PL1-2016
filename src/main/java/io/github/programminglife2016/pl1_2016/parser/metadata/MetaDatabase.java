package io.github.programminglife2016.pl1_2016.parser.metadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ravishivam on 8-5-16.
 */
public class MetaDatabase {

    private static final String DATABASE_DRIVER = "org.postgresql.Driver";

    private static final String HOST = "jdbc:postgresql://127.0.0.1:5432/PL1";

    private static final String ROLL = "postgres";

    private static final String PASSWORD = "visual";

    private Connection connection;

    public MetaDatabase() {
        connectDatabase();
    }

    /**
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

    public static void main(String[] argv) {
    }
}
