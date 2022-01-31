package ch.bfh.oware.model;

public abstract class Players {
	public String name;
	public boolean canPlay;
	
	public Players(String name) {
		this.name = name;
	}
	
	public boolean playPit(int pit){
		
		return false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean getState() {
		return this.canPlay;
	}
	
}
