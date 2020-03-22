package model;

import java.util.List;
import java.util.stream.Collectors;

public class MasterResponse {

    private final List<WorkerResponse> workerResponses;
    private String error;

    public MasterResponse(List<WorkerResponse> workerResponses) {
        this.workerResponses = workerResponses;
    }

    public List<WorkerResponse> getWorkerResponses() {
        return workerResponses;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        String masterResponse = "{\"response\":\n[\n" + getWorkerResponses().stream().map(response ->
                "{\n" +
                        "\"query\": '" + response.getQuery() + "\",\n" +
                        "\"searchEngine\": '" + response.getSearchEngine().toString() + "\",\n" +
                        "\"results\": \"" + response.getResults().toString() + "\"\n}")
                .collect(Collectors.joining(",\n")) + "\n]";
        if (error != null) {
            masterResponse += ",\n\"error\": \"" + error + "\"";
        }
        masterResponse += "\n}";
        return masterResponse;
    }
}
