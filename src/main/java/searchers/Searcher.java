package searchers;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class Searcher {

    public abstract JSONArray search(String query) throws MalformedURLException;

    protected JSONArray getResults(URL url) {
        return StubServer.getResults(url);
    }
}
