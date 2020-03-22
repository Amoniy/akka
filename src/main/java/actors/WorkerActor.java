package actors;

import akka.actor.UntypedActor;
import model.SearchEngine;
import model.WorkerRequest;
import model.WorkerResponse;
import org.json.JSONArray;
import searchers.BingSearcher;
import searchers.GoogleSearcher;
import searchers.Searcher;
import searchers.YandexSearcher;

public class WorkerActor extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof WorkerRequest) {
            WorkerRequest request = (WorkerRequest) o;
            SearchEngine searchEngine = request.getSearchEngine();
            String query = request.getQuery();
            Searcher searcher;
            switch (searchEngine) {
                case BING:
                    searcher = new BingSearcher(0);
                    break;
                case GOOGLE:
                    searcher = new GoogleSearcher(0);
                    break;
                case YANDEX:
                    searcher = new YandexSearcher(request.getTimeout());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected search engine: " + searchEngine);
            }
            JSONArray results = searcher.search(query);
            WorkerResponse response = new WorkerResponse(searchEngine, query, results);
            getSender().tell(response, getSelf());
        } else {
            unhandled(o);
        }
    }
}
