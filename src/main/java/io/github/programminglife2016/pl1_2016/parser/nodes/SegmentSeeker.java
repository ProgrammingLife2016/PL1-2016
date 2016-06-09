package io.github.programminglife2016.pl1_2016.parser.nodes;


/**
 * @author  Kamran Tadzjibov on 09.06.2016.
 */
public class SegmentSeeker implements Seeker {

    private NodeCollection collection;
    public SegmentSeeker(NodeCollection collection) {
        this.collection = collection;
    }

    @Override
    public int[] find(String name, int position) {
        for(Node n : collection.values()) {
            if(n.getRangePerGenome().containsKey(name)
                    && n.getRangePerGenome().get(name).isInInterval(position))
                return new int[]{n.getId(), getLocalPosition(n.getRangePerGenome().get(name), position)};
        }
        return null;
    }

    private int getLocalPosition(SequenceRange range, int position){
        return position - range.getStart();
    }
}
