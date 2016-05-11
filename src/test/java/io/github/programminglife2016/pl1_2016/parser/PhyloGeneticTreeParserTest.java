package io.github.programminglife2016.pl1_2016.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the PhylogeneticTreeParser.
 */
public class PhyloGeneticTreeParserTest {

    /**
     * Create parser object.
     */
    @Before
    public void setUp() {
    }

    @Test
    public void testTreeWithSingleNode() {
        PhyloGeneticTreeParser parser = new PhyloGeneticTreeParser();
        String s = "(B:6.0,A:5.0);";
        Collection<TreeNode> nodes  = parser.parseTokensFromString(s);
        assertEquals("B", nodes.stream().findFirst().get().getId());
    }

    /**
     * Test parsing a segment line to produce the correct segment.
     */
    @Test
    public void testTreeWithOneNestedLevel() {
        PhyloGeneticTreeParser parser = new PhyloGeneticTreeParser();
        String s = "(B:6.0,(A:5.0,(Z:9.0,T:10):3.0,E:4.0):5.0,D:11.0);";
        Collection<TreeNode> node = parser.parseTokensFromString(s);
        assertEquals("B", node.stream().findFirst().get().getId());
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
