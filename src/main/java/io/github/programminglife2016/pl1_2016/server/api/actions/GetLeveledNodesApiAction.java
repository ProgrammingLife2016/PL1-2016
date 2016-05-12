package io.github.programminglife2016.pl1_2016.server.api.actions;

import io.github.programminglife2016.pl1_2016.parser.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class GetLeveledNodesApiAction implements ApiAction {
    private NodeCollection nodeCollection;
    private TreeNode rootNode;
    private GenomeSelectionStrategy genomeSelectionStrategy;

    public GetLeveledNodesApiAction(NodeCollection nodeCollection, TreeNode rootNode) {
        this.nodeCollection = nodeCollection;
        this.rootNode = rootNode;
        this.genomeSelectionStrategy = new ClosestGenomeSelectionStrategy();
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
            return reduced.toJson();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void parseCSV(NodeCollection nodeCollection) {
        InputStream is = GetLeveledNodesApiAction.class.getResourceAsStream("/genomes/TB328.csv");
        Scanner sc = new Scanner(is);
        sc.useDelimiter("[\\s,]");
        while (sc.hasNext()) {
            int id = sc.nextInt();
            int x = sc.nextInt() / 1000;
            int y = sc.nextInt();
            nodeCollection.get(id).setXY(x, y);
        }
    }
}
