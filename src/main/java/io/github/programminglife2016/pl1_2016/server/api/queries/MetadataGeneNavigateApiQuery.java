package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.parser.metadata.Annotation;
import io.github.programminglife2016.pl1_2016.parser.nodes.NodeCollection;
import io.github.programminglife2016.pl1_2016.parser.nodes.Seeker;
import io.github.programminglife2016.pl1_2016.parser.nodes.SegmentSeeker;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;
import org.json.JSONObject;

import java.util.List;

/**
 * A query that responds to the /api/metadata/genenavigate/(\\S+)$,
 * and returns node and index in this node of the
 * base.
 */
public class MetadataGeneNavigateApiQuery implements ApiQuery {
    private NodeCollection nodeCollection;

    /**
     * Construct the ApiQuery.
     *
     * @param nodeCollection nodeCollection to retrieve the data information from
     */
    public MetadataGeneNavigateApiQuery(NodeCollection nodeCollection) {
        this.nodeCollection = nodeCollection;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/metadata/genenavigate/(.+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> {
            String str = args.get(0).replaceAll("%20", " ");
            Seeker sk = new SegmentSeeker(nodeCollection);
            for (Annotation annotation : nodeCollection.getAnnotations()) {
                if (annotation.getDisplayName().equals(str)) {
                    return sk.find("MT_H37RV_BRD_V5.ref", annotation.getStart()).toString();
                }
            }
            return "";
        };
    }
}
