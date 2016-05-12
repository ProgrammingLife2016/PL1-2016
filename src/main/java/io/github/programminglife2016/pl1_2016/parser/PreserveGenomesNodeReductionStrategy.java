package io.github.programminglife2016.pl1_2016.parser;

import java.util.*;

public class PreserveGenomesNodeReductionStrategy implements NodeReductionStrategy {
    private Collection<String> genomes;
    private int currnode = 0;
    private int totalSize = 0;

    public PreserveGenomesNodeReductionStrategy(Collection<String> genomes) {
        this.genomes = genomes;
    }

    @Override
    public NodeCollection reduce(NodeCollection original) {
        System.out.println(genomes);
        this.totalSize = original.getNodes().size();
        NodeCollection filter = filter(original);
        NodeCollection aggregate = filter;
        for (int i = 0; i < 8; i++) {
            this.totalSize = aggregate.getNodes().size();
            this.currnode = 0;
            aggregate = aggregate(aggregate);
        }
        return aggregate;
    }

    private NodeCollection filter(NodeCollection original) {
        NodeCollection filtered = new NodeMap(original.getNodes().size());
        for (Node node : original) {
            printPercentage("Filtering");
            if (Collections.disjoint(genomes, node.getGenomes())) {
                continue;
            }
            Node clonedNode;
            if (filtered.containsKey(node.getId())) {
                clonedNode = filtered.get(node.getId());
            } else {
                filtered.put(node.getId(), node.clone());
                clonedNode = filtered.get(node.getId());
            }
            Collection<Node> newLinks = new ArrayList<>();
            Collection<Node> newBackLinks = new ArrayList<>();
            for (Node nodeFront : clonedNode.getLinks()) {
                if (Collections.disjoint(genomes, nodeFront.getGenomes())) {
                    continue;
                }
                Node clonedNodeFront;
                if (filtered.containsKey(nodeFront.getId())) {
                    clonedNodeFront = filtered.get(nodeFront.getId());
                } else {
                    filtered.put(nodeFront.getId(), nodeFront.clone());
                    clonedNodeFront = filtered.get(nodeFront.getId());
                }
                newLinks.add(clonedNodeFront);
            }
            for (Node nodeBack : clonedNode.getBackLinks()) {
                if (Collections.disjoint(genomes, nodeBack.getGenomes())) {
                    continue;
                }
                Node clonedNodeBack;
                if (filtered.containsKey(nodeBack.getId())) {
                    clonedNodeBack = filtered.get(nodeBack.getId());
                } else {
                    filtered.put(nodeBack.getId(), nodeBack.clone());
                    clonedNodeBack = filtered.get(nodeBack.getId());
                }
                newBackLinks.add(clonedNodeBack);
            }
            clonedNode.getLinks().clear();
            clonedNode.getBackLinks().clear();
            newLinks.stream().forEach(clonedNode::addLink);
            newBackLinks.stream().forEach(clonedNode::addBackLink);
        }
        System.out.println("\rFiltering... 100%");
        return filtered;
    }

    private NodeCollection aggregate(NodeCollection nodeCollection) {
        NodeCollection aggregated = new NodeMap(nodeCollection.getNodes().size());
        nodeCollection.getNodes().stream().forEach(x -> aggregated.put(x.getId(), x));
        Collection<Node> toRemove = new HashSet<>();
        for (Node node : aggregated) {
            printPercentage("Aggregating");
            while (node.getLinks().size() == 1 && node.getLinks().iterator().next().getBackLinks().size() == 1) {
                Node next = node.getLinks().iterator().next();
                toRemove.add(next);
                node.getLinks().clear();
                node.setData(node.getData() + next.getData());
                next.getLinks().stream().forEach(node::addLink);
                next.getLinks().clear();
                next.getBackLinks().clear();
                boolean azi = true;
                while (node.getLinks().size() >= 2 && azi) {
                    azi = false;
                    Collection<Node> linksToRemove = new ArrayList<>();
                    Collection<Node> linksToAdd = new ArrayList<>();
                    for (Node nodeFront : node.getLinks()) {
                        List<Node> intersection = new ArrayList<>(node.getLinks());
                        intersection.retainAll(nodeFront.getLinks());
                        if (!intersection.isEmpty()) {
                            toRemove.add(nodeFront);
                            linksToRemove.add(nodeFront);
                            linksToAdd.addAll(nodeFront.getLinks());
                            node.setData(node.getData() + nodeFront.getData());
                            nodeFront.getLinks().clear();
                            nodeFront.getLinks().clear();
                            azi = true;
                            break;
                        }
                    }
                    linksToRemove.stream().forEach(x -> node.getLinks().remove(x));
                    linksToAdd.stream().forEach(node::addLink);
                }
            }
        }
        System.out.println("\rAggregating... 100%");
        toRemove.stream().forEach(aggregated::removeNode);
        for (Node aggregatedNode : aggregated) {
            aggregatedNode.getBackLinks().clear();
        }
        for (Node aggregatedNode : aggregated) {
            aggregatedNode.getLinks().stream().forEach(x -> x.addBackLink(aggregatedNode));
        }
        return aggregated;
    }

        /**
          * Every 500 nodes, print the current percentage.
          */
    @SuppressWarnings("checkstyle:magicnumber")
    private void printPercentage(String action) {
//        if (currnode++ % (totalSize / 1000) == 0) {
            System.out.print(String.format("\r%s... %.1f%%.", action, (double) currnode
                    / totalSize * 100));
//        }
    }
}
