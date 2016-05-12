package io.github.programminglife2016.pl1_2016.parser.phylotree;

import io.github.programminglife2016.pl1_2016.parser.TreeNodeCollection;
import io.github.programminglife2016.pl1_2016.parser.TreeNodeList;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.*;

/**
 * Class for representing a node in the phylogenetic tree.
 */
public class BaseTreeNode implements TreeNode {
    private static int STATIC_ID = 1;
    private int id;
    private String name;
    private double weight;
    private List<TreeNode> children;
    private TreeNode parent;

    /**
     * Create a dummy Tree Node.
     */
    public BaseTreeNode() {
        this("-", 0);
    }

    /**
     * Create a Tree Node Object.
     * @param name name of the tree node.
     * @param weight weight of the tree node.
     */
    public BaseTreeNode(String name, double weight) {
        this(name, weight, new ArrayList<>(), null);
    }

    /**
     * Create a BaseTreeNode object.
     * @param name name of the treenode.
     * @param weight weight of the tree node.
     * @param children list of children tree nodes.
     * @param parent parent tree node of this tree node.
     */
    public BaseTreeNode(String name, double weight, List<TreeNode> children, TreeNode parent) {
        this.id = STATIC_ID++;
        this.name = name;
        this.weight = weight;
        this.children = children;
        this.parent = parent;
    }

    /**
     * Add a child to the current tree node.
     * @param child child tree node of this tree node.
     */
    public void addChild(TreeNode child) {
        this.children.add(child);
    }

    @Override
    public TreeNodeCollection flatten() {
        TreeNodeCollection treeNodes = new TreeNodeList();
        if (children.isEmpty()) {
            treeNodes.add(this);
        } else {
            children.stream().map(TreeNode::flatten).forEach(treeNodes::addAll);
        }
        return treeNodes;
    }

    /**
     * Set the list of children tree nodes.
     * @param children list of tree node objects.
     */
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    /**
     * Get the id of the tree node.
     *
     * @return id of the tree node
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Set the name of the tree node.
     * @param name value of the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the weight of the tree node.
     * @param weight double value of the weight of the tree.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Get name of tree node.
     * @return name of tree.
     */
    public String getName() {
        return name;
    }

    /**
     * Return weight of this node.
     * @return weight of tree node.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Get the children from this node.
     * @return list of children.
     */
    public List<TreeNode> getChildren() {
        return children;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * Convert tree to string.
     * @return String representation of the treenode.
     */
    @Override
    public String toString() {
        return "[" + this.getName() + " " + this.getWeight()
                + " {" + children.stream()
                                 .map(Object::toString)
                                 .collect(joining(", ")) + "}]";
    }
}
