package searchers;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;

public class YandexSearcher extends Searcher {

    private final int timeout;

    public YandexSearcher(int timeout) {
        this.timeout = timeout;
    }

    public static final String YA_HOST = "https://yandex.ru";

    @Override
    public JSONArray search(String query) throws MalformedURLException {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = new URL(YA_HOST + "/?q=" + query);
        return getResults(url);
    }
}
