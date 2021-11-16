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
    List<String> foundPasswords = new ArrayList<String>();
    int userCounter;
    int hackerCount = 8;
    long start;

    interface Init {}

    List<ActorRef<Hacker.Message>> hackers = new LinkedList<ActorRef<Hacker.Message>>();
    ActorRef<SystemRoot.Init> system;

    public SystemRoot(ActorContext<Init> context) {
        super(context);
        system = getContext().getSelf();
        for(int i = 0; i < 8; i++){
            //ActorRef<Hacker.Message> hacker = context.spawn(Hacker.create(), "hacker" + i);
            //getContext().watch(hacker);
            hackers.add(context.spawn(Hacker.create(), "hacker" + i));
        }
    }

    public static Behavior<Init> create() {
        return Behaviors.setup(SystemRoot::new);
    }

    public static class CreateHackers implements Init{
        String userFile;
        String encryptedFile;
        long start;
        public CreateHackers(String userFile, String encryptedFile, long start){
            this.userFile = userFile;
            this.encryptedFile = encryptedFile;
            this.start = start;
        }
    }

    public static class FoundPasswords implements SystemRoot.Init{
        String foundPassword;
        public FoundPasswords(String foundPassword){
            this.foundPassword = foundPassword;
        }
    }

    @Override
    public Receive<Init> createReceive() {
        return newReceiveBuilder()
                .onMessage(CreateHackers.class, this::onCreateHackers)
                .onMessage(FoundPasswords.class, this::onFoundPasswords)
                .build();
    }

    private Behavior<Init> onCreateHackers(CreateHackers command) {
        // timer
        start = command.start;

        // every hacker reads all the encrypted entries
        for(int i = 0; i < hackerCount; i++){
            hackers.get(i).tell(new Hacker.ReadPasswords(command.encryptedFile));
        }

        // every hacker receives a reference to the SystemRoot
        for(int i = 0; i < hackerCount; i++){
            hackers.get(i).tell(new Hacker.GetReference(system));
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
        for(int i = 0; i < userCounter; i++){
            String hackerName = getContext().getChild("hacker" + i%hackerCount).get().path().name();
            System.out.println(hackerName + " received line Nr. " + (i+1) + " (" + lines.get(i) + ")");
            hackers.get(i % hackerCount).tell(new Hacker.User(lines.get(i)));
        }

        return this;
    }

    private Behavior<Init> onFoundPasswords(FoundPasswords command) {
        foundPasswords.add(command.foundPassword);
        System.out.println("Password Nr. " + foundPasswords.size() + " received: " + command.foundPassword);
        if(foundPasswords.size() >= userCounter){
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            long seconds = timeElapsed /1000;
            long millis = timeElapsed % 1000;
            System.err.println("Process duration = "+seconds+","+millis+"s");
        }
        return this;
    }

}
