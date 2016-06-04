package io.github.programminglife2016.pl1_2016.parser.metadata;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import io.github.programminglife2016.pl1_2016.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GFFParser implements Parser {
    private InputStream is;
    private List<Annotation> annotations;

    public GFFParser(InputStream is) {
        this.is = is;
        this.annotations = new ArrayList<>();
    }

    /**
     * Parse data from inputStream.
     */
    public void read() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one line of a .gff file.
     * @param line line containig data.
     */
    private void parseLine(String line) {
        String[] data = line.split("\t");
        if (data[0].startsWith("##")) {
            return;
        }
        Annotation annotation = new Annotation()
                .setSeqId(data[0])
                .setSource(data[1])
                .setType(data[2])
                .setStart(Integer.parseInt(data[3]))
                .setEnd(Integer.parseInt(data[4]))
                .setScore(Float.parseFloat(data[5]))
                .setStrand(data[6])
                .setPhase(data[7]);
        String[] pairs = data[8].split(";");
        Arrays.stream(pairs).forEach(pair -> parseAttribute(annotation, pair));
        annotations.add(annotation);
    }

    /**
     * Parse one attribute from the last column in the .gff file.
     * @param annotation current annotation data structure.
     * @param data String containing the key, value pairs of additional attributes.
     */
    private void parseAttribute(Annotation annotation, String data) {
        String[] pair = data.split("=");
        switch(pair[0]) {
            case "calhounClass":
                annotation.setCalhounClass(pair[1]);
                break;
            case "Name":
                annotation.setName(pair[1]);
                break;
            case "ID":
                annotation.setId(pair[1]);
                break;
            case "displayName":
                annotation.setDisplayName(pair[1]);
                break;
        }
    }

    /**
     * Get the list of annotation objects.
     * @return list of annotations.
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public JsonSerializable parse(InputStream inputStream) {
        return null;
    }
}
