package io.github.programminglife2016.pl1_2016.parser.nodes;

/**
 * @author Kamran Tadzjibov on 08.06.2016.
 */
public interface IndexRange {
    /**
     * Find out whether baseIndex is in the range.
     * @param baseIndex index of position of nucleotide base
     * @return return true if baseIndex is in the range
     */
    boolean isInInterval(int baseIndex);

    /**
     * Find out whether this range is a part of the given genome.
     * @param genomeName name of a genome which may contain this range
     * @return true if current range is a part of the given genome
     */
    boolean isPartOf(String genomeName);
}
