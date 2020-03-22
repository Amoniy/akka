package model;

public class WorkerRequest {

    private final SearchEngine searchEngine;
    private final String query;
    private final int timeout;

    public WorkerRequest(SearchEngine searchEngine, String query, int timeout) {
        this.searchEngine = searchEngine;
        this.query = query;
        this.timeout = timeout;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public String getQuery() {
        return query;
    }

    public int getTimeout() {
        return timeout;
    }
}
