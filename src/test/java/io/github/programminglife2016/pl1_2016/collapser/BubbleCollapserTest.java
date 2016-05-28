//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for the BubbleCollapser class.
 */
public class BubbleCollapserTest {
    private static final String DATA = "H\tVN:Z:1.0\n"
            + "H\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta;\n"
            + "S\t1\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:M"
            + "T_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t1\t+\t2\t+\t0M\n"
            + "S\t2\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta\tCRD:Z:G"
            + "-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t2\t+\t3\t+\t0M\n"
            + "L\t2\t+\t4\t+\t0M\n"
            + "S\t3\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:G-1.fasta\tCRDCTG:Z:MT_"
            + "H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t3\t+\t5\t+\t0M\n"
            + "S\t4\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:G-4.fasta\tCRDCTG:Z:MT_H37RV_BRD_"
            + "V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t4\t+\t5\t+\t0M\n"
            + "S\t5\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta\tCRD:Z:G"
            + "-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t5\t+\t6\t+\t0M\n"
            + "L\t5\t+\t7\t+\t0M\n"
            + "S\t6\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:M"
            + "T_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t6\t+\t8\t+\t0M\n"
            + "L\t6\t+\t9\t+\t0M\n"
            + "S\t7\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:Z:G-4.fasta\tCRDCTG:Z:MT_H37RV_BR"
            + "D_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t7\t+\t10\t+\t0M\n"
            + "L\t7\t+\t11\t+\t0M\n"
            + "S\t8\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:MT_H37RV_BR"
            + "D_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t8\t+\t12\t+\t0M\n"
            + "S\t9\tsomedData\t*\tORI:Z:G-3.fasta\tCRD:Z:G-3.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:"
            + "Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t9\t+\t12\t+\t0M\n"
            + "S\t10\tsomedData\t*\tORI:Z:G-5.fasta\tCRD:Z:G-5.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG"
            + ":Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t10\t+\t13\t+\t0M\n"
            + "S\t11\tsomedData\t*\tORI:Z:G-4.fasta\tCRD:Z:G-4.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG"
            + ":Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t11\t+\t13\t+\t0M\n"
            + "S\t12\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:"
            + "MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t12\t+\t15\t+\t0M\n"
            + "L\t12\t+\t14\t+\t0M\n"
            + "S\t13\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:Z:G-4.fasta\tCRDCTG:Z:MT_H37RV_B"
            + "RD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t13\t+\t16\t+\t0M\n"
            + "S\t14\tsomedData\t*\tORI:Z:G-3.fasta\tCRD:Z:G-3.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG"
            + ":Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t14\t+\t15\t+\t0M\n"
            + "S\t15\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\tCRDCTG:Z:"
            + "MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n"
            + "L\t15\t+\t16\t+\t0M\n"
            + "S\t16\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta\tCRD:Z:"
            + "G-1.fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0";
    private NodeCollection nodeCollection;
    private BubbleCollapser collapser;

    /**
     * Create parser object.
     *
     * @throws IOException thrown if the test data is faulty
     */
    @Before
    public void setUp() throws IOException {
        InputStream is = IOUtils.toInputStream(DATA, "UTF-8");
        nodeCollection = new SegmentParser().parse(is);
        collapser = new BubbleCollapser(nodeCollection);
    }

    /**
     * Verify that the end node had not changed after bubbling.
     */
    @Test
    public void testRetainEndNode() {
        collapser.collapseBubbles();
        Node bubble = collapser.getBubbles().get(2).getEndNode();
        assertEquals(bubble, nodeCollection.get(16));
    }

    /**
     * Verify that a correct bubble has formed.
     */
    @Test
    public void testSimpleBubbleCollapsing() {
        collapser.collapseBubbles();
        Node bubble = collapser.getBubbles().get(2);
        assertEquals(5, bubble.getStartNode().getId());
        assertEquals(16, bubble.getEndNode().getId());
    }
}
