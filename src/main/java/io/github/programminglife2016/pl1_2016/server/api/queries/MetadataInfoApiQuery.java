package io.github.programminglife2016.pl1_2016.server.api.queries;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.parser.metadata.Specimen;
import io.github.programminglife2016.pl1_2016.parser.metadata.Subject;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Collection;
import java.util.Map;
/**
 * Query that responds to /api/metriojgnnjemwfjugenrfwoejnreturns a subject
 */
public class MetadataInfoApiQuery implements ApiQuery {
    private Map<String, Subject> subjects;
    /**
     * Construct the ApiQuery.
     *
     * @param subjects database to retrieve the data information from
     */
    public MetadataInfoApiQuery(Map<String, Subject> subjects) {
        this.subjects = subjects;
    }
    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/metadata/info/(\\S+)$";
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
            JSONObject lineage = new JSONObject();
            lineage.put("specimen_id", args.get(0));
            Subject subject = subjects.getOrDefault(args.get(0), new Specimen("").setLineage(""));
            lineage.put("lineage", subject.getLineage());
            lineage.put("gdstpattern", subject.getGdstPattern());

            JSONArray tkkList = new JSONArray();
            subjects.entrySet().stream()
                    .filter(e -> e.getValue()
                                  .getLineage()
                                  .replace(" ", "-")
                                  .equals(args.get(0)))
                    .forEach(e -> tkkList.put(e.getValue().getNameId()));

            resp.put("status", "success");
            resp.put("subject", lineage);
            resp.put("tkkList", tkkList);
            return resp.toString();
        };
    }
}
