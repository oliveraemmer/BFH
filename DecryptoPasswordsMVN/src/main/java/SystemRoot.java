/*
 * Block Week 2 - Ping-Pong Akka example.
 */

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The root actor of the Akka ping-pong system. It spawns ping and pong child
 * actors and sets up the mutual references such that, as a next step, the
 * children can exchange messages.
 */
public class SystemRoot extends AbstractBehavior<SystemRoot.Init> {

    LinkedList<String> lines = new LinkedList<String>();
    int userCounter;
    int hackerNumber = 8;
    int terminatedCounter = 0;

    interface Init {}

    public static class createHackers implements Init{
        String userFile;
        String encryptedFile;
        public createHackers(String userFile, String encryptedFile){
            this.userFile = userFile;
            this.encryptedFile = encryptedFile;
        }
    }

    public static Behavior<Init> create() {
        return Behaviors.setup(SystemRoot::new);
    }

    List<ActorRef<Hacker.Message>> hackers = new LinkedList<ActorRef<Hacker.Message>>();

    public SystemRoot(ActorContext<Init> context) {
        super(context);
        for(int i = 0; i < 8; i++){
            ActorRef<Hacker.Message> hacker = context.spawn(Hacker.create(), "hacker" + i);
            getContext().watch(hacker);
            hackers.add(hacker);
        }
    }

    @Override
    public Receive<Init> createReceive() {
        return newReceiveBuilder()
                .onMessage(createHackers.class, this::startingHackers)
                .onSignal(Terminated.class, this::onTerminated)
                .build();
    }

    private Behavior<Init> startingHackers(createHackers command) {
        // every hacker reads all the encrypted entries
        for(int i = 0; i < hackerNumber; i++){
            hackers.get(i).tell(new Hacker.ReadPasswords(command.encryptedFile));
        }
        // reading users
        try(BufferedReader br = new BufferedReader(new FileReader(command.userFile))) {
            String line = br.readLine();

            while (line != null) {
                //System.out.println("line = "+line);
                userCounter++;
                lines.addLast(line);
                line = br.readLine();
            }

        }
        catch(FileNotFoundException e1){
            System.err.println("File "+command.userFile+" not found");
            return this;
        }
        catch(IOException e2){
            System.err.println("Problem reading the file "+command.userFile);
            return this;
        }
        // Start hacking
        for(int i = 1; i < userCounter; i++){
            hackers.get(i%hackerNumber).tell(new Hacker.User(lines.get(i)));
        }

        return this;
    }

    private Behavior<Init> onTerminated(Terminated command) {
        terminatedCounter++;
        if(terminatedCounter >= userCounter){
            getContext().getSystem().terminate();
        }
        return this;
    }

}
