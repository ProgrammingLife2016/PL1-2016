package io.github.programminglife2016.pl1_2016.parser.nodes;

import io.github.programminglife2016.pl1_2016.parser.Parser;
import io.github.programminglife2016.pl1_2016.parser.metadata.Specimen;
import io.github.programminglife2016.pl1_2016.parser.metadata.SpecimenParser;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Temporary simple parser for parsing .gfa files.
 */
public class SegmentParser implements Parser {
    private static final String REFERENCE = "MT_H37RV_BRD_V5.ref";
    private static final String ATTR_ZINDEX = "START:Z:";
    private static final String ATTR_ORI = "ORI:Z:";
    private static final String GENOME_SUFFIX = ".fasta";
    private static final int SEGMENT_POSITION_FACTOR = 100;

    private NodeCollection nodeCollection;
    private InputStream positions;
    private Map<String, Subject> specimens;

    /**
     * Create parser object.
     */
    public SegmentParser() {
        nodeCollection = new NodeMap();
    }

    /**
     * Create the parser, providing a positions file where the segment positions are stored.
     *
     * @param positions information about the positions of the segments
     * @param metadata metadata of the segments
     */
    public SegmentParser(InputStream positions, InputStream metadata) {
        this();
        this.positions = positions;
        SpecimenParser specimenParser = new SpecimenParser();
        this.specimens = specimenParser.parse(metadata);
        Subject ref = new Specimen();
        ref.setNameId(REFERENCE);
        specimens.put(REFERENCE, ref);
    }

    /**
     * Create the parser, providing a positions file where the segment positions are stored.
     *
     * @param positions information about the positions of the segments
     */
    public SegmentParser(InputStream positions) {
        this();
        this.positions = positions;
    }

    /**
     * Read and parse the data from the input stream
     * @param inputStream input data
     * @return Data structure with parsed data.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public NodeCollection parse(InputStream inputStream) {
        read(inputStream);
        if (positions != null) {
            Scanner sc = new Scanner(positions, "UTF-8");
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(" ");
                if (line[0].equals("node")) {
                    nodeCollection.get(Integer.parseInt(line[1])).setXY(
                            (int) (Double.parseDouble(line[2]) * SEGMENT_POSITION_FACTOR),
                            (int) (Double.parseDouble(line[3]) * SEGMENT_POSITION_FACTOR));
                } else {
                    break;
                }
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
        if (!nodeCollection.containsKey(id)) {
            nodeCollection.put(id, new Segment(id, seq, column));
        } else {
            nodeCollection.get(id).setData(seq);
            nodeCollection.get(id).setColumn(column);
        }
        if (specimens != null) {
            Set<Subject> genomes = Arrays.asList(data[4].substring(ATTR_ORI.length()).split(";"))
                    .stream()
                    .map(x -> specimens.get(x.substring(0, x.length() - GENOME_SUFFIX.length())))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            nodeCollection.get(id).addGenomes(genomes);
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

    /**
     * Get the parsed metadata information.
     *
     * @return the parsed metadata information
     */
    public Map<String, Subject> getSubjects() {
        return specimens;
    }
}