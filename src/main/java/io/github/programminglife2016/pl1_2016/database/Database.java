package io.github.programminglife2016.pl1_2016.database;


/**
 * Interface for creating a database.
 */
public interface Database {
    /**
     * Driver for the database.
     */
     String DATABASE_DRIVER = "org.postgresql.Driver";
    /**
     * Host for the database.
     */
    String HOST = "jdbc:postgresql://localhost:5432/pl1";
    /**
     * Roll for the database.
     */
     String ROLL = "pl";
    /**
     * Password for database.
     */
    String PASSWORD = "visual";
    /**
     * Name of nodes table.
     */
     String NODES_TABLE = "segments";
    /**
     * Name of specimen table.
     */
     String SPECIMEN_TABLE = "specimen";
    /**
     * Name of links table.
     */
    String LINK_TABLE = "links";
    /**
     * Name of specimen table.
     */
     String LINK_GENOMES_TABLE = "genomeslinks";
}
