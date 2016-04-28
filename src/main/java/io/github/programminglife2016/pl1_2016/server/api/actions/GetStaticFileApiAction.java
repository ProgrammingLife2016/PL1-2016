package io.github.programminglife2016.pl1_2016.server.api.actions;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Return a static file (HTML, CSS, JS, etc.) upon request.
 */
public class GetStaticFileApiAction implements ApiAction {
    /**
     * Compute a response based on the query arguments.
     *
     * @param args query arguments
     * @return response to the client
     */
    @Override
    public String response(List<String> args) {
        try {
            String uri = String.format("/webapp/%s", args.get(0));
            InputStream is = GetStaticFileApiAction.class.getResourceAsStream(uri);
            String ret = IOUtils.toString(is, "UTF-8");
            return ret;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return "404";
        }
    }
}
