package io.github.programminglife2016.pl1_2016.parser;

import java.util.Collection;

public interface TreeNodeCollection extends Collection<TreeNode>, JsonSerializable {
    TreeNode getRoot();
    void setRoot(TreeNode root);
}
