package io.github.programminglife2016.pl1_2016.parser;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.function.Consumer;

/**
 * Adapter for the StringTokenizer class.
 */
public class StringTokenizerAdapter extends StringTokenizer implements StringIterator {

    /**
     * Create StringTokinzerAdapter object.
     * @param str string to split up into tokens.
     * @param delim delimters of the tokens.
     * @param returnDelims if the tokenizer should return the delimiters itself.
     */
    public StringTokenizerAdapter(String str, String delim, boolean returnDelims) {
        super(str, delim, returnDelims);
    }

    @Override
    public void remove() {

    }

    @Override
    public void forEachRemaining(Consumer<? super String> action) {

    }

    @Override
    public boolean hasNext() {
        return this.hasMoreTokens();
    }

    @Override
    public String next() {
        return this.nextToken();
    }

    @Override
    public String peek() {
        return null;
    }
}
