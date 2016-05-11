package io.github.programminglife2016.pl1_2016.parser;

import io.github.programminglife2016.pl1_2016.parser.phylotree.TreeNode;

import java.util.Collection;

public interface TreeNodeCollection extends Collection<TreeNode>, JsonSerializable {
    TreeNode getRoot();
    void setRoot(TreeNode root);
}
