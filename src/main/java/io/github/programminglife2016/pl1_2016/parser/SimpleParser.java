package io.github.programminglife2016.pl1_2016.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Temporary simple parser for parsing .gfa files.
 */
public class SimpleParser implements Parser, JsonSerializable {

    /**
     * Map containing the DNA seqments.
     */
    private Map<Integer, Segment> segmentMap;

    public SimpleParser() {
        segmentMap = new HashMap<Integer, Segment>(9000);
    }

    public JsonSerializable parse(InputStream inputStream) {
        return this;
    }

    public String toJson() {
        return "";
    }

    private void printSegments() {
        for(Map.Entry<Integer, Segment> entry: segmentMap.entrySet()) {
            System.out.println("segment.id = " + entry.getValue().getId());
            for (Segment link: entry.getValue().getLinks()) {
                System.out.println("\tlink = " + link.getId());
            }
        }
    }

    /**
     * Read lines of the file and parse data to data structure.
     * @param filename fileName of .gfa file.
     */
    private void readFile(String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
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
       case 'H': parseHeaderLine(data);
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
     * Parse a header line according to the GFA specification.
     * @param data contents of line separated by whitespace.
     */
    private void parseHeaderLine(String[] data) {
        return;
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
    private void parseLinkLine(String[] data) {
        //L	1	+	2	+	0M
        int from = Integer.parseInt(data[1]);
        String fromOrient = data[2];
        int to = Integer.parseInt(data[3]);
        String toOrient = data[4];
        String overlap = data[5];
        if (!segmentMap.containsKey(to)) {
            segmentMap.put(to, new Segment(to));
        }
        segmentMap.get(from).addLink(segmentMap.get(to));
        segmentMap.get(to).addLink(segmentMap.get(from));
    }
}
