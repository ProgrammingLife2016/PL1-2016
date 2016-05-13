package io.github.programminglife2016.pl1_2016.parser.phylotree;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
        TreeNodeCollection node  = parser.parseTokensFromString(s);
        assertEquals("B", node.getRoot().getChildren().get(0).getName());
    }

    /**
     * Test parsing a segment line to produce the correct segment.
     */
    @Test
    public void testTreeWithOneNestedLevel() {
        PhyloGeneticTreeParser parser = new PhyloGeneticTreeParser();
        String s = "(B:6.0,(A:5.0,(Z:9.0,T:10):3.0):5.0,D:11.0);";
        TreeNodeCollection node = parser.parse(stringToInputStream(s));
        assertEquals("B", node.getRoot().getChildren().get(0).getName());
    }

    /**
     * Test to get correct flattened list leaves.
     */
    @Test
    public void testBasicBaseTreeNodeFunctions() {
        PhyloGeneticTreeParser parser = new PhyloGeneticTreeParser();
        String s = "(B:6.0,D:11.0);";
        TreeNodeCollection node = parser.parse(stringToInputStream(s));
        TreeNodeCollection collection = node.getRoot().flatten();
        collection.setRoot(node.getRoot());
        node.getRoot().getId();
        assertEquals(node.getRoot().getChildren().get(0),
                    collection.getRoot().getChildren().get(0));
        assertEquals("B",
                    node.getRoot().getChildren().get(0).getName());
        assertEquals(6.0,
                    node.getRoot().getChildren().get(0).getWeight(), 0.0001);
    }

    /**
     * Test setting for children in node.
     */
    @Test
    public void testSettingBaseNodeChild() {
        PhyloGeneticTreeParser parser = new PhyloGeneticTreeParser();
        String s = "(B:6.0,D:11.0);";
        TreeNodeCollection node = parser.parse(stringToInputStream(s));
        List<TreeNode> lst = new ArrayList<>();
        lst.add(new BaseTreeNode());
        lst.add(new BaseTreeNode());
        node.getRoot().setChildren(lst);
        assertEquals("[- 0.0 {}]", node.getRoot().getChildren().get(0).toString());
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
