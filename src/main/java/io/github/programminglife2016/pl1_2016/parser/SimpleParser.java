package io.github.programminglife2016.pl1_2016.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Temporary simple parser for parsing .gfa files.
 */
public class SimpleParser implements Parser {
    private static final int SIZE = 9000;

    /**
     * Map containing the DNA seqments.
     */
    private SegmentMap segmentMap;

    /**
     * Create parser object.
     */
    public SimpleParser() {
        segmentMap = new SegmentMap(SIZE);
    }

    /**
     * Read and parse the data from the input stream
     * @param inputStream input data
     * @return Data structure with parsed data.
     */
    public JsonSerializable parse(InputStream inputStream) {
        read(inputStream);
        printSegments();
        return segmentMap;
    }

    private void printSegments() {
        for (Map.Entry<Integer, Segment> entry: segmentMap.entrySet()) {
            System.out.println("segment.id = " + entry.getValue().getId());
            for (Segment link: entry.getValue().getLinks()) {
                System.out.println("\tlink = " + link.getId());
            }
        }
    }

    /**
     * Parse data from inputStream.
     * @param inputStream stream of data.
     */
    private void read(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Parse one line of a .gfa file.
     * @param line contents of the specific line.
     */
    private void parseLine(String line) {
       line = line.trim();
       String[] data = line.split("\\s+");
       switch (data[0].charAt(0)) {
       case 'H':
           break;
       case 'S': parseSegmentLine(data);
           break;
       case 'L': parseLinkLine(data);
           break;
       default:
           break;
       }
    }

    /**
     * Parse a segment line according to the GFA specification.
     * @param data contents of line separated by whitespace.
     */
    private void parseSegmentLine(String[] data) {
        //S	4	G	*	ORI:Z:MT_H37RV_BRD_V5.
        int id = Integer.parseInt(data[1]);
        String seq = data[2];
        if (!segmentMap.containsKey(id)) {
            segmentMap.put(id, new Segment(id, seq));
        } else {
            segmentMap.get(id).setData(seq);
        }
    }

    /**
     * Parse a link line according to the GFA specification.
     * @param data contents of line separated by whitespace.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private void parseLinkLine(String[] data) {
        //L	1	+	2	+	0M
        int from = Integer.parseInt(data[1]);
        int to = Integer.parseInt(data[3]);
        if (!segmentMap.containsKey(to)) {
            segmentMap.put(to, new Segment(to));
        }
        segmentMap.get(from).addLink(segmentMap.get(to));
    }

    /**
     * Get the segmentMap containing all the segments.
     * @return hashmap of segments.
     */
    public SegmentMap getSegmentMap() {
        return segmentMap;
    }
}
