package io.github.programminglife2016.pl1_2016.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A HTTP request handler, that returns 200 OK with the message when a request is received.
 */
class SuccessHandler implements MessageHandler {
    private static final int HTTP_STATUS_OK = 200;
    private String message;

    /**
     * Set the initial message to be the argument.
     *
     * @param message the intiial messge
     */
    SuccessHandler(String message) {
        this.message = message;
    }

    /**
     * Set the return message to be the argument.
     *
     * @param message the return data
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Write 200 OK with the message as response.
     *
     * @param httpExchange the HTTP request
     * @throws IOException thrown if an error occurs during writing the response
     */
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(HTTP_STATUS_OK, message.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(message.getBytes("UTF-8"));
        os.close();
    }
}
