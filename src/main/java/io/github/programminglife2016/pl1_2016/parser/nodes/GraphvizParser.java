package io.github.programminglife2016.pl1_2016.parser.nodes;

import io.github.programminglife2016.pl1_2016.collapser.GraphExplorer;
import io.github.programminglife2016.pl1_2016.parser.JsonSerializable;
import io.github.programminglife2016.pl1_2016.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static javax.imageio.ImageIO.read;


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
            String line;
            while ((line = reader.readLine()) != null) {
//                parseLine(line);
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

}
