package io.github.programminglife2016.pl1_2016.collapser;

import io.github.programminglife2016.pl1_2016.parser.nodes.Node;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeMap;

public class PositionHandler implements PositionManager{

    public NodeCollection calculatePositions(NodeCollection nodes){
        NodeCollection nodesRes = new NodeMap();
        int i = 0;
        for (Node node :
                nodes.values()) {
            node.setXY(i,0);
            nodesRes.put(node.getId(), node);
            i += 100;
        }
        return nodesRes;
    }

}

