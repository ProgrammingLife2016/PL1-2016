package io.github.programminglife2016.pl1_2016.parser.phylotree;

import io.github.programminglife2016.pl1_2016.parser.Parser;

import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Collectors;


/**
 * Class for parsing a .nwk file to an internal tree structure.
 */
public class PhylogeneticTreeParser implements Parser {
    @Override
    public TreeNodeCollection parse(InputStream inputStream) {
        String s = inputStreamToString(inputStream);
        return parseTokensFromString(s);
    }

    private String inputStreamToString(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        String s = sc.useDelimiter("\\A").next();
        sc.close();
        return s;
    }

    /**
     * Create tree from string.
     * @param s string representin the tree.
     * @return root of tree
     */
    public TreeNodeCollection parseTokensFromString(String s) {
        StringTokenizer tokenizer = new StringTokenizer(s, "(:,);", true);
        TreeNodeCollection nodes = construct(tokenizer);
        return nodes;
    }

    /**
     * Construct tree from the input of the String Tokenizer.
     * @param tokenizer tokenizer with the contents of the .nwk file.
     * @return parsed Tree Node object.
     */
    public TreeNodeCollection construct(StringTokenizer tokenizer) {
        TreeNodeCollection nodes = new TreeNodeList();
        TreeNode root = new BaseTreeNode();
        nodes.add(root);
        nodes.setRoot(root);
        TreeNode current = root;
        while (tokenizer.hasMoreTokens()) {
            String currentToken = tokenizer.nextToken();
            switch (currentToken) {
                case "(":
                    current = parseChild(nodes, current);
                    break;
                case ":":
                    double weight = Double.parseDouble(tokenizer.nextToken());
                    current.setWeight(weight);
                    break;
                case ",":
                    current = parseParent(nodes, current);
                    break;
                case ")":
                    current = current.getParent();
                    break;
                case ";":
                    break;
                default:
                    current.setName(currentToken.replace("_", " "));
                    break;
            }
        }
        return nodes;
    }

    private TreeNode parseChild(TreeNodeCollection nodes, TreeNode current) {
        TreeNode child = new BaseTreeNode();
        nodes.add(child);
        child.setParent(current);
        current.addChild(child);
        return child;
    }

    private TreeNode parseParent(TreeNodeCollection nodes, TreeNode current) {
        TreeNode newnode = new BaseTreeNode();
        nodes.add(newnode);
        current = current.getParent();
        newnode.setParent(current);
        current.addChild(newnode);
        return newnode;
    }
}
