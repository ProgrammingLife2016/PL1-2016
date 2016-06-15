package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.Launcher;
import io.github.programminglife2016.pl1_2016.parser.ObjectSerializer;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to view bubbling changes
 */
public final class BubbleMain {
    private static final int THRESHOLD = 500;
    private static final double SEGMENT_POSITION_FACTOR = 100;

    private BubbleMain() { }

    /**
     * Execute bubbling
     * @param args arguments.
     * @throws IOException thrown when reading the files fails.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, URISyntaxException {
        InputStream is =
                BubbleMain.class.getClass().getResourceAsStream("/genomes/TB328.gfa");
//        TB328-old
//        InputStream is =
//                BubbleMain.class.getClass().getResourceAsStream("/genomes/TB328-old.gfa");
        InputStream mt =
                BubbleMain.class.getClass().getResourceAsStream("/genomes/metadata.csv");
        InputStream pos =
                BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.positions");
        NodeCollection nodeCollection = new SegmentParser(pos, mt).parse(is);


        System.gc();


//        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
//        NodeCollection collectionToShow = dispatcher.getThresholdedBubbles(4, false);
//
        ObjectSerializer serializer = new ObjectSerializer();
//        serializer.serializeItem((new ArrayList<>(collectionToShow.values())), "src/main/resources/genomes/TB328dispatched.ser");
//        PrintWriter printer = new PrintWriter(new File("src/main/resources/genomes/TB328-filtered.dot"));
//        for (Node node : collectionToShow.values()) {
//            node.getLinks().forEach(link -> printer.println(node.getId() + " -> " + link.getId()));
//        }
        List<Node> nodes = (List<Node>) serializer.getSerializedItem("src/main/resources/objects/bubbles-with-positions.ser");
        System.out.println();
        NodeCollection collection = serializer.listAsNodeCollection(nodes);

//        collectionToShow.assignNewPositions(Launcher.class.getResourceAsStream("/genomes/tb328-filtered.positions"));

//        PrintWriter printer = new PrintWriter(new File("src/main/resources/genomes/TB328.positions"));
//        printer.print("graph something blah blah");
//        for (Node node : collectionToShow.values()) {
//            printer.print("node " + node.getStartNode().getId() + " " + (((double) node.getStartNode().getX())/100.0) + " " + (((double) node.getStartNode().getY())/100.0) + " something");
//            printer.print("node " + node.getEndNode().getId() + " " + (((double) node.getEndNode().getX())/100.0) + " " + (((double) node.getEndNode().getY())/100.0) + " something");
//            for (Node child : node.getContainer()) {
//                printer.print("node " + child.getId() + " " + (((double) child.getX())/100.0) + " " + (((double) child.getY())/100.0) + " something");
//            }
//        }
//        printer.close();

//        List<Node> bubblesWithPositions = new ArrayList<Node>(collectionToShow.values());
//        serializer.serializeItem(bubblesWithPositions, "src/main/resources/objects/bubbles-with-positions.ser");

        //==============================
//        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
//        NodeCollection nodes = dispatcher.getThresholdedBubbles(4, false);
//        for (Node node : nodes.values()) {
//            node.getLinks().forEach(x -> System.out.println(node.getId() + " -> " + x.getId()));
//        }
//        System.out.println("NodeCollection before filtering: " + nodeCollection.size());
//        System.out.println("NodeCollection after filtering SNPs and indels: " + nodes.size());
        //=======================================

//        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
//        collapser.collapseBubbles();
//        for (Node node : collapser.getBubbles()) {
//            node.getLinks().forEach(x -> System.out.println(node.getId() + " -> " + x.getId()));
//        }

        //=======================================

//        BubbleDetector detector = new BubbleDetector(nodeCollection);
//        detector.findMultiLevelBubbles();
//        for (Node node : detector.getBubbleBoundaries()) {
//            System.out.println(node.getId());
//        }
    }
}
