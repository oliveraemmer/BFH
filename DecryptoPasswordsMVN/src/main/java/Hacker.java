/*
 * Block Week 2 - Ping-Pong Akka example.
 */

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedList;

/**
 * A simple actor having the 'pong' role. It receives the initialization message
 * containing the reference to ping. After having received the pong, it sends a
 * ping to ping, and awaits the answer of ping. Having received the answer of
 * ping, this repeats indefinitely.
 */
public class Hacker extends AbstractBehavior<Hacker.Message> {

    interface Message {}
    private String name;
    private int hackCounter;
    LinkedList<String> passwords = new LinkedList<String>();

    ActorRef<Hacker.Message> hacker;

    public Hacker(ActorContext<Message> context) {
        super(context);
        name = context.getSelf().path().name();
    }

    public static Behavior<Hacker.Message> create() {
        return Behaviors.setup(Hacker::new);
    }

    public static class ReadPasswords implements  Hacker.Message {
        String passwordFile;
        public ReadPasswords(String PasswordFile){
            this.passwordFile = PasswordFile;
        }
    }

    public static class User implements Hacker.Message {
        String user;
        public User(String user) {
            this.user = user;
        }
    }

    static String hash(String algorithm,String textToHash) throws NoSuchAlgorithmException  {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
        byte[] hashedByetArray = digest.digest(byteOfTextToHash);
        String encoded = Base64.getEncoder().encodeToString(hashedByetArray);
        return encoded;
    }

    @Override
    public Receive<Hacker.Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(Hacker.ReadPasswords.class, this::onReadPasswords)
                .onMessage(Hacker.User.class, this::hack)
                .build();
    }

    private Behavior<Hacker.Message> onReadPasswords(ReadPasswords command){
        try(BufferedReader br = new BufferedReader(new FileReader(command.passwordFile))) {
            String pwd1 = br.readLine();
            while (pwd1 != null) {
                passwords.addLast(pwd1);
                pwd1 = br.readLine();
            }
        }
        catch(FileNotFoundException e1){
            System.err.println("File "+command.passwordFile+" not found");
            return this;
        }
        catch(IOException e2){
            System.err.println("Problem reading the file "+command.passwordFile);
            return this;
        }
        return this;
    }

    private Behavior<Hacker.Message> hack(User command) {
        System.out.println(name + " has hacked " + hackCounter + " times\n(" + command.user + ")\n\n");
        hackCounter++;

        String[] lineSplited = command.user.split(" ");
        if(lineSplited.length!=3){
            System.err.println("Malformed line"+command.user);
        }
        String _user = lineSplited[0];
        String hashedPassword = lineSplited[2];
        String salt = lineSplited[1];

        for(String pwd : passwords){
            try{
                String hashedPwd = hash("SHA-512",pwd+salt);
                if(hashedPwd.equals(hashedPassword)){
                    System.out.println(_user+" "+pwd);
                }

            }
            catch(NoSuchAlgorithmException e){
                System.err.println("Algorithm SHA 512 not implmented");
                return this;
            }

        }
        return this;
    }

}
