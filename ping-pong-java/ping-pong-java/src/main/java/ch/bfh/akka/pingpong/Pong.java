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
 * A simple actor having the 'pong' role. It receives the initialization message
 * containing the reference to ping. After having received the pong, it sends a
 * ping to ping, and awaits the answer of ping. Having received the answer of
 * ping, this repeats indefinitely.
 */
public class Pong extends AbstractBehavior<Pong.Message> {

    interface Message {}

    ActorRef<Ping.Message> ping;

    public Pong(ActorContext<Message> context) {
        super(context);
    }

    public static Behavior<Pong.Message> create() {
        return Behaviors.setup(Pong::new);
    }

    public static class Init implements Pong.Message {
        ActorRef<Ping.Message> ping;

        public Init(ActorRef<Ping.Message> ping) {
            this.ping = ping;
        }
    }

    public static class sayPong implements Pong.Message {}

    @Override
    public Receive<Pong.Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(Pong.Init.class, this::setUp)
                .onMessage(Pong.sayPong.class, this::sayingPong)
                .build();
    }

    private Behavior<Pong.Message> setUp(Init init) {
        ping = init.ping;
        System.out.println("ActorRef ping received");
        return this;
    }

    private Behavior<Pong.Message> sayingPong(sayPong sayPong) throws InterruptedException {
        System.out.println("Pong\n");
        Thread.sleep(1000);
        ping.tell(new Ping.sayPing());
        return this;
    }

}
