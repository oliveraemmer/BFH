/*
 * Project and Training 1: Oware Game - Computer Science, Berner Fachhochschule
 */
package ch.bfh.oware.model;

import java.util.List;

/**
 * This interface represent the Oware game board, which consists of two rows of
 * pits and a given amount of seeds. Newly created instances implementing this
 * interface represent the initial state of the Oware game, in which the seeds
 * are equally distributed over all pits (usually 4 seeds in each pit). The
 * number of pits in each row is called <i>size</i> (usually 6). If seeds
 */
public interface Board {

	/**
	 * Returns the size of the Oware game board (number of pits in each row).
	 *
	 * @return The size of the board
	 */
	int getSize();

	/**
	 * Returns the initial number of seeds in each pit of the Oware game board at
	 * the beginning of a new game.
	 *
	 * @return The initial number of seeds
	 */
	int getInitialSeeds();

	/**
	 * Returns the current number of seeds at the specified pit (numbered from 1 to
	 * size) and row (numbered from 1 to 2) of the Oware game board.
	 *
	 * @param row The row number
	 * @param pit The pit number
	 * @return The current number of seeds
	 */
	int countSeeeds(int row, int pit);

	/**
	 * Checks if there is a legal move in the specified row (numbered from 1 to 2)
	 * of the Oware game board.
	 *
	 * @param row The row number
	 * @return True, if there is a legal move, false otherwise
	 */
	boolean hasMoves(int row);

	/**
	 * Returns the list of legal moves in the specified row (numbered from 1 to 2)
	 * of the Oware game board. If no moves are available, this list is empty.
	 *
	 * @param row The row number
	 * @return The list of legal moves
	 */
	List<Integer> getMoves(int row);

	/**
	 * Conducts a move at the specified pit (numbered from 1 to size) and row
	 * (numbered from 1 to 2) according to the Oware game rules.
	 *
	 * @param row The row number
	 * @param pit The pit number
	 */
	void play(int row, int pit);

	/**
	 * Returns the current score (number of collected seeds) of the specified row
	 * (numbered from 1 to 2).
	 *
	 * @param row The row number
	 * @return The current score
	 */
	int getScore(int row);

	/**
	 * Checks if the game is over. This is the case if only two seeds remain on the
	 * Oware game board.
	 *
	 * @return True iff the game is over, false otherwise
	 */
	boolean gameOver();

}
