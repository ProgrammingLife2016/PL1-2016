package io.github.programminglife2016.pl1_2016.parser.nodes;

/**
 * @author Kamran Tadzjibov on 08.06.2016.
 */
public class SequenceRange implements IndexRange {

    private String genomeName;
    private int start;
    private int end;

    public SequenceRange(String genomeName, int start, int end) {
        this.genomeName = genomeName;
        this.start = start;
        this.end = end;
    }

    public boolean isInInterval(int baseIndex){
        return start <= baseIndex && baseIndex < end;
    }

    public String getGenomeName() {
        return genomeName;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isPartOf(String geneomeName) {
        return this.genomeName.equals(geneomeName);
    }
}
