package io.github.programminglife2016.pl1_2016.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Classes that parse input data should implement this class. Provides a parser method.
 */
public interface Parser {
    /**
     * Parse the InputStream to an appropriate data structure.
     *
     * @param inputStream input data
     * @return parsed data
     */
    JsonSerializable parse(InputStream inputStream) throws IOException;
}
