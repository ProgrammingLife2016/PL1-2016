package io.github.programminglife2016.pl1_2016.parser;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by ravishivam on 4-5-16.
 */
public class SpecimenParser implements Parser {
    private HashMap<String, Data> specimens;
    @Override
    public JsonSerializable parse(InputStream inputStream) {
        return this.specimens;
    }
}
