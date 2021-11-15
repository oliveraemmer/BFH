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
import ch.bfh.akka.pingpong.PingPongRoot.InitMessage;

/**
 * The root actor of the Akka ping-pong system. It spawns ping and pong child
 * actors and sets up the mutual references such that, as a next step, the
 * children can exchange messages.
 */
public class PingPongRoot extends AbstractBehavior<PingPongRoot.InitMessage> {

    interface InitMessage {}

    public static class createPingPong implements InitMessage{
        public createPingPong() {}
    }

    public static Behavior<InitMessage> create() {
        return Behaviors.setup(PingPongRoot::new);
    }

    private final ActorRef<Ping.Message> ping;
    private final ActorRef<Pong.Message> pong;

    public PingPongRoot(ActorContext<InitMessage> context) {
        super(context);
        ping = context.spawn(Ping.create(), "ping");
        pong = context.spawn(Pong.create(), "pong");
    }

    @Override
    public Receive<InitMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(createPingPong.class, this::onStart).build();
    }

    private Behavior<InitMessage> onStart(createPingPong command) {
        ping.tell(new Ping.Init(pong));
        pong.tell(new Pong.Init(ping));
        ping.tell(new Ping.Start());
        return this;
    }




}
