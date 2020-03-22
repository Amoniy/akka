import actors.AnswerHolder;
import actors.MasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import model.MasterRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        ActorSystem system = ActorSystem.create("QuerySystem");

        String in;
        while ((in = bufferedReader.readLine()) != null) {
            if (in.equals("exit")) {
                System.exit(0);
            }
            OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
            ActorRef master = system.actorOf(Props.create(MasterActor.class, outputWriter, new AnswerHolder()), "master");
            master.tell(new MasterRequest(in, 0), ActorRef.noSender());
        }
    }
}
