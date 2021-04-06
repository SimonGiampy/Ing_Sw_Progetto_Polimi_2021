package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.stream.IntStream;

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
	
	/**
	 * it recall production rules function to get the output of the production
	 * @return an arraylist of resources that contains production's output.
	 */
	public ArrayList<Resources> produce(){
		return productionRules.produce();
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
		showProductionRulesInformation();
		System.out.println("Card victory points: "+ getVictoryPoints() + "\n");
	}
	// "\033[0m"; white
	/*
	PURPLE("\u001B[35m"), //purple color
	BLUE("\u001B[34m"), //blue color
	YELLOW("\u001B[33m"); //yellow color
	GREEN("\U001B[32M");
	 */

	/**
	 * it checks if card's production is available
	 * @param input is a list of all the resources the player owns
	 * @return true if the the production is available
	 */
	public boolean isCardProductionAvailable(ArrayList<Resources> input){
		return productionRules.isProductionAvailable(input);
	}


	/**
	 * getter for card's production faith points
	 * @return the number of faith points
	 */
	public int returnFaithPoints(){
		return productionRules.getFaithOutput();
	}

	public void showProductionRulesInformation(){
		productionRules.showProductionRulesInformation();
	}

	/**
	 * retrieves a copy of the production input resources
	 * @return a copy of the production
	 */
	public ArrayList<Resources> getProductionInput(){
		 return productionRules.getInputCopy();
	}
	public int numberOfOutputEmptyResources(){
		return productionRules.numberOfOutputEmptyResources();
	}
	public int numberOfInputEmptyResources(){
		return productionRules.numberOfInputEmptyResources();
	}



}
