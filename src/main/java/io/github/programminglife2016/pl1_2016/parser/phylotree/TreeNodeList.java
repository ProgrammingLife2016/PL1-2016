package io.github.programminglife2016.pl1_2016.parser.phylotree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Adapter pattern of ArrayList. Used to contain TreeNodes.
 */
public class TreeNodeList extends ArrayList<TreeNode> implements TreeNodeCollection {
    private TreeNode root;

    @Override
    public final String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(TreeNodeList.class,
                new TreeNodeCollectionSerializer()).create();
        return gson.toJson(this);
    }

    @Override
    public final TreeNode getRoot() {
        return root;
    }

    @Override
    public final void setRoot(TreeNode root) {
        this.root = root;
    }
}
