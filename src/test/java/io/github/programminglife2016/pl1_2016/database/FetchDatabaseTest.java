package io.github.programminglife2016.pl1_2016.database;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;

/**
 * Tests for fetchDatabase.
 * @author Ravi Autar.
 */
public class FetchDatabaseTest {
    @Mock
    FetchDatabase dataBase;

    @Before
    public void setUp() {
        dataBase = Mockito.mock(FetchDatabase.class);
    }

    @Test
    public void test() throws SQLException {
    }
}
