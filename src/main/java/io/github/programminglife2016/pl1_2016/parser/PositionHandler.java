package io.github.programminglife2016.pl1_2016.parser;

import java.util.List;
import java.util.Map;

/**
 * Created by ravishivam on 23-4-16.
 */
public class PositionHandler {
    /**
     * Map containing the DNA seqments.
     */
    private SegmentMap segmentMap;

    /**
     * Map containing columns of based on Z-index and the segments it contains.
     */
    private Map<Integer, List<Integer>> columns;

    /**
     * Create a PositionHandler.
     * @param segmentMap Map containing all the segments which positions need to be calculated.
     * @param columns All the columns based on Z-index with the segments they contain.
     */
    public PositionHandler(SegmentMap segmentMap, Map<Integer, List<Integer>> columns) {
        this.columns = columns;
        this.segmentMap = segmentMap;
    }

    /**
     * Calculate the positions of the segments in the segmentMap.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public void calculatePositions() {
        int currx = 0;
        int spacing = 10;
        for (Map.Entry<Integer, List<Integer>> entry : columns.entrySet()) {
            List<Integer> segments = entry.getValue();
            if (segments.size() == 1) {
                segmentMap.get(segments.get(0)).setXY(currx,0);
                currx = currx + spacing;
                continue;
            }
            int boundary = (segments.size()-1)*5;
            for (Integer index :
                    segments) {
                segmentMap.get(index).setXY(currx, boundary);
                boundary = boundary - spacing;
            }
            currx = currx + spacing;
        }
    }
}
