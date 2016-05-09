package io.github.programminglife2016.pl1_2016.parser;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.*;

/**
 * Class for representing a node in the phylogenetic tree.
 */
public class BaseTreeNode implements JsonSerializable, TreeNode {
    private String id;
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
     * @param id id of the tree node.
     * @param weight weight of the tree node.
     */
    public BaseTreeNode(String id, double weight) {
        this(id, weight, new ArrayList<>(), null);
    }

    /**
     * Create a BaseTreeNode object.
     * @param id id of the treenode.
     * @param weight weight of the tree node.
     * @param children list of children tree nodes.
     * @param parent parent tree node of this tree node.
     */
    public BaseTreeNode(String id, double weight, List<TreeNode> children, TreeNode parent) {
        this.id = id;
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

    /**
     * Set the list of children tree nodes.
     * @param children list of tree node objects.
     */
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    /**
     * Set the id of the tree node.
     * @param id value of the id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set the weight of the tree node.
     * @param weight double value of the weight of the tree.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Get id of tree node.
     * @return id of tree.
     */
    public String getId() {
        return id;
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
        return "[" + this.getId() + " " + this.getWeight()
                + " {" + children.stream()
                                 .map(Object::toString)
                                 .collect(joining(", ")) + "}]";
    }
}
