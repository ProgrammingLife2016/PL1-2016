package io.github.programminglife2016.pl1_2016.parser;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Collector;

public class TokenIterator implements StringIterator {
    private String tokens;
    private int pos;
    private String delimiters;
    private StringBuilder sb;

    public TokenIterator(String tokens, String delimiters, boolean r) {
        this.delimiters = delimiters;
        this.tokens = tokens;
        reset();
    }

    public void reset() {
        this.pos = 0;
    }

    public String peek() {
        if (hasNext()) {
            int tempPos = this.pos;
            String next = this.next();
            this.pos = tempPos;
            return next;
        } else {
            return "";
        }
    }

    @Override
    public boolean hasNext() {
        return (pos < tokens.length());
    }

    @Override
    public String next() {
        sb = new StringBuilder();
        char c = tokens.charAt(pos);
        while (hasNext() && delimiters.indexOf(c) == -1) {
            sb.append(c);
            pos++;
            if (hasNext()) {
                c = tokens.charAt(pos);
            }
        }
        if (sb.length() == 0) {
            sb.append(c);
            pos++;
        }
        return sb.toString();
    }

    @Override
    public void remove() {

    }

    @Override
    public void forEachRemaining(Consumer<? super String> action) {

    }
}
