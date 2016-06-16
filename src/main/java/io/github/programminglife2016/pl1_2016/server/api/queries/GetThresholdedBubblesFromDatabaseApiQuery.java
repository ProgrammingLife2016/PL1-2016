package io.github.programminglife2016.pl1_2016.server.api.queries;

import io.github.programminglife2016.pl1_2016.database.FetchDatabase;
import io.github.programminglife2016.pl1_2016.server.api.actions.ApiAction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Listens to /api/nodes/[threshold] and return the data of segment [threshold].
 */
public class GetThresholdedBubblesFromDatabaseApiQuery implements ApiQuery {
    private FetchDatabase fdb;

    /**
     * Construct the ApiQuery.
     *
     * @param fdb database to retrieve the data information from
     */
    public GetThresholdedBubblesFromDatabaseApiQuery(FetchDatabase fdb) {
        this.fdb = fdb;
    }

    /**
     * Return the regex string of this query.
     *
     * @return the regex string of this query
     */
    @Override
    public String getQuery() {
        return "^/api/nodes/(\\d+)/(\\d+)/(\\d+)/(\\S+)/(\\d+)/(\\S+)$";
    }

    /**
     * Return the action of this query.
     *
     * @return the action of this query
     */
    @Override
    public ApiAction getApiAction() {
        return args -> {
            HashMap<Integer, ArrayList<String>> items = new HashMap<Integer, ArrayList<String>>();
            for (String highlighted : args.get(5).split("/")) {

                String[] list = highlighted.split("-");
                int id = Integer.parseInt(list[1]);
                String genome = list[0];
                ArrayList<String> itemsList = items.get(id);
                if (itemsList == null) {
                    itemsList = new ArrayList<>();
                    itemsList.add(genome);
                    items.put(id, itemsList);
                } else {
                    // add if item is not already in list
                    if (!itemsList.contains(genome)) {
                        itemsList.add(genome);
                    }
                }
            }
            return fdb.getNodes(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)), Integer.parseInt(args
                    .get(2)), Boolean.parseBoolean(args.get(3)), Integer.parseInt(args.get(4)), items).toString();
        };
    }
}
