package io.github.programminglife2016.pl1_2016.parser.phylotree;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;

import java.util.Collection;

/**
 * A data type that holds TreeNodes.
 */
public interface TreeNodeCollection extends Collection<TreeNode>, JsonSerializable {
    /**
     * Get the root of this tree.
     *
     * @return root of this tree
     */
    TreeNode getRoot();

    /**
     * Set the root of this tree.
     *
     * @param root root of this tree
     */
    void setRoot(TreeNode root);
}
