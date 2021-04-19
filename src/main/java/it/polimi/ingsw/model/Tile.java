package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Tile {
	
	private final int victoryPoints;
	
	//These insideVatican attributes are true not only for the tiles that actually are in the correspondent Vatican Zone
	//but also for all the tiles after them
	private ArrayList<Boolean> insideVatican;

	private final boolean papalSpace;

	/**
	 * Constructor that assigns values to the attributes
	 * @param victoryPoints number of victory points
	 * @param insideVatican array of boolean that indicates whether a tile is inside a vatican zone or not
	 * @param papalSpace true if the tile is a papal space
	 */
	public Tile(int victoryPoints, ArrayList<Boolean> insideVatican, boolean papalSpace) {
		this.victoryPoints = victoryPoints;
		this.insideVatican = insideVatican;
		this.papalSpace = papalSpace;
	}
	
	public boolean isInsideVatican(int reportNumber) {
		return insideVatican.get(reportNumber);
	}
	
	public boolean isPapalSpace() {
		return papalSpace;
	}
	
	public int tilePoints(){
		return victoryPoints;
	}

	public ArrayList<Boolean> getInsideVatican() {
		return insideVatican;
	}
}
