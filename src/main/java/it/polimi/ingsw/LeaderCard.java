package it.polimi.ingsw;

import it.polimi.ingsw.abilities.AbilityEffectActivation;

import java.util.ArrayList;

/**
 * Represents all the Leader cards with their requirements and use a strategy pattern for the activation of the ability
 */
public class LeaderCard {

	private int victoryPoints;
	private ArrayList<Resources> resourceRequirements;
	private ArrayList<CardRequirement> cardRequirements;
	private AbilityEffectActivation effectActivation;
	private boolean abilityActivated;


	/**
	 * Constructor that sets the attributes to their values based on the parsed information
	 * @param victoryPoints number of victory points of the card
	 * @param requirements arrayList of the resources needed to play the card
	 * @param cardRequirements arrayList of the dev card required
	 * @param effectActivation instance of the ability
	 */
	public LeaderCard(int victoryPoints, ArrayList<Resources> requirements, ArrayList<CardRequirement> cardRequirements,
					  AbilityEffectActivation effectActivation) {
		this.victoryPoints = victoryPoints;
		this.resourceRequirements = requirements;
		this.cardRequirements = cardRequirements;
		this.effectActivation = effectActivation;
		abilityActivated = false;
	}

	/**
	 * Checks if the player satisfies the resource requirements of the leader card
	 * @param totalPlayerResources ArrayList of the total resources owned by the player
	 * @return true if the player can play the card, false otherwise
	 */
	protected boolean checkResources(ArrayList<Resources> totalPlayerResources) {
		if(resourceRequirements.isEmpty())
			return true;
		else
			return totalPlayerResources.containsAll(resourceRequirements);
	}

	/**
	 * Checks if the player satisfies the dev card requirements of the leader card
	 * @param totalCardOwned ArrayList of colors and levels of dev cards owned by the player
	 * @return true if requirements are satisfied, false otherwise
	 */
	protected boolean checkCards(ArrayList<CardRequirement> totalCardOwned) {
		if(cardRequirements.isEmpty())
			return true;
		else
			return totalCardOwned.containsAll(cardRequirements);
	}
	
	
	/**
	 * activates ability with the Strategy design pattern
	 * @param player the player who is asking to activate its ability
	 */
	protected void activateAbility(Player player) {
		effectActivation.activateAbility(player);
		abilityActivated = true;
	}
	
	/**
	 * victory points earned from a leader card
	 * @return the value when the game is finished
	 */
	protected int getVictoryPoints() {
		if (abilityActivated) return victoryPoints;
		else return 0;
	}
}
