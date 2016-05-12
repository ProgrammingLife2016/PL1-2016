package io.github.programminglife2016.pl1_2016.parser;

import io.github.programminglife2016.pl1_2016.parser.phylotree.TreeNode;

import java.util.Collection;

public interface GenomeSelectionStrategy {
    Collection<String> genomes(TreeNode rootNode, int level);
}
