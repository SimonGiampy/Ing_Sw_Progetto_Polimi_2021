package it.polimi.ingsw;

public class Tile {
	
	private final int victoryPoints;
	
	//These insideVatican attributes are true not only for the tiles that actually are in the correspondent Vatican Zone
	//but also for all the tiles after them
	private final boolean insideVatican1, insideVatican2, insideVatican3;
	
	//True only in tiles 8, 16 and 24
	private final boolean papalSpace;
	
	public Tile(int victoryPoints, boolean insideVatican1, boolean insideVatican2, boolean insideVatican3, boolean papalSpace) {
		this.victoryPoints = victoryPoints;
		this.insideVatican1 = insideVatican1;
		this.insideVatican2 = insideVatican2;
		this.insideVatican3 = insideVatican3;
		this.papalSpace = papalSpace;
	}
	
	public boolean isInsideVatican1() {
		return insideVatican1;
	}
	
	public boolean isInsideVatican2() {
		return insideVatican2;
	}
	
	public boolean isInsideVatican3() {
		return insideVatican3;
	}
	
	public boolean isPapalSpace() {
		return papalSpace;
	}
	
	public int tilePoints(){
		return victoryPoints;
	}
}
