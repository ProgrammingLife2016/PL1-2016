package io.github.programminglife2016.pl1_2016.parser;

import java.util.ArrayList;
import java.util.List;

public class TreeNode implements JsonSerializable {
    private String id;
    private double weight;
    private List<TreeNode> children;
    private TreeNode parent;

    public TreeNode() {
        this("-", 0);
    }

    public TreeNode(String id, double weight) {
        this(id, weight, new ArrayList<>(), null);
    }

    public TreeNode(String id, double weight, List<TreeNode> children, TreeNode parent) {
        this.id = id;
        this.weight = weight;
        this.children = children;
        this.parent = parent;
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void toString(String c) {
        System.out.print(c + "#" + this.getId() + " " + getWeight());
        if (parent != null)
            System.out.println(" " + parent.getId());
        else
            System.out.println();

        this.getChildren().forEach(n -> n.toString(c + "  "));
    }
}
