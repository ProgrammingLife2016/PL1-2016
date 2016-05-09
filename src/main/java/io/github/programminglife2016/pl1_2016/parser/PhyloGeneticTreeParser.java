package io.github.programminglife2016.pl1_2016.parser;

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
        StringIterator tokenizer = new TokenIterator(s, "(:,);", true);
        TreeNodeCollection nodes = construct(tokenizer);
        return nodes;
    }

    /**
     * Construct tree from the input of the String Tokenizer.
     * @param tokenizer tokenizer with the contents of the .nwk file.
     * @return parsed Tree Node object.
     */
    public TreeNodeCollection construct(StringIterator tokenizer) {
        TreeNodeCollection nodes = new TreeNodeList();
        TreeNode root = new BaseTreeNode();
        nodes.add(root);
        nodes.setRoot(root);
        TreeNode current = root;
        while (tokenizer.hasNext()) {
            String currentToken = tokenizer.next();
            switch (currentToken) {
                case "(":
                    TreeNode child = new BaseTreeNode();
                    nodes.add(child);
                    child.setParent(current);
                    current.addChild(child);
                    current = child;
                    break;
                case ":":
                    double weight = Double.parseDouble(tokenizer.next());
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
                    current.setName(currentToken);
                    break;
            }
        }
        return nodes;
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
                if (!current.getName().equals("-") && current.getWeight() != 0.0) {
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
                current.setName(next);
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
        s = "(A:0.1,(B:0.2,(C:0.3,(D:0.4,E:0.5):0.6):0.4):0.5);";
//        parser.parse(Launcher.class.getResourceAsStream("/genomes/340tree.rooted.TKK.nwk"));
        System.out.println(parser.parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8))).toJson());
        System.out.println("Done");
    }
}
