package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.parser.metadata.Specimen;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Map;

/**
 * Query that responds to /api/metadata/genomes and returns all genomes
 */
public class MetadataGenomeApiQuery implements ApiQuery {
    private Map<String, Subject> subjects;
    /**
     * Construct the ApiQuery.
     *
     * @param subjects database to retrieve the data information from
     */
    public MetadataGenomeApiQuery(Map<String, Subject> subjects) {
        this.subjects = subjects;
    }
    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/metadata/genomes$";
    }
    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> {
            JSONObject resp = new JSONObject();
            JSONObject data = new JSONObject();
            subjects.entrySet().forEach(s -> data.put(s.getKey(), s.getValue().getGdstPattern()));
            resp.put("status", "success");
            resp.put("data", data);
            return resp.toString();
        };
    }
}
