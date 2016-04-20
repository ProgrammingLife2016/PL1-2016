package io.github.programminglife2016.pl1_2016.server;

import com.sun.net.httpserver.HttpHandler;

/**
 * A HTTP handler whose return data can be changed.
 */
interface MessageHandler extends HttpHandler {
    /**
     * Set the return message to be the argument.
     *
     * @param message the return data
     */
    void setMessage(String message);
}
