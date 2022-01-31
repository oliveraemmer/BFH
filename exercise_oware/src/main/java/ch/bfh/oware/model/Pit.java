package ch.bfh.oware.model;

public class Pit {
	public int seeds;
	public Players player;
	
	public Pit(int seeds, Player player) {
		this.seeds = seeds;
		this.player = player;
	}
	
	public int getSeeds() {
		return seeds;
	}
	
	public Players getPlayer() {
		return player;
	}
}
