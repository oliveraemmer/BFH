/*
 * Project and Training 1: Oware Game - Computer Science, Berner Fachhochschule
 */
package ch.bfh.oware.model;

import java.util.List;
import java.util.ArrayList;

// please change the name of this class according to your specific implementation
public class MyBoardClass implements Board {

	public int size;
	public int seeds;
	public int scoreA = 0;
	public int scoreB = 0;
	public ArrayList<Integer> row1 = new ArrayList<Integer>();
	public ArrayList<Integer> row2 = new ArrayList<Integer>();
	
	public MyBoardClass(int size, int seeds) {
		this.size = size;
		this.seeds = seeds;
		for (int i = 0; i < size; i++) {
			row1.add(seeds);
			row2.add(seeds);
		}
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public int getInitialSeeds() {
		return this.seeds;
	}
	
	@Override
	public int countSeeeds(int row, int pit) {
		if(row == 1) {
			return row1.get(pit-1);
		}
		
		if(row == 2) {
			// Game was made with numbers going from 1 - 6 for both players so this has to be changed for player 2
			int c = this.size - pit;
			return row2.get(c);
		}
		
		return 0;
	}
	
	@Override
	public boolean hasMoves(int row) {
		for (int i = 0; i < size; i++) {
			if(row == 1) {
				if (row1.get(i) != 0) {
					return true;
				}
			}
			
			if (row == 2) {
				if (row2.get(i) != 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<Integer> getMoves(int row) {
		// Go through row and check if there is more than 0 seeds per pit. If there is more, add it to the list
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			if (row == 1) {
				if(row1.get(i) != 0) {
					list.add(i+1);
				}
			}
		}
		for (int i = size - 1; 0 <= i; i--) {
			if (row == 2) {
				// Game was made with numbers going from 1 - 6 for both players so this has to be changed for player 2
				// Console show -> Player B: moves = [6, 5, 4, 3, 2, 1]
				// Actual values -> Player B: moves = [1, 2, 3, 4, 5, 6]
				i++;
				int c = size - i;
				i--;
				if(row2.get(c) != 0) {
					list.add(i + 1);
				}
			}
		}
		return list;
	}
	
	@Override
	public void play(int row, int currentPit) {
		int movesLeft;
		boolean firstCall = true;
		currentPit--;
		
		if(row == 1) {
			// Emtpy the pit that was chosen and add the seeds to movesLeft
			movesLeft = row1.get(currentPit);
			row1.set(currentPit, 0);
			row1(row, currentPit, movesLeft, firstCall);
		} else if (row == 2) {
			// Game was made with numbers going from 1 - 6 for both players so this has to be changed for player 2
			// Example:
			// size = 6
			// Player B plays 5
			// 5 turns into 2
			currentPit++;
			int c = this.getSize() - currentPit;
			currentPit = 1 + c;
			currentPit--;
			
			// Emtpy the pit that was chosen and add the seeds to movesLeft
			movesLeft = row2.get(currentPit);
			row2.set(currentPit, 0);
			row2(row, currentPit, movesLeft, firstCall);
		}
	}

	public int row1(int row, int currentPit, int movesLeft, boolean firstCall) {
		int size = this.getSize();
		// Do this only the first time the function gets called (play() -> row1())
		if (firstCall) {
			firstCall = false;
			// Immediately go to row2 if the last pit was chosen
			if (currentPit == size - 1) {
				row2(row, currentPit, movesLeft, firstCall);
				return 0;
			} else {
				// Otherwise go to next pit
				currentPit++;
			}
		}
		// Do this if function gets called by row2()
		if (!firstCall) {

		}
		// Go through the pits to the right of the pit that was chosen and add 1 seed
		for (int i = currentPit; i < size; i++) {
			row1.set(i, row1.get(i) + 1);
			// Check if pit now has 2 seeds. If yes, add those seeds to score of row2
			if (row1.get(i) == 2 && row == 2) {
				this.scoreB += 2;
				row1.set(i, 0);
			}
			movesLeft--;
			currentPit = i;
			// If theres no moves left to do stop adding seeds
			if(movesLeft < 1) {
				return 0;
			}
		}
		// If all the pits right to the pit that was chosen were added a seed and there are still moves left to do, go to row2()
		if(movesLeft > 0) {
			row2(row, currentPit, movesLeft, firstCall);
			return 0;
		}
		return 0;
	}
	
	
	public int row2(int row, int currentPit, int movesLeft, boolean firstCall) {
		int size = this.getSize();
		// Do this only the first time the function gets called (play() -> row2())
		if (firstCall) {
			firstCall = false;
			// Immediately go to row2 if the last pit was chosen
			if (currentPit == 0) {
				row1(row, currentPit, movesLeft, firstCall);
				return 0;
			} else {
				// Otherwise go to next pit
				currentPit--;
			}
		}
		// Do this if function gets called by row1()
		if (!firstCall) {
			
		}
		// Go through the pits to the left of the pit that was chosen and add 1 seed
		for (int i = currentPit; i < size && -1 < i; i--) {
			row2.set(i, row2.get(i) + 1);
			// Check if pit now has 2 seeds. If yes, add those seeds to score of row1
			if (row2.get(i) == 2 && row == 1) {
				this.scoreA += 2;
				row2.set(i, 0);
			}
			movesLeft--;
			currentPit = i;
			// If there's no moves left to do stop adding seeds
			if(movesLeft < 1) {
				return 0;
			}
		}
		// If all the pits left to the pit that was chosen were added a seed and there are still moves left to do, go to row2()
		if(movesLeft > 0) {
			row1(row, currentPit, movesLeft, firstCall);
			return 0;
		}
		return 0;
	}
	
	@Override
	public int getScore(int row) {
		if(row == 1) {
			return scoreA;
		}
		if(row == 2) {
			return scoreB;
		}
		return 0;
	}

	@Override
	public boolean gameOver() {
		int add = 0;
		for (int i = 0; i < size; i++) {
			add += row1.get(i);
			add += row2.get(i);
		}
		
		if (add > 2) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		// TODO Implement
		return "";
	}
	
	public List<Integer> getRow(int row) {
		if (row == 1) {
			return row1;
		}
		
		if(row == 2) {
			return row2;
		}
		ArrayList<Integer> empty = new ArrayList<Integer>();
		return empty;
	}

}
