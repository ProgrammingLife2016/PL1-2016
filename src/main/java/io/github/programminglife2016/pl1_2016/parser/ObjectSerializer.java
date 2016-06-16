package io.github.programminglife2016.pl1_2016.parser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;

import java.io.*;
import java.util.Collection;

/**
 * Serializer for any object of choice.
 * @author Ravi Autar
 */
public class ObjectSerializer {
    public void serializeItem(Object object, String infile) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(infile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(object);
        out.close();
        fileOut.close();
        System.out.println("Object saved succesfully!");
    }

    public Object getSerializedItem(String infile) throws IOException, ClassNotFoundException {
        Object serializedObject = null;
        FileInputStream fileIn = new FileInputStream(infile);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        serializedObject = in.readObject();
        in.close();
        fileIn.close();
        return serializedObject;
    }

    /**
     * Convert collection of nodes to NodeCollection
     * @param res collection of nodes
     * @return NodeCollection of given nodes
     */
    public NodeCollection listAsNodeCollection(Collection<Node> res) {
        NodeCollection collection = new NodeMap();
        for (Node node: res) {
            collection.put(node.getId(), node);
        }
        return collection;
    }

    public Node getRecursiveNode(Node bubble) {
        Node curr = bubble.getStartNode();
        while (curr.isBubble()) {
            curr = curr.getStartNode();
        }
        return curr;
    }
}
