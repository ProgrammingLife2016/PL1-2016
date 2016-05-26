package io.github.programminglife2016.pl1_2016.parser.nodes;

import io.github.programminglife2016.pl1_2016.parser.Parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Temporary simple parser for parsing .gfa files.
 */
public class SegmentParser implements Parser {
    private static final String ATTR_ZINDEX = "START:Z:";
    private static final String ATTR_ORI = "ORI:Z:";
    private static final String GENOME_SUFFIX = ".fasta";

    /**
     * Map containing the DNA seqments.
     */
    private NodeCollection nodeCollection;

    /**
     * Create parser object.
     */
    public SegmentParser() {
        nodeCollection = new NodeMap();
    }

    /**
     * Read and parse the data from the input stream
     * @param inputStream input data
     * @return Data structure with parsed data.
     */
    public NodeCollection parse(InputStream inputStream) {
        read(inputStream);
        Scanner sc = new Scanner(SegmentParser.class.getResourceAsStream("/genomes/TB328.dot"));
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(" ");
            if (line[0].equals("node")) {
                nodeCollection.get(Integer.parseInt(line[1])).setXY((int) (Double.parseDouble(line[2]) * 100), (int) (Double.parseDouble(line[3]) * 100));
            } else {
                break;
            }
        }
        return nodeCollection;
    }

    /**
     * Parse data from inputStream.
     * @param inputStream stream of data.
     */
    private void read(InputStream inputStream) {
        System.out.println("Parsing...");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
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
    @SuppressWarnings("checkstyle:magicnumber")
    private void parseSegmentLine(String[] data) {
        int id = Integer.parseInt(data[1]);
        String seq = data[2];
        int column = 0;
        if (data[data.length - 1].contains(ATTR_ZINDEX)) {
            column = Integer.parseInt(data[data.length - 1].split(":")[2]);
        }
        Collection<String> genomes = Arrays.asList(data[4].substring(ATTR_ORI.length()).split(";"))
                .stream()
                .map(x -> x.substring(0, x.length() - GENOME_SUFFIX.length()))
                .map(x -> x.replace("_", " ").replace("-", " ")).collect(Collectors.toList());
        if (!nodeCollection.containsKey(id)) {
            nodeCollection.put(id, new Segment(id, seq, column));
        } else {
            nodeCollection.get(id).setData(seq);
            nodeCollection.get(id).setColumn(column);
        }
        nodeCollection.get(id).addGenomes(genomes);
    }

    /**
     * Parse a link line according to the GFA specification.
     * @param data contents of line separated by whitespace.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private void parseLinkLine(String[] data) {
        int from = Integer.parseInt(data[1]);
        int to = Integer.parseInt(data[3]);

        if (!nodeCollection.containsKey(to)) {
            nodeCollection.put(to, new Segment(to));
        }
        nodeCollection.get(from).addLink(nodeCollection.get(to));
        nodeCollection.get(to).addBackLink(nodeCollection.get(from));
    }

    /**
     * Get the nodeCollection containing all the segments.
     * @return hashmap of segments.
     */
    public NodeCollection getSegmentCollection() {
        return nodeCollection;
    }
}
