package model;

import org.json.JSONArray;

public class WorkerResponse {

    private final SearchEngine searchEngine;
    private final String query;
    private final JSONArray results;

    public WorkerResponse(SearchEngine searchEngine, String query, JSONArray results) {
        this.searchEngine = searchEngine;
        this.query = query;
        this.results = results;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public String getQuery() {
        return query;
    }

    public JSONArray getResults() {
        return results;
    }
}
