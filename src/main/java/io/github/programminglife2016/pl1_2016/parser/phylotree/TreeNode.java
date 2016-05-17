package io.github.programminglife2016.pl1_2016.parser.phylotree;

import java.util.List;

/**
 * TreeNode interface to make a contruct tree datastructure.
 */
public interface TreeNode {
    /**
     * Get the id of the tree node.
     *
     * @return id of the tree node
     */
    int getId();

    /**
     * Set the name of the tree node.
     * @param name value of the name.
     */
    void setName(String name);

    /**
     * Set the list of children tree nodes.
     * @param children list of tree node objects.
     */
    void setChildren(List<TreeNode> children);

    /**
     * Get id of tree node.
     * @return id of tree.
     */
    String getName();

    /**
     * Return weight of this node.
     * @return weight of tree node.
     */
    double getWeight();

    /**
     * Set the weight of the tree node.
     * @param weight double value of the weight of the tree.
     */
    void setWeight(double weight);

    /**
     * Get the children from this node.
     * @return list of children.
     */
    List<TreeNode> getChildren();

    /**
     * Set the parent of this node.
     *
     * @param node the parent of this node
     */
    void setParent(TreeNode node);

    /**
     * Return the parent of this node, or null if this node is a root.
     *
     * @return the parent of this node, or null if this node is a root.
     */
    TreeNode getParent();

    /**
     * Add a child to the current tree node.
     * @param node child tree node of this tree node.
     */
    void addChild(TreeNode node);

    /**
     * Convert the tree with this node as the root into a one-dimensional collection.
     *
     * @return a TreeNodeColleciton containing all the nodes under this node, including this node.
     */
    TreeNodeCollection flatten();
}
