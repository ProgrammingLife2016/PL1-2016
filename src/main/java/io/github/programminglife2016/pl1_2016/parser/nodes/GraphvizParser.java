package io.github.programminglife2016.pl1_2016.parser.nodes;

import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import io.github.programminglife2016.pl1_2016.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ravishivam on 20-5-16.
 */
public class GraphvizParser implements Parser {
    NodeCollection nodeCollection;

    public GraphvizParser() {
        nodeCollection = new NodeMap();
    }
    @Override
    public JsonSerializable parse(InputStream inputStream) throws IOException {
        readFile(inputStream);
        return nodeCollection;
    }

    private void readFile(InputStream inputStream) {
        System.out.println("Parsing...");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            reader.readLine();
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

    private void parseLine(String line) {
        String[] data = line.split(" ");
        switch (data[0]) {
            case "node" :
                handleNode(data);
                break;
            case "edge" :
                handleEdge(data);
                break;
            case "stop" :
                break;

        }
    }

    private void handleNode(String[] data) {
        Node node = new Segment(Integer.parseInt(data[1]));
        node.setXY(Integer.parseInt(data[2]), Integer.parseInt(data[3]));
        this.nodeCollection.put(node.getId(), node);
    }

    private void handleEdge(String[] data) {
        int from = Integer.parseInt(data[1]);
        int to = Integer.parseInt(data[2]);
        this.nodeCollection.get(from).addLink(this.nodeCollection.get(to));
    }

}
