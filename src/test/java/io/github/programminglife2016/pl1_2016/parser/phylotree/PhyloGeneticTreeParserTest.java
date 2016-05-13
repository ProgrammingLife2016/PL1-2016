package io.github.programminglife2016.pl1_2016.parser.phylotree;

import com.google.gson.JsonElement;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.tree.Tree;

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

    @Test
    public void testSerializer() {
        PhyloGeneticTreeParser parser = new PhyloGeneticTreeParser();
        String s = "(B:6.0,D:11.0);";
        TreeNodeCollection node = parser.parse(stringToInputStream(s));
        TreeNode ch1 = new BaseTreeNode("B", 6.0);
        TreeNode ch2 = new BaseTreeNode("D", 11.0);
        List<TreeNode> childs = new ArrayList<>();
        childs.add(ch1);
        childs.add(ch2);
        TreeNode node1 = new BaseTreeNode("-", 0.0);
        node1.setChildren(childs);
        ch1.setParent(node1);
        ch2.setParent(node1);
        TreeNodeCollection treeNodes = new TreeNodeList();
        treeNodes.add(node1);
        treeNodes.add(ch1);
        treeNodes.add(ch2);
        treeNodes.setRoot(node1);
        String el2 = node.toJson();
        String el1 = treeNodes.toJson();
        assertEquals(el1, el2);
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
