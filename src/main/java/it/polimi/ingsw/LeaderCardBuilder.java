package it.polimi.ingsw;

import it.polimi.ingsw.util.Resources;
import it.polimi.ingsw.abilities.*;

import java.util.ArrayList;

/**
 * Builder Design pattern for the construction of the Leader Card object, with the parameters read from the XML config file
 */
public class LeaderCardBuilder {
	
	private int victoryPoints;
	private ArrayList<Resources> resourceRequirements;
	private ArrayList<CardRequirement> cardRequirements;
	private ArrayList<AbilityEffectActivation> effects;
	
	/**
	 * basic constructor
	 */
	public LeaderCardBuilder() {
		effects = new ArrayList<>();
	}
	
	/**
	 * first function to be called when building the Leader Card. Assigns the standard objects that are common for every leader card
	 * @param victoryPoints number of victory points
	 * @param requirements list of resources requirements
	 * @param cardRequirements list of dev card requirements
	 * @return the instance of this Builder
	 */
	public LeaderCardBuilder xmlData(int victoryPoints, ArrayList<Resources> requirements, ArrayList<CardRequirement> cardRequirements) {
		this.victoryPoints = victoryPoints;
		this.resourceRequirements = requirements;
		this.cardRequirements = cardRequirements;
		return this;
	}
	
	/**
	 * discount ability or additional depot ability
	 * @param ability the string identifying the type of ability
	 * @param list of resources
	 * @return builder object
	 */
	public LeaderCardBuilder xmlData(String ability, ArrayList<Resources> list) {
		if (ability.equals("discount")) {
			effects.add(new DiscountAbility(list));
		} else if (ability.equals("depot")) {
			effects.add(new AdditionalDepotAbility(list));
		}
		return this;
	}
	
	/**
	 * additional production ability constructor
	 * @param inputList input resources
	 * @param faithOutput number of faith points
	 * @param outputList output resources
	 * @return builder object
	 */
	public LeaderCardBuilder xmlData(ArrayList<Resources> inputList, int faithOutput, ArrayList<Resources> outputList) {
		effects.add(new ExtraProductionAbility(inputList, faithOutput, outputList));
		return this;
	}
	
	/**
	 * white marble ability constructor
	 * @param list of resources to transform
	 * @param whiteMarbles the number of marbles to use
	 * @return the builder object
	 */
	public LeaderCardBuilder xmlData(ArrayList<Resources> list, int whiteMarbles) {
		effects.add(new WhiteMarbleAbility(list, whiteMarbles));
		return this;
	}
	
	/**
	 * lazy production of the leader card with the xml data. Also checks whether the leader card has at least one ability defined
	 * @return the leader card
	 */
	public LeaderCard build() {
		assert !effects.isEmpty();
		return new LeaderCard(victoryPoints, resourceRequirements, cardRequirements, effects);
	}
	
}
