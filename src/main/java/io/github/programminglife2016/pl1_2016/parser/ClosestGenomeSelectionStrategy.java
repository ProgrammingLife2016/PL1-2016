package io.github.programminglife2016.pl1_2016.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ClosestGenomeSelectionStrategy implements GenomeSelectionStrategy {
    @Override
    public Collection<String> genomes(TreeNode rootNode, int level) {
        List<TreeNode> treeNodes = rootNode.flatten().stream().collect(Collectors.toList());
        Collection<String> genomes = new ArrayList<>();
        for (int i = 0; i < Math.min(Math.pow(2, level), treeNodes.size()); i++) {
            genomes.add(treeNodes.get(i).getName());
        }
//        return genomes;
        return Arrays.asList("TKK 02 0007", "TKK 02 0006");
    }
}
