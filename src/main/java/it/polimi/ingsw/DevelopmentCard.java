package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * this class handles single card management
 */
public class DevelopmentCard {
	
	private final int level,victoryPoints;
	private final Colors color;
	private final ArrayList<Resources> resourcesRequirement;
	private final ProductionRules productionRules;
	
	/**
	 * constructor for instantiating card's attributes
	 * @param level is the level of the card
	 * @param color is the color of the card
	 * @param victoryPoints is the number of card's victory points
	 * @param resourceList is a list of needed resources to buy the card
	 * @param productionRules is the instance of the corresponding production rules
	 */
	
	public DevelopmentCard(int level, Colors color, int victoryPoints, ArrayList<Resources> resourceList, ProductionRules productionRules) {
		this.level = level;
		this.color = color;
		this.victoryPoints = victoryPoints;
		this.resourcesRequirement = resourceList;
		this.productionRules = productionRules;
	}
	
	/**
	 * getter for the attribute level
	 * @return card's level
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * getter for the attribute victoryPoints
	 * @return the number of Victory points
	 */
	public int getVictoryPoints(){
		return victoryPoints;
	}

	/**
	 * too lazy to write something here
	 * @return //
	 */
	public ArrayList<Resources> getResourcesRequirement(){
		return resourcesRequirement;
	}
	
	/**
	 * getter for card's color
	 * @return the color of the card
	 */
	public Colors getColor(){
		return color;
	}

	public void showCard(){
		/*
		String colorCode;
		String resetWhite = "\033[0m";
		switch(this.color){
			case GREEN -> colorCode = "\u001B[32m";
			case BLUE -> colorCode = "\u001B[34m";
			case YELLOW -> colorCode = "\u001B[33m";
			case PURPLE -> colorCode = "\u001B[35m";
			default -> throw new IllegalStateException("Unexpected value: " + this.color);
		}

		 */
		System.out.print("Resources requirements: ");
		ListSet.showListMultiplicityOnConsole(getResourcesRequirement());
		System.out.println("Card: level " + getLevel() + " and color " + getColor());
		productionRules.showProductionRulesInformation();
		System.out.println("Card victory points: "+ getVictoryPoints() + "\n");
	}
	// "\033[0m"; white
	/*
	PURPLE("\u001B[35m"), //purple color
	BLUE("\u001B[34m"), //blue color
	YELLOW("\u001B[33m"); //yellow color
	GREEN("\U001B[32M");
	 */

	public ProductionRules getProductionRules() {
		return productionRules;
	}
}
