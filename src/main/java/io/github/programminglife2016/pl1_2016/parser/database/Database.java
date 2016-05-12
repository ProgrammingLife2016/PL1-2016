package io.github.programminglife2016.pl1_2016.parser.database;

import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;

import java.sql.SQLException;

public interface Database {
    public void connect();

    public NodeCollection fetchSegments() throws SQLException;

    public void writeSegments(NodeCollection nodes) throws SQLException;

    public SpecimenCollection fetchSpecimen();

    public void WriteSpecimens(SpecimenCollection specimen);
}
