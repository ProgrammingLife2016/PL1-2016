package io.github.programminglife2016.pl1_2016.parser.phylotree;

import java.util.List;

/** TreeNode interface to make a contruct tree datastructure.
 *
 * Created by ravishivam on 6-5-16.
 */
public interface TreeNode {

    /**
     * Set the id of the tree node.
     * @param id value of the id.
     */
    void setId(String id);

    /**
     * Set the list of children tree nodes.
     * @param children list of tree node objects.
     */
    void setChildren(List<TreeNode> children);

    /**
     * Get id of tree node.
     * @return id of tree.
     */
    String getId();

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
}
