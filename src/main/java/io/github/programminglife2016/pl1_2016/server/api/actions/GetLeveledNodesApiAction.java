package io.github.programminglife2016.pl1_2016.server.api.actions;

import io.github.programminglife2016.pl1_2016.parser.*;

import java.util.Collection;
import java.util.List;

public class GetLeveledNodesApiAction implements ApiAction {
    private NodeCollection nodeCollection;
    private TreeNode rootNode;
    private GenomeSelectionStrategy genomeSelectionStrategy;

    public GetLeveledNodesApiAction(NodeCollection nodeCollection, TreeNode rootNode) {
        this.nodeCollection = nodeCollection;
        this.rootNode = rootNode;
        this.genomeSelectionStrategy = new RandomGenomeSelectionStrategy();
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
            PositionManager positionManager = new PositionHandler(reduced);
            positionManager.calculatePositions();
            String json = reduced.toJson();
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
