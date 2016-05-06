package io.github.programminglife2016.pl1_2016.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 * Class for parsing a .nwk file to an internal tree structure.
 */
public class PhyloGeneticTreeParser implements Parser {

    @Override
    public JsonSerializable parse(InputStream inputStream) {
        String s = inputStreamToString(inputStream);
        StringTokenizer tokenizer = new StringTokenizer(s, "(:,);", true);
        TreeNode root = constructTree(null, tokenizer);
        root = root.getChildren().get(0);
        root.toString("");
        return null;
    }

    private String inputStreamToString(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        String s = sc.useDelimiter("\\A").next();
        sc.close();
        return s;
    }

    /**
     * Construct tree from the input of the String Tokenizer.
     * @param parent parent of the current node.
     * @param tokenizer tokenizer with the contents of the .nwk file.
     * @return parsed Tree Node object.
     */
    public TreeNode constructTree(TreeNode parent, StringTokenizer tokenizer) {
        TreeNode current = new TreeNode();
        String next;
        double weight;
        List<TreeNode> nodes = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            next = tokenizer.nextToken();
            switch (next) {
                case "(":
                    TreeNode newNode = constructTree(current, tokenizer);
                    nodes.add(newNode);
                    if (tokenizer.hasMoreTokens()) {
                        tokenizer.nextToken();
                    }
                    break;
                case ":":
                    current.setWeight(Double.parseDouble(tokenizer.nextToken()));
                    break;
                case ",":
                    nodes.add(current);
                    current = new TreeNode();
                    break;
                case ")":
                    nodes.add(current);
                    weight = 0;
                    if (tokenizer.hasMoreTokens() && tokenizer.nextToken().equals(":")) {
                        weight = Double.parseDouble(tokenizer.nextToken());
                    }
                    return new TreeNode("-", weight, nodes, parent);
                case ";":
                    current.setChildren(nodes);
                    return current;
                default:
                    current.setId(next);
                    break;
            }
        }
        current.setChildren(nodes);
        return current;
    }
}
