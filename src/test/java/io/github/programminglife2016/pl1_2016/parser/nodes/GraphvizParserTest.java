package io.github.programminglife2016.pl1_2016.parser.nodes;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertEquals;

/**
 * Tester for the graphviz parser.
 * @author Ravi Autar.
 */
public class GraphvizParserTest {
    private GraphvizParser parser;

    /**
     * Sets up initial values for every test case.
     */
    @Before
    public void setUp() {
        this.parser = new GraphvizParser();
    }

    /**
     * Added test for testing nodes in a certain graph.
     * @throws IOException Exception thrown if file was not found.
     */
    @Test
    public void testNodesInGraph() throws IOException {
        String data = "graph 1 7520.5 8.8472\n"
                + "node 1 0.375 6.375 0.75 0.5 1 solid ellipse black lightgrey\n"
                + "node 2 1.625 6.375 0.75 0.5 2 solid ellipse black lightgrey\n"
                + "node 3 2.875 6.75 0.75 0.5 3 solid ellipse black lightgrey\n"
                + "node 4 2.875 6 0.75 0.5 4 solid ellipse black lightgrey\n"
                + "node 5 4.125 6.375 0.75 0.5 5 solid ellipse black lightgrey\n"
                + "node 6 5.375 6.6944 0.75 0.5 6 solid ellipse black lightgrey\n"
                + "stop";
        NodeCollection collection = (NodeCollection) parser.parse(stringToInputStream(data));
        assertEquals(collection.get(1).getId(), 1);
    }

    /**
     * Converts a String to an InputStream
     * @param s String
     * @return InputStream of that String.
     */
    public InputStream stringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }
}
