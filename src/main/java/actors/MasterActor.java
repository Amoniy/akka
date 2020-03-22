package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;
import model.MasterRequest;
import model.MasterResponse;
import model.SearchEngine;
import model.WorkerRequest;
import model.WorkerResponse;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MasterActor extends UntypedActor {

    public static final int SEARCH_ENGINE_COUNT = SearchEngine.values().length;
    private final ActorRef workerRouter;
    OutputStreamWriter outputWriter;
    private List<WorkerResponse> responses;
    private AnswerHolder answerHolder; // for testing purposes

    public MasterActor(OutputStreamWriter outputWriter, AnswerHolder answerHolder) {
        this.outputWriter = outputWriter;
        this.answerHolder = answerHolder;
        workerRouter = getContext().actorOf(new RoundRobinPool(SEARCH_ENGINE_COUNT).props(Props.create(WorkerActor.class)),
                "workerRouter");
        responses = new ArrayList<>();
        getContext().setReceiveTimeout(Duration.create("5 seconds"));
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof MasterRequest) {
            MasterRequest request = (MasterRequest) o;
            for (int i = 0; i < SEARCH_ENGINE_COUNT; i++) {
                workerRouter.tell(new WorkerRequest(SearchEngine.values()[i], request.getQuery(), request.getTimeout()), getSelf());
            }
        } else if (o instanceof WorkerResponse) {
            WorkerResponse response = (WorkerResponse) o;
            responses.add(response);
            if (responses.size() == SEARCH_ENGINE_COUNT) {
                handleExit(false);
            }
        } else if (o instanceof ReceiveTimeout) {
            handleExit(true);
            getContext().system().terminate();
        } else {
            unhandled(o);
        }
    }

    private void handleExit(boolean fail) throws IOException {
        MasterResponse response = new MasterResponse(responses);
        if (fail) {
            response.setError("Master actor timed out.");
        }
        outputWriter.write(response.toString());
        outputWriter.flush();
        answerHolder.setAnswer(response);
        context().stop(self());
    }

    public AnswerHolder getAnswerHolder() {
        return answerHolder;
    }
}
