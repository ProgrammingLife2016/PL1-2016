package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class to view bubbling changes
 */
public final class BubbleMain {
    private static final int THRESHOLD = 100;

    private BubbleMain() { }

    /**
     * Execute bubbling
     * @param args arguments.
     * @throws IOException thrown when reading the files fails.
     */
    public static void main(String[] args) throws IOException {
//        InputStream segis =
//              BubbleMain.class.getClass()
//                              .getResourceAsStream("/genomes/tb10_interestingpart.gfa");
//        InputStream dotis =
//              BubbleMain.class.getClass()
//                              .getResourceAsStream("/genomes/tb10_interestingpart.txt");
//        InputStream segis =
//              BubbleMain.class.getClass().getResourceAsStream("/genomes/output.gfa");
        InputStream dotis =
                BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.positions");

        InputStream is = BubbleMain.class.getClass().getResourceAsStream("/genomes/TB10.gfa");
        InputStream mt = BubbleMain.class.getClass().getResourceAsStream("/genomes/metadata.csv");
        NodeCollection nodeCollection = new SegmentParser(dotis, mt).parse(is);

        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
        nodeCollection = dispatcher.getThresholdedBubbles(THRESHOLD);
//        for(Map.Entry<Integer, Node> entry : nodeCollection.entrySet()) {
//            Node node = entry.getValue();
//            int x = (node.getStartNode().getX() + node.getEndNode().getX())/2;
//            int y = (node.getStartNode().getY() + node.getEndNode().getY())/2;
//            nodeCollection.get(entry.getKey()).setXY(x, y);
//        }
//
//        System.out.println(nodeCollection.size());
//        Server server = new RestServer(dispatcher.getThresholdedBubbles(0,7));
////        Server server = new RestServer(nodeCollection);
//        server.startServer();
    }
}
