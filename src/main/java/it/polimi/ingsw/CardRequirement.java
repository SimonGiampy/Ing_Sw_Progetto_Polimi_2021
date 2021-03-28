package it.polimi.ingsw;

/**
 * This class represent the requirement of a single card for the activation of a leader card
 */
public class CardRequirement {

	private final Colors color;
	private final int level;
	
	/**
	 * Constructs the representation of the requirement of a card
	 * @param color required card color
	 * @param level required card level
	 */
	public CardRequirement(Colors color, int level) {
		this.color = color;
		this.level = level;
	}

	protected int getLevel(){
		return level;
	}
	
	protected Colors getColor(){
		return color;
	}
}
