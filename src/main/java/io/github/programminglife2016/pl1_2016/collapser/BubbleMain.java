package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.GraphvizParser;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import io.github.programminglife2016.pl1_2016.server.Server;
import io.github.programminglife2016.pl1_2016.server.api.RestServer;

import java.io.IOException;
import java.io.InputStream;


public class BubbleMain {
    private static final double NANOSECONDS_PER_SECOND = 1000000000;

    public static void main(String[] args) throws IOException {
        InputStream segis = BubbleMain.class.getClass().getResourceAsStream("/genomes/output.gfa");
        InputStream dotis = BubbleMain.class.getClass().getResourceAsStream("/genomes/output.txt");
        NodeCollection nodeCollection = (new GraphvizParser((new SegmentParser()).parse(segis))).parse(dotis);
//        BubbleDispatcher dispatcher = new BubbleDispatcher(nodeCollection);
//        System.out.println("reached");
//        System.out.println(nodeCollection.size());
//        Server server = new RestServer(dispatcher.getLevelBubbles(0,7));
        Server server = new RestServer(nodeCollection);
        server.startServer();
    }
}
