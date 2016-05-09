package io.github.programminglife2016.pl1_2016.parser;

import io.github.programminglife2016.pl1_2016.Launcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * Class for parsing a .nwk file to an internal tree structure.
 */
public class PhyloGeneticTreeParser implements Parser {

    @Override
    public JsonSerializable parse(InputStream inputStream) {
        String s = inputStreamToString(inputStream);
        Collection<TreeNode> nodes = parseTokensFromString(s);
        return null;
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
    public Collection<TreeNode> parseTokensFromString(String s) {
        StringTokenizer tokenizer = new StringTokenizer(s, "(:,);", true);
        Collection<TreeNode> nodes = construct(tokenizer);
        return nodes;
    }

    /**
     * Construct tree from the input of the String Tokenizer.
     * @param tokenizer tokenizer with the contents of the .nwk file.
     * @return parsed Tree Node object.
     */
    public Collection<TreeNode> construct(StringTokenizer tokenizer) {
        Collection<TreeNode> nodes = new ArrayList<TreeNode>();
        TreeNode root = new BaseTreeNode();
        nodes.add(root);
        TreeNode current = root;
        while (tokenizer.hasMoreTokens()) {
            String currentToken = tokenizer.nextToken();
            switch (currentToken) {
                case "(":
                    TreeNode child = new BaseTreeNode();
                    nodes.add(child);
                    child.setParent(current);
                    current.addChild(child);
                    current = child;
                    break;
                case ":":
                    double weight = Double.parseDouble(tokenizer.nextToken());
                    current.setWeight(weight);
                    break;
                case ",":
                    TreeNode newnode = new BaseTreeNode();
                    nodes.add(newnode);
                    current = current.getParent();
                    newnode.setParent(current);
                    current.addChild(newnode);
                    current = newnode;
                    break;
                case ")":
                    current = current.getParent();
                    break;
                case ";":
                    break;
                default:
                    current.setId(currentToken);
                    break;
            }
        }
        return nodes;
    }
}
