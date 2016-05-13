package io.github.programminglife2016.pl1_2016.server.api.actions;

import io.github.programminglife2016.pl1_2016.parser.*;
import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;
import io.github.programminglife2016.pl1_2016.parser.nodes.PositionHandler;
import io.github.programminglife2016.pl1_2016.parser.phylotree.TreeNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GetLeveledBoundedNodesApiAction implements ApiAction {
    private static final int SLACK = 200;
    private NodeCollection nodeCollection;
    private TreeNode rootNode;
    private GenomeSelectionStrategy genomeSelectionStrategy;

    public GetLeveledBoundedNodesApiAction(NodeCollection nodeCollection, TreeNode rootNode) {
        this.nodeCollection = nodeCollection;
        this.rootNode = rootNode;
        this.genomeSelectionStrategy = new FurthestGenomeSelectionStrategy();
    }

    /**
     * Compute a response based on the query arguments.
     *
     * @param args query arguments
     * @return response to the client
     */
    @Override
    public String response(List<String> args) {
        try {
            Collection<String> genomes = genomeSelectionStrategy.genomes(rootNode, Integer.parseInt(args.get(0)));
            NodeReductionStrategy nodeReductionStrategy = new PreserveGenomesNodeReductionStrategy(genomes);
            NodeCollection reduced = nodeReductionStrategy.reduce(nodeCollection);
            new PositionHandler(reduced).calculatePositions();
            int lowerBound = Integer.parseInt(args.get(1)) - SLACK;
            int upperBound = Integer.parseInt(args.get(2)) + SLACK;
            List<Node> toKeep = reduced.getNodes().stream().filter(x -> lowerBound < x.getX() && x.getX() < upperBound).collect(Collectors.toList());
            NodeCollection reducedBounded = new NodeMap(toKeep.size());
            toKeep.stream().forEach(x -> reducedBounded.put(x.getId(), x));
            return reducedBounded.toJson();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
