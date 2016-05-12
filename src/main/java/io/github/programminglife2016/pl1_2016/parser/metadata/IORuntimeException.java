package io.github.programminglife2016.pl1_2016.parser.metadata;

import java.io.IOException;

/**
 * Created by ali on 12-5-16.
 */
public class IORuntimeException extends RuntimeException {
    public IORuntimeException(IOException e) {
        super(e);
    }
}
