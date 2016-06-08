package io.github.programminglife2016.pl1_2016.parser.nodes;

/**
 * @author Kamran Tadzjibov on 08.06.2016.
 */
public interface IndexRange {
    boolean isInInterval(int baseIndex);
    boolean isPartOf(String genomeName);
}
