package io.github.programminglife2016.pl1_2016.database;

import io.github.programminglife2016.pl1_2016.parser.metadata.Annotation;
import io.github.programminglife2016.pl1_2016.parser.metadata.Specimen;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests for setUpDatabase.
 * @author Ravi Autar.
 */
public class SetupDatabaseTest {
    SetupDatabase dataBase;
    Connection connection;
    Statement statement;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Collection specimenList;

    @Before
    public void setUp() throws SQLException {
        Specimen specimen = new Specimen("TK_02_0005");
        initSpecimen(specimen);
        specimenList = new ArrayList();
        specimenList.add(specimen);
        dataBase = new SetupDatabase();
        resultSet = Mockito.mock(ResultSet.class);
        statement = Mockito.mock(Statement.class);
        connection = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        dataBase.setSplist(specimenList);
        Mockito.when(statement.executeQuery(Mockito.anyString())).thenReturn(resultSet);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        dataBase.setConnection(connection);
//        Mockito.when(dataBase)
    }

    private Specimen initSpecimen(Specimen specimen) {
        specimen.setAge(0);
        specimen.setMale(false);
        specimen.setHivStatus(0);
        specimen.setCohort("");
        specimen.setSmear(0);
        specimen.setDate("");
        specimen.setSingleColony(false);
        specimen.setPdstpattern("");
        specimen.setCapreomycin("");
        specimen.setEthambutol("");
        specimen.setIsoniazid("");
        specimen.setEthionamide("");
        specimen.setKanamycin("");
        specimen.setKanamycin("");
        specimen.setPyrazinamide("");
        specimen.setOfloxacin("");
        specimen.setRifampin("");
        specimen.setStreptomycin("");
        specimen.setSpoligotype("");
        specimen.setLineage("");
        specimen.setGdstPattern("");
        specimen.setXdr("");
        specimen.setDistrict("");
        specimen.setType("");
        return specimen;
    }

    @Test (expected = JSONException.class)
    public void test() throws SQLException {
        Annotation annotation = new Annotation();
        annotation.setId("");
        annotation.setSeqId("");
        annotation.setDisplayName("");
        annotation.setEnd(0);
        annotation.setStart(0);
        List<Annotation> ann = new ArrayList<>();
        ann.add(annotation);
        NodeCollection collection = new NodeMap();
        collection.setAnnotations(ann);
        dataBase.setup(collection);
    }
}
