/*
 * Block Week 2 - Ping-Pong Akka example.
 */
package ch.bfh.akka.pingpong;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * A simple actor having the 'ping' role. It receives the initialization message
 * containing the reference to pong. After having received the start message, it
 * sends a pong to pong, and awaits the answer of pong. Having received the
 * answer of pong, this repeats indefinitely.
 */
public class Ping extends AbstractBehavior<Ping.Message> {

    ActorRef<Pong.Message> pong;

    interface Message {}

    public Ping(ActorContext<Message> context) {
        super(context);
    }

    public static Behavior<Ping.Message> create() {
        return Behaviors.setup(Ping::new);
    }

    public static class Init implements Ping.Message {
        ActorRef<Pong.Message> pong;
        public Init(ActorRef<Pong.Message> pong) {
            this.pong = pong;
        }
    }

    public static class Start implements Ping.Message {}

    public static class sayPing implements Ping.Message {}

    @Override
    public Receive<Ping.Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(Ping.Init.class, this::setUp)
                .onMessage(Ping.Start.class, this::pingStart)
                .onMessage(Ping.sayPing.class, this::sayingPing)
                .build();
    }

    private Behavior<Ping.Message> setUp(Init init) {
        pong = init.pong;
        System.out.println("ActorRef pong received");
        return this;
    }

    private Behavior<Ping.Message> pingStart(Start start){
        pong.tell(new Pong.sayPong());
        return this;
    }

    private Behavior<Ping.Message> sayingPing(sayPing sayPing){
        System.out.println("Ping\n");
        pong.tell(new Pong.sayPong());
        return this;
    }

}
