import actors.AnswerHolder;
import actors.MasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import model.MasterRequest;
import model.MasterResponse;
import model.SearchEngine;
import model.WorkerResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.OutputStreamWriter;

public class ActorsTest {

    @Test
    public void testPositive() {
        OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
        ActorSystem system = ActorSystem.create("QuerySystem");
        AnswerHolder answerHolder = new AnswerHolder();
        ActorRef master = system.actorOf(Props.create(MasterActor.class, outputWriter, answerHolder), "master");
        String query = "TEST";
        String workerAnswer = "[{\"link\":\"link0\",\"description\":\"description0\",\"title\":\"title0\"},{\"link\":\"link1\"," +
                "\"description\":\"description1\",\"title\":\"title1\"},{\"link\":\"link2\",\"description\":\"description2\"," +
                "\"title\":\"title2\"},{\"link\":\"link3\",\"description\":\"description3\",\"title\":\"title3\"},{\"link\":\"link4\"," +
                "\"description\":\"description4\",\"title\":\"title4\"}]";
        master.tell(new MasterRequest(query, 0), ActorRef.noSender());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MasterResponse response = answerHolder.getAnswer();
        Assert.assertNull(response.getError());
        Assert.assertEquals(3, response.getWorkerResponses().size());
        for (WorkerResponse workerResponse : response.getWorkerResponses()) {
            Assert.assertEquals(query, workerResponse.getQuery());
            if (workerResponse.getSearchEngine().equals(SearchEngine.BING)) {
                Assert.assertEquals(workerAnswer, workerResponse.getResults().toString());
            } else if (workerResponse.getSearchEngine().equals(SearchEngine.GOOGLE)) {
                Assert.assertEquals(workerAnswer, workerResponse.getResults().toString());
            } else if (workerResponse.getSearchEngine().equals(SearchEngine.YANDEX)) {
                Assert.assertEquals(workerAnswer, workerResponse.getResults().toString());
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void testTimeout() {
        String query = "TEST";
        OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
        ActorSystem system = ActorSystem.create("QuerySystem");
        AnswerHolder answerHolder = new AnswerHolder();
        ActorRef master = system.actorOf(Props.create(MasterActor.class, outputWriter, answerHolder), "master");
        String workerAnswer = "[{\"link\":\"link0\",\"description\":\"description0\",\"title\":\"title0\"},{\"link\":\"link1\"," +
                "\"description\":\"description1\",\"title\":\"title1\"},{\"link\":\"link2\",\"description\":\"description2\"," +
                "\"title\":\"title2\"},{\"link\":\"link3\",\"description\":\"description3\",\"title\":\"title3\"},{\"link\":\"link4\"," +
                "\"description\":\"description4\",\"title\":\"title4\"}]";
        master.tell(new MasterRequest(query, 10000), ActorRef.noSender());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MasterResponse response = answerHolder.getAnswer();
        Assert.assertEquals("Master actor timed out.", response.getError());
        Assert.assertEquals(2, response.getWorkerResponses().size());
        for (WorkerResponse workerResponse : response.getWorkerResponses()) {
            Assert.assertEquals(query, workerResponse.getQuery());
            if (workerResponse.getSearchEngine().equals(SearchEngine.BING)) {
                Assert.assertEquals(workerAnswer, workerResponse.getResults().toString());
            } else if (workerResponse.getSearchEngine().equals(SearchEngine.GOOGLE)) {
                Assert.assertEquals(workerAnswer, workerResponse.getResults().toString());
            } else if (workerResponse.getSearchEngine().equals(SearchEngine.YANDEX)) {
                Assert.fail();
            } else {
                Assert.fail();
            }
        }
    }
}
