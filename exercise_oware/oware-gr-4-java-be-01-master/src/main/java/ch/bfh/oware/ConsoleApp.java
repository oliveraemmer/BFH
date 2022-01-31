/*
 * Project and Training 1: Oware Game - Computer Science, Berner Fachhochschule
 */

/**
 * This Class starts the OWARE game in the console.
 * Right at the beginning the user can define the
 * game size and choose if he wants to play against a
 * bot or against another player who is using the same console.
 * 
 * The game ends when only two seeds are left in the game.
 * 
 * The bot chooses non empty pits.
 */
package ch.bfh.oware;

import ch.bfh.oware.model.Board;
import ch.bfh.oware.model.MyBoardClass;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsoleApp {
	
	public static void main(String[] args) {
		
		// define random number for bot
		Random rand = new Random();
		
		// start of the game
		Scanner input = new Scanner(System.in);
		System.out.println("How many pits?");
		int size = input.nextInt();
		System.out.println("How many seeds?");
		int seeds = input.nextInt();
		System.out.println("How many players? (1 or 2)");
		int playerCount = input.nextInt();
		
		MyBoardClass board = new MyBoardClass(size, seeds);
		System.out.println("Game starts:");
		System.out.println("Player A: " + board.getRow(1) + "\nPlayer B: " + board.getRow(2));
		System.out.println(board.getScore(1) + "/" + board.getScore(2) + "\n\n");
		
		// player 1 = player A
		// player 2 = player B (bot if singleplayer)
		int player = 1;
		
		// multiplayer
		while (board.gameOver() == false) {
			if (player == 1) {
				System.out.println("Player A: moves = " + board.getMoves(player));
				System.out.println("Player A, choose a pit:");
				int pit = input.nextInt();
				// Execute the play
				board.play(1, pit);
				System.out.println("Player A: " + board.getRow(1) + "\nPlayer B: " + board.getRow(2));
				System.out.println(board.getScore(1) + "/" + board.getScore(2) + "\n\n");
				// Change to other player
				player++;
			}
			
			if (player == 2) {
				if (playerCount == 2) {
					System.out.println("Player B: moves = " + board.getMoves(player));
					System.out.println("Player B, choose a pit:");
					int pit = input.nextInt();
					// Execute the play
					board.play(2, pit);
					System.out.println("Player B: " + board.getRow(1) + "\nPlayer B: " + board.getRow(2));
					System.out.println(board.getScore(1) + "/" + board.getScore(2) + "\n\n");
				}
				
				if (playerCount == 1) {
					if (board.hasMoves(2)) {
						System.out.println("Bot: moves = " + board.getMoves(player));
						// Create Array for the possible moves
						List<Integer> possibleMoves = new ArrayList<Integer>();
						possibleMoves = board.getMoves(2);
						// Get random number that isn't bigger than the amount of possible moves
						int pit = rand.nextInt(board.getMoves(2).size());
						System.out.println("Bot plays " + possibleMoves.get(pit));
						// Execute the play with random number from list of possibleMoves
						board.play(2, possibleMoves.get(pit));
						System.out.println("Player A: " + board.getRow(1) + "\nPlayer B: " + board.getRow(2));
						System.out.println(board.getScore(1) + "/" + board.getScore(2) + "\n\n");
					} else {
						System.out.println("Bot has no moves");
					}
				}
				// Change to other player
				player--;
			}
		}
	}
}
