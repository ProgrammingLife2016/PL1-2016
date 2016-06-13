package io.github.programminglife2016.pl1_2016.parser.nodes;

/**
 * @author Kamran Tadzjibov on 08.06.2016.
 */
public class SequenceRange implements IndexRange {

    private String genomeName;
    private int start;
    private int end;

    /**
     * Create sequence range with genome name, start position and end position of segment.
     * @param genomeName name of the genome
     * @param start start position of genome
     * @param end end position of genome
     */
    public SequenceRange(String genomeName, int start, int end) {
        this.genomeName = genomeName;
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean isInInterval(int baseIndex) {
        return start <= baseIndex && baseIndex < end;
    }

    /**
     *
     * @return name of genome to which current range belongs
     */
    public String getGenomeName() {
        return genomeName;
    }

    /**
     *
     * @return start position of the segment
     */
    public int getStart() {
        return start;
    }

    /**
     *
     * @return end position of the segment
     */
    public int getEnd() {
        return end;
    }

    @Override
    public boolean isPartOf(String geneomeName) {
        return this.genomeName.equals(geneomeName);
    }
}
