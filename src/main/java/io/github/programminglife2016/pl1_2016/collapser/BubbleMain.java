package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.Launcher;
import io.github.programminglife2016.pl1_2016.parser.ObjectSerializer;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


        ObjectSerializer serializer = new ObjectSerializer();

        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
        System.out.println("reached");
        NodeCollection collectionToShow = dispatcher.getThresholdedBubbles(4, false);
        collectionToShow.assignNewPositions(Launcher.class.getResourceAsStream("/genomes/tb328-filtered.positions"));

        List<Node> bubblesWithPositions = new ArrayList<Node>(collectionToShow.values());
        serializer.serializeItem(bubblesWithPositions, "src/main/resources/objects/bubbles-with-positions.ser");

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
//
        //========================================
//        for (Node node : nodeCollection.values()) {
//            if (node.getId() > 78053)
//            node.getLinks().forEach(x -> System.out.println(node.getId() + " -> " + x.getId()));
//        }
    }
}
