package io.github.programminglife2016.pl1_2016.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Temporary simple parser for parsing .gfa files.
 */
public class SimpleParser implements Parser {
    private static final int SIZE = 9000;
    private static final String ATTR_ZINDEX = "START:Z:";

    private Map<Integer, List<Integer>> reversedLinks;
    /**
     * Map containing each column and how many segments the column contains.
     */
    private Map<Integer, List<Integer>> columns;

    /**
     * Map containing the DNA seqments.
     */
    private NodeCollection segmentMap;

    /**
     * Create parser object.
     */
    public SimpleParser() {
        segmentMap = new NodeMap(SIZE);
    }

    /**
     * Read and parse the data from the input stream
     * @param inputStream input data
     * @return Data structure with parsed data.
     */
    public JsonSerializable parse(InputStream inputStream) {
        columns = new TreeMap<Integer, List<Integer>>();
        reversedLinks = new HashMap<>();
        read(inputStream);
        return segmentMap;
    }

    /**
     * Parse data from inputStream.
     * @param inputStream stream of data.
     */
    private void read(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
            this.columns = reassignColumns(this.columns, this.reversedLinks);
            PositionManager positionHandler = new PositionHandler(this.segmentMap, this.columns);
            positionHandler.calculatePositions();
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

    private Map<Integer, List<Integer>> reassignColumns(Map<Integer, List<Integer>> columns, Map<Integer, List<Integer>> reversedLinks) {
        int currentColumn = 1;
        Map<Integer, List<Integer>> revisedGraph = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry :
                columns.entrySet()) {
            revisedGraph.put(currentColumn, new ArrayList<>());
            for (Integer node : entry.getValue()) {
                int correctColumn = currentColumn + findDeltaColumn(node, columns, reversedLinks);
                revisedGraph.get(correctColumn).add(node);
            }
            currentColumn++;
        }
        return revisedGraph;
    }

    public int findDeltaColumn(int node, Map<Integer, List<Integer>> columns, Map<Integer, List<Integer>> reversedLinks) {
        List<Integer> incomingLinks = reversedLinks.get(node);
        if(incomingLinks == null) return 0;
        if (incomingLinks.size() >= 2) {
            for (int i = 0; i < incomingLinks.size(); i++) {
                int firstcol = this.segmentMap.get(incomingLinks.get(i)).getColumn();
                for (int j = i+1; j < incomingLinks.size() ; j++) {
                    int secondcol = this.segmentMap.get(incomingLinks.get(j)).getColumn();
                    if (firstcol != secondcol) {
                        return -1;
                    }
                }
            }
        }
        return 0;
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
        int id = Integer.parseInt(data[1]);
        String seq = data[2];
        int column = 0;
        if (data[data.length - 1].contains(ATTR_ZINDEX)) {
            column = Integer.parseInt(data[data.length - 1].split(":")[2]);
            if (!columns.containsKey(column)) {
                columns.put(column, new ArrayList<Integer>());
                columns.get(column).add(id);
            }
            else {
                columns.get(column).add(id);
            }
        }
        if (!segmentMap.containsKey(id)) {
            segmentMap.put(id, new Segment(id, seq, column));
        } else {
            segmentMap.get(id).setData(seq);
            segmentMap.get(id).setColumn(column);
        }
    }

    /**
     * Parse a link line according to the GFA specification.
     * @param data contents of line separated by whitespace.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private void parseLinkLine(String[] data) {
        int from = Integer.parseInt(data[1]);
        int to = Integer.parseInt(data[3]);

        if (!reversedLinks.containsKey(to)) {
            reversedLinks.put(to, new ArrayList<>());
        }
        reversedLinks.get(to).add(from);

        if (!segmentMap.containsKey(to)) {
            segmentMap.put(to, new Segment(to));
        }
        segmentMap.get(from).addLink(segmentMap.get(to));
    }

    /**
     * Get the segmentMap containing all the segments.
     * @return hashmap of segments.
     */
    public NodeCollection getSegmentCollection() {
        return segmentMap;
    }
}
