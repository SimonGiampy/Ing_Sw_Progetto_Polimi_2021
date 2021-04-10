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
	
	/**
	 * override of toString method for debugging purposes
	 * @return a string representing the contents of this object
	 */
	@Override
	public String toString() {
		String support;
		if (level==0)
			support="any";
		else support= String.valueOf(level);
		return color.getColorCode()+"█"+Unicode.RESET+" LVL = "+support;
	}
	
	
	/**
	 * checks if two CardRequirement objects are equal, by looking at their color and level
	 * @param obj the CardRequirement object to which this object is being confronted to
	 * @return true if the 2 CardRequirements are equal
	 */
	@Override
	public boolean equals(Object obj) {
		CardRequirement comparison = (CardRequirement) obj;
		return this.getColor().getColorNumber() == comparison.getColor().getColorNumber() && this.getLevel() == comparison.getLevel();
	}
	
	/**
	 * generates a unique hashcode for the CardRequirement object
	 * @return the integer representing the object
	 */
	@Override
	public int hashCode() {
		int n = this.getColor().getColorNumber() * 100;
		return n + this.getLevel();
	}
}
