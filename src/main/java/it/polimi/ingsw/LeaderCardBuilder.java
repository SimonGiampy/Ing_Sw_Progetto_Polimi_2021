package it.polimi.ingsw;

import it.polimi.ingsw.abilities.*;

import java.util.ArrayList;

public class LeaderCardBuilder {
	
	private int victoryPoints;
	private ArrayList<Resources> resourceRequirements;
	private ArrayList<CardRequirement> cardRequirements;
	private AbilityEffectActivation effectActivation;
	
	public LeaderCardBuilder() {
	
	}
	
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
			effectActivation = new DiscountAbility(list);
		} else if (ability.equals("depot")) {
			effectActivation = new AdditionalDepotAbility(list);
		} else throw new IllegalArgumentException("ability is not correctly defined");
		return null;
	}
	
	/**
	 * additional production ability constructor
	 * @param inputList input resources
	 * @param faithOutput number of faith points
	 * @param outputList output resources
	 * @return builder object
	 */
	public LeaderCardBuilder xmlData(ArrayList<Resources> inputList, int faithOutput, ArrayList<Resources> outputList) {
		effectActivation = new ExtraProductionAbility(inputList, faithOutput, outputList);
		return this;
	}
	
	/**
	 * white marble ability constructor
	 * @param list of resources to transform
	 * @param whiteMarbles the number of marbles to use
	 * @return the builder object
	 */
	public LeaderCardBuilder xmlData(ArrayList<Resources> list, int whiteMarbles) {
		effectActivation = new WhiteMarbleAbility(list, whiteMarbles);
		return this;
	}
	
	/**
	 * lazy production of the leader card with the xml data
	 * @return the leader card
	 */
	public LeaderCard build() {
		assert effectActivation != null;
		return new LeaderCard(victoryPoints, resourceRequirements, cardRequirements, effectActivation);
	}
	
}
