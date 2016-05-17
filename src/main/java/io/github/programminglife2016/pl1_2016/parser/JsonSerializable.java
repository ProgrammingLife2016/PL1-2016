package io.github.programminglife2016.pl1_2016.parser;

/**
 * Data is sent by the server in JSON format. Classes extending this method can convert their
 * representation to JSON.
 */
public interface JsonSerializable {
    /**
     * Convert the data type to JSON.
     *
     * @return JSON representation of this data type.
     */
    String toJson();
}
