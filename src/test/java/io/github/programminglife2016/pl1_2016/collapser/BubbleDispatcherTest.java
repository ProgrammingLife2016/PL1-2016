package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ravishivam on 20-5-16.
 */
public class BubbleDispatcherTest {
    private NodeCollection nodeCollection;

    private BubbleDispatcher dispatcher;

    private static final String data = "H\tVN:Z:1.0\n" +
            "H\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta;\n" +
            "S\t1\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t1\t+\t2\t+\t0M\n" +
            "S\t2\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t2\t+\t3\t+\t0M\n" +
            "L\t2\t+\t4\t+\t0M\n" +
            "S\t3\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t3\t+\t5\t+\t0M\n" +
            "S\t4\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:G-4.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t4\t+\t5\t+\t0M\n" +
            "S\t5\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t5\t+\t6\t+\t0M\n" +
            "L\t5\t+\t7\t+\t0M\n" +
            "S\t6\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t6\t+\t8\t+\t0M\n" +
            "L\t6\t+\t9\t+\t0M\n" +
            "S\t7\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:Z:G-4.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t7\t+\t10\t+\t0M\n" +
            "L\t7\t+\t11\t+\t0M\n" +
            "S\t8\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t8\t+\t12\t+\t0M\n" +
            "S\t9\tsomedData\t*\tORI:Z:G-3.fasta\tCRD:Z:G-3.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t9\t+\t12\t+\t0M\n" +
            "S\t10\tsomedData\t*\tORI:Z:G-5.fasta\tCRD:Z:G-5.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t10\t+\t13\t+\t0M\n" +
            "S\t11\tsomedData\t*\tORI:Z:G-4.fasta\tCRD:Z:G-4.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t11\t+\t13\t+\t0M\n" +
            "S\t12\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t12\t+\t15\t+\t0M\n" +
            "L\t12\t+\t14\t+\t0M\n" +
            "S\t13\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:Z:G-4.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t13\t+\t16\t+\t0M\n" +
            "S\t14\tsomedData\t*\tORI:Z:G-3.fasta\tCRD:Z:G-3.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t14\t+\t15\t+\t0M\n" +
            "S\t15\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" +
            "L\t15\t+\t16\t+\t0M\n" +
            "S\t16\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0";

    /**
     * Create parser object.
     */
    @Before
    public void setUp() {
        InputStream is = stringToInputStream(data);
        nodeCollection = new SegmentParser().parse(is);
        dispatcher = new BubbleDispatcher(nodeCollection);
    }

    @Test
    public void testBubblingPosition() {
        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
        collapser.collapseBubbles();
        Coordinate coord = new Coordinate(0, 0);
        coord = collapser.bubbles.get(0).position(coord, collapser.bubbles, null, 1);
        for (Node bubble : dispatcher.bubbleCollection) {
            bubble.setXY(bubble.getStartNode().getX(), bubble.getStartNode().getY());
        }
        for (Node node : nodeCollection.values()) {
            Iterator<Node> linkIterator = node.getLinks().iterator();
            while (linkIterator.hasNext()) {
                Node nodeLink = linkIterator.next();
                if (nodeLink.getX() < node.getX()) {
                    linkIterator.remove();
                }
            }
        }
        assertEquals(nodeCollection.get(1).getId(), 1);
    }
    @Test
    public void testBubbleFirstZoomLevel() {
        BubbleCollapser collapser = new BubbleCollapser(nodeCollection);
        collapser.collapseBubbles();
        Coordinate coord = new Coordinate(0, 0);
        coord = collapser.bubbles.get(0).position(coord, collapser.bubbles, null, 1);
        for (Node bubble : dispatcher.bubbleCollection) {
            bubble.setXY(bubble.getStartNode().getX(), bubble.getStartNode().getY());
        }
        for (Node node : nodeCollection.values()) {
            Iterator<Node> linkIterator = node.getLinks().iterator();
            while (linkIterator.hasNext()) {
                Node nodeLink = linkIterator.next();
                if (nodeLink.getX() < node.getX()) {
                    linkIterator.remove();
                }
            }
        }
        NodeCollection testCollection = dispatcher.getLevelBubbles(0, 4);
        assertEquals(nodeCollection.get(1).getId(), 1);
    }

    @Test
    public void testSecondHandler() {
        PositionHandler ph = new PositionHandler();
        ph.calculatePositions(nodeCollection);
        assertEquals(nodeCollection.get(1).getX(), 0);
    }

    /**
     * Converts a String to an InputStream
     * @param s String
     * @return InputStream of that String.
     */
    public InputStream stringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }
}
