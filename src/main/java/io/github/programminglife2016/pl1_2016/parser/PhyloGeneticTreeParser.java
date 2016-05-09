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
        TreeNode root = parseTokensFromString(s);
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
    public TreeNode parseTokensFromString(String s) {
        StringIterator tokenizer = new TokenIterator(s, "(:,);", true);
        TreeNode root = constructTree(null, tokenizer);
        return root.getChildren().get(0);
    }

    /**
     * Construct tree from the input of the String Tokenizer.
     * @param parent parent of the current node.
     * @param tokenizer tokenizer with the contents of the .nwk file.
     * @return parsed Tree Node object.
     */
    public TreeNode constructTree(TreeNode parent, StringIterator tokenizer) {
        TreeNode current = new BaseTreeNode();
        String next;
        double weight;
        List<TreeNode> nodes = new ArrayList<>();
        while (tokenizer.hasNext()) {
            next = tokenizer.next();
            switch (next) {
            case "(":
                TreeNode newNode = constructTree(current, tokenizer);
                nodes.add(newNode);
                if (tokenizer.hasNext()) {
                    tokenizer.next();
                }
                break;
            case ":":
                current.setWeight(Double.parseDouble(tokenizer.next()));
                break;
            case ",":
                nodes.add(current);
                current = new BaseTreeNode();
                break;
            case ")":
                if (!current.getId().equals("-") && current.getWeight() != 0.0) {
                    nodes.add(current);
                }
                weight = 0;
                if (tokenizer.hasNext() && tokenizer.next().equals(":")) {
                    weight = Double.parseDouble(tokenizer.next());
                }
                return new BaseTreeNode("-", weight, nodes, parent);
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

    /**
     * Test parser.
     * @param args args.
     */
    public static void main(String[] args) {
        System.out.println("PhylgoGenetic Tree Parser");
        PhyloGeneticTreeParser parser = new PhyloGeneticTreeParser();
//        String s = "(A: 0.1,B: 0.2,(C:0.3,D:0.4):0.5);";
        String s = "(A: 0.1,B: 0.2,(C:0.3,(Z:1,Y:2):0.4):0.5);";
//        parser.parse(Launcher.class.getResourceAsStream("/genomes/340tree.rooted.TKK.nwk"));
        s = "(A:0.1,(B:0.2,(C:0.3,(D:0.4,E:0.5):0.6):0.4):0.5);";
        System.out.println("Done");
        parser.parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }
}
