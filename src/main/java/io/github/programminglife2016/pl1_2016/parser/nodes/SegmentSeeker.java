package io.github.programminglife2016.pl1_2016.parser.nodes;


import org.json.JSONObject;

/**
 * @author Kamran Tadzjibov on 09.06.2016.
 */
public class SegmentSeeker implements Seeker {

    private NodeCollection collection;

    /**
     * Create seeker object and initialize it with collection of segments.
     * @param collection collection of segments
     */
    public SegmentSeeker(NodeCollection collection) {
        this.collection = collection;
    }

    @Override
    public JSONObject find(String name, int position) {
        JSONObject result = new JSONObject();
        for (Node n : collection.values()) {
            if (n.getRangePerGenome().containsKey(name)
                && n.getRangePerGenome().get(name).isInInterval(position)) {
                result.put("status", "success");
                result.put("id", n.getId());
                result.put("x", n.getX());
                result.put("y", n.getY());
                result.put("indexInSegment",
                        getLocalPosition(n.getRangePerGenome().get(name), position));
                return result;
            }
        }
        result.put("status", "error");
        return result;

    }


    private int getLocalPosition(SequenceRange range, int position) {
        return position - range.getStart();
    }
}
