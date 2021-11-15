/*
 * Block Week 2 - Ping-Pong Akka example.
 */
package ch.bfh.akka.pingpong;

import java.io.IOException;

import akka.actor.typed.ActorSystem;
import ch.bfh.akka.pingpong.PingPongRoot.InitMessage;

/**
 * This is the main class of a simple Akka ping pong Java application. A root actor
 * PingPongMain is created which, upon receiving an init message, creates the
 * two actors 'ping' and 'pong' via the 'aContext.spawn()' methods. The two
 * actors are then configured with the references of the other actor (i.e., pong
 * gets the reference of ping, and ping gets the reference of pong). Finally,
 * ping is sent the start message, and from now on ping and pong exchange
 * messages. Hit the 'ENTER' key to terminate the Akka application.
 */
public class PingPongMain {

	/**
	 * Main entry point of the application. Hit the 'ENTER' key
	 * to terminate the Akka application.
	 *
	 * @param args not used
	 * @throws InterruptedException may be thrown if main thread is interrupted
	 *         unexpectedly
	 */
	public static void main(String[] args) throws InterruptedException {

		final ActorSystem<PingPongRoot.InitMessage> system = ActorSystem.create(PingPongRoot.create(), "system");
		system.tell(new PingPongRoot.createPingPong());

		try {
			System.out.println(">>> Press ENTER to exit <<<");
			System.in.read();
		} catch (IOException ignored) {
		} finally {
			system.terminate();
		}
	}
}
