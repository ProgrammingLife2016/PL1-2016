//CHECKSTYLE.OFF: MagicNumber
package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.Launcher;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentParser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertEquals;

/**
 * Test for the BubbleDispatcher class.
 */
public class BubbleDispatcherTest {
    private BubbleDispatcher dispatcher;
    public InputStream input;
    public InputStream pos;

    /**
     * Create parser object.
     *
     * @throws IOException thrown if the test data input faulty
     */
    @Before
    public void setUp() throws IOException {
        String meta = "H\tVN:Z:1.0\n" + "H\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta;\n" +
                "S\t1\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta\t" +
                "CRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t1\t+\t2\t+\t0M\n" +
                "S\t2\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta" + "\tCRD:Z:G-1"
                + ".fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t2\t+\t3\t+\t0M\n" +
                "L\t2\t+\t4\t+\t0M\n" + "S\t3\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:G-1.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t3\t+\t5\t+\t0M\n" +
                "S\t4\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:G-4.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t4\t+\t5\t+\t0M\n" +
                "S\t5\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta" + "\tCRD:Z:G-1"
                + ".fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1" + "\tSTART:Z:0\n" + "L\t5\t+\t6\t+\t0M\n" +
                "L\t5\t+\t7\t+\t0M\n" + "S\t6\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t6\t+\t8\t+\t0M\n" +
                "L\t6\t+\t9\t+\t0M\n" + "S\t7\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:Z:G-4.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t7\t+\t10\t+\t0M\n" +
                "L\t7\t+\t11\t+\t0M\n" + "S\t8\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta\tCRD:Z:G-1.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t8\t+\t12\t+\t0M\n" +
                "S\t9\tsomedData\t*\tORI:Z:G-3.fasta\tCRD:Z:G-3.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t9\t+\t12\t+\t0M\n" +
                "S\t10\tsomedData\t*\tORI:Z:G-5.fasta\tCRD:Z:G-5.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t10\t+\t13\t+\t0M\n" +
                "S\t11\tsomedData\t*\tORI:Z:G-4.fasta\tCRD:Z:G-4.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t11\t+\t13\t+\t0M\n" +
                "S\t12\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t12\t+\t15\t+\t0M\n" +
                "L\t12\t+\t14\t+\t0M\n" + "S\t13\tsomedData\t*\tORI:Z:G-4.fasta;G-5.fasta\tCRD:Z:G-4.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t13\t+\t16\t+\t0M\n" +
                "S\t14\tsomedData\t*\tORI:Z:G-3.fasta\tCRD:Z:G-3.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t14\t+\t15\t+\t0M\n" +
                "S\t15\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta\tCRD:Z:G-1.fasta" +
                "\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1\tSTART:Z:0\n" + "L\t15\t+\t16\t+\t0M\n" +
                "S\t16\tsomedData\t*\tORI:Z:G-1.fasta;G-2.fasta;G-3.fasta;G-4.fasta;G-5.fasta" + "\tCRD:Z:G-1"
                + ".fasta\tCRDCTG:Z:MT_H37RV_BRD_V5\tCTG:Z:NZ_KK327777.1" + "\tSTART:Z:0";
        input = stringToInputStream(meta);
        pos = Launcher.class.getClass().getResourceAsStream("/genomes/testGraph.positions");
        NodeCollection nodeCollection = new SegmentParser(pos, null).parse(input);
        dispatcher = new BubbleDispatcher(nodeCollection, "tests");
    }

    /**
     * Verify if the dispatcher has created the correct number of nodes.
     */
    @Test
    public void testDispatchingCorrectView() {
        NodeCollection testCollection = dispatcher.getThresholdedBubbles(4, false);
        assertEquals(7, testCollection.size());
    }

    /**
     * Converts a String to an InputStream
     *
     * @param s String
     * @return InputStream of that String.
     */
    public InputStream stringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }
}
