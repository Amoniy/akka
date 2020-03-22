package searchers;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;

public class GoogleSearcher extends Searcher {

    private final int timeout;

    public GoogleSearcher(int timeout) {
        this.timeout = timeout;
    }

    private static final String HOST = "https://google.com";

    @Override
    public JSONArray search(String query) throws MalformedURLException {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = new URL(HOST + "/?q=" + query);
        return getResults(url);
    }
}
