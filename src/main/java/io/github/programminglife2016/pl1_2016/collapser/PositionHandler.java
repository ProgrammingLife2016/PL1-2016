package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;

import java.lang.instrument.Instrumentation;
import java.util.*;

public class PositionHandler implements PositionManager{

    private static final int SPACING = 100;
    private static final int FACTOR = 50;

    public NodeCollection calculatePositions(NodeCollection nodes){
        NodeCollection nodesRes = new NodeMap();
        Queue<Node> toVisit = new LinkedList<>();
        Node node = nodes.values().iterator().next();
        HashMap<Integer, Set<Node>> columns = new HashMap<>();
        HashMap<Node, Integer> nodetocolumns = new HashMap<>();
        columns.put(0, new HashSet<>());
        columns.get(0).add(node);
        nodetocolumns.put(node, 0);
        toVisit.add(node);
        while (!toVisit.isEmpty()){
            node = toVisit.remove();
            for (Node neighbour: node.getLinks()) {
                int newcol = nodetocolumns.get(node) + 1;
                if (!columns.containsKey(newcol)) {
                    columns.put(newcol, new HashSet<>());
                    columns.get(newcol).add(neighbour);
                    nodetocolumns.put(neighbour,newcol);
                }
                else {
                    columns.get(newcol).add(neighbour);
                    nodetocolumns.put(neighbour,newcol);
                }
                toVisit.add(neighbour);
            }
        }
        return calculatePosition(columns, nodes);
    }

    public NodeCollection calculatePosition(HashMap<Integer, Set<Node>> columns, NodeCollection nodeMap) {
        int currx = 0;
        for (Map.Entry<Integer, Set<Node>> entry: columns.entrySet()) {
            ArrayList<Node> nodes = new ArrayList<>(entry.getValue());
            if (nodes.size() == 1) {
                nodeMap.get(nodes.get(0).getId()).setXY(currx, 0);
                currx = currx + SPACING;
                continue;
            }
            int boundary = (nodes.size() - 1) * FACTOR;
            for (Node no : nodes) {
                int index = no.getId();
                nodeMap.get(index).setXY(currx, boundary);
                boundary = boundary - SPACING;
            }
            currx = currx + SPACING;
        }
        return nodeMap;
    }

}

