package io.github.programminglife2016.pl1_2016.parser;

import java.util.Iterator;

/**
 * Interface for String tokenizer.
 */
public interface StringIterator extends Iterator<String> {
    /**
     * Get the next string from the iterator.
     * @return next string.
     */
    String peek();
}
