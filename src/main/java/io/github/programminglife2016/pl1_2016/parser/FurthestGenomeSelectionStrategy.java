package io.github.programminglife2016.pl1_2016.parser;

import io.github.programminglife2016.pl1_2016.parser.phylotree.TreeNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class FurthestGenomeSelectionStrategy implements GenomeSelectionStrategy {
    @Override
    public Collection<String> genomes(TreeNode rootNode, int level) {
        if (level == 0) {
            Optional<TreeNode> treeNode = rootNode.flatten().stream().findFirst();
            return Collections.singletonList(treeNode.get().getName());
        }
        if (rootNode.getChildren().isEmpty()) {
            return Collections.singletonList(rootNode.getName());
        } else {
            Collection<TreeNode> children = rootNode.getChildren();
            Collection<String> allGenomes = new ArrayList<>();
            for (TreeNode child : children) {
                Collection<String> genomes = genomes(child, level - 1);
                allGenomes.addAll(genomes);
            }
            return allGenomes;
        }
    }
}
