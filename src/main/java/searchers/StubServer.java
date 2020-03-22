package searchers;

import model.SearchResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class StubServer {

    public StubServer() {

    }

    public static JSONArray getResults(URL url) {
        JSONArray results = new JSONArray();
        for (int i = 0; i < 5; i++) {
            SearchResult result = new SearchResult("title" + i, "link" + i, "description" + i);
            JSONObject jsonObject = new JSONObject(result);
            results.put(jsonObject);
        }
        return results;
    }
}
