/*
 * Block Week 2 - Ping-Pong Akka example.
 */
package ch.bfh.akka.pingpong;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ch.bfh.akka.pingpong.PingPongRoot.InitMessage;

/**
 * The root actor of the Akka ping-pong system. It spawns ping and pong child
 * actors and sets up the mutual references such that, as a next step, the
 * children can exchange messages.
 */
public class PingPongRoot extends AbstractBehavior<PingPongRoot.InitMessage> {

    interface InitMessage {}

    public static class createPingPong implements InitMessage{
        public final String name;

        public createPingPong(String name) {
            this.name = name;
        }
    }

    public static Behavior<InitMessage> create() {
        return Behaviors.setup(PingPongRoot::new);
    }

    public PingPongRoot(ActorContext<InitMessage> context) {
        super(context);
        System.out.println("Hello World");
    }

    @Override
    public Receive<InitMessage> createReceive() {
        return newReceiveBuilder().onMessage(createPingPong.class, this::test).build();
    }

    private Behavior<InitMessage> test(createPingPong command) {
        System.out.println("Test");
        return this;
    }




}
