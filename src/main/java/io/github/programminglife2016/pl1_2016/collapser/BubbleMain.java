package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.Launcher;
import io.github.programminglife2016.pl1_2016.parser.ObjectSerializer;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

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
//        NodeCollection nodeCollection = new SegmentParser(pos, mt).parse(is);




//        Scanner scanner = new Scanner(new File("src/main/resources/genomes/TB328.positions"));
//        PrintWriter printer = new PrintWriter(new File("src/main/resources/genomes/TB328-uniques.positions"));
//        scanner.nextLine();
//        printer.println("graph blah blah something blah");
//        Set<String> uniques = new HashSet<>();
//        while(scanner.hasNext()) {
//            String lin = scanner.nextLine();
//            String[] line = lin.split(" ");
//            if (!uniques.contains(line[1])) {
//                uniques.add(line[1]);
//                printer.println(lin);
//            }
//        }
//        printer.println("stop");
//        printer.close();
//        scanner.close();
//
//        for (int i = 1; i < 78136; i++) {
//            if (!uniques.contains(Integer.toString(i))) {
//                System.out.println(i);
//            }
//        }

//        System.gc();

//        ObjectSerializer serializer = new ObjectSerializer();
//        PrintWriter printer = new PrintWriter(new File("src/main/resources/genomes/TB328-filtered.dot"));
//        for (Node node : collectionToShow.values()) {
//            node.getLinks().forEach(link -> printer.println(node.getId() + " -> " + link.getId()));
//        }
//        List<Node> nodes = (List<Node>) serializer.getSerializedItem("src/main/resources/objects/bubbles-with-positions.ser");
//        NodeCollection collectionToShow = serializer.listAsNodeCollection(nodes);
//        PrintWriter printer = new PrintWriter(new File("src/main/resources/genomes/TB328.positions"));
//        printer.println("graph something blah blah");
//        for (Node node : collectionToShow.values()) {
//            Node startseg = serializer.getRecursiveNode(node.getStartNode());
//            Node endseg = serializer.getRecursiveNode(node.getEndNode());
//            printer.println("node " + startseg.getId() + " " + (((double) node.getStartNode().getX())/100.0) + " " + (((double) node.getStartNode().getY())/100.0) + " something");
//            printer.println("node " + endseg.getId() + " " + (((double) node.getEndNode().getX())/100.0) + " " + (((double) node.getEndNode().getY())/100.0) + " something");
//            System.out.println("node " + startseg.getId() + " " + (((double) node.getStartNode().getX())/100.0) + " " + (((double) node.getStartNode().getY())/100.0) + " something");
//            System.out.println("node " + endseg.getId() + " " + (((double) node.getEndNode().getX())/100.0) + " " + (((double) node.getEndNode().getY())/100.0) + " something");
//
//            for (Node child : node.getContainer()) {
//                Node childseg = serializer.getRecursiveNode(child);
//                printer.println("node " + childseg.getId() + " " + (((double) child.getX())/100.0) + " " + (((double) child.getY())/100.0) + " something");
//                System.out.println("node " + childseg.getId() + " " + (((double) child.getX())/100.0) + " " + (((double) child.getY())/100.0) + " something");
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