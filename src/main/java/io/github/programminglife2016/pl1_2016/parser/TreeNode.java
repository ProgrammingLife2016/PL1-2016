package io.github.programminglife2016.pl1_2016.parser;

import java.util.List;

/** TreeNode interface to make a contruct tree datastructure.
 *
 * Created by ravishivam on 6-5-16.
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
    public void setWeight(double weight);


        /**
         * Get the children from this node.
         * @return list of children.
         */
    List<TreeNode> getChildren();

    void setParent(TreeNode node);

    TreeNode getParent();

    void addChild(TreeNode node);

    TreeNodeCollection flatten();
}
