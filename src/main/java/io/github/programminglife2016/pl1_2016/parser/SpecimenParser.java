package io.github.programminglife2016.pl1_2016.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by ravishivam on 4-5-16.
 */
public class SpecimenParser implements Parser {

    private SpecimenMap specimens;

    public SpecimenParser() {
        specimens = new SpecimenMap();
    }

    @Override
    public JsonSerializable parse(InputStream inputStream) {
        read(inputStream);
        return specimens;
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
            reader.readLine();
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

    }
}
