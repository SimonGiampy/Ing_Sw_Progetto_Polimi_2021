package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.Resources;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class handles single card management
 */
public class DevelopmentCard implements Serializable {
	
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
	 * getter for resources requirements
	 * @return resources requirements
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

	public ProductionRules getProductionRules() {
		return productionRules;
	}

}
