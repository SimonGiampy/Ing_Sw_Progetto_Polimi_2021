package it.polimi.ingsw;

import it.polimi.ingsw.abilities.AbilityEffectActivation;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents all the Leader cards with their requirements and use a strategy pattern for the activation of the ability
 */
public class LeaderCard {

	private final int victoryPoints;
	private final ArrayList<Resources> resourceRequirements; // list of resources required for the activation
	private final ArrayList<CardRequirement> cardRequirements; // list of development card requirements
	private final ArrayList<AbilityEffectActivation> effectsActivation; // a single leader card supports multiple abilities
	private boolean abilitiesActivated;
	
	
	/**
	 * Constructor that sets the attributes to their values based on the parsed information
	 * @param victoryPoints number of victory points of the card
	 * @param requirements arrayList of the resources needed to play the card
	 * @param cardRequirements arrayList of the dev card required
	 * @param effectsActivation instance of the ability
	 */
	public LeaderCard(int victoryPoints, ArrayList<Resources> requirements, ArrayList<CardRequirement> cardRequirements,
	                  ArrayList<AbilityEffectActivation> effectsActivation) {
		this.victoryPoints = victoryPoints;
		this.resourceRequirements = requirements;
		this.cardRequirements = cardRequirements;
		this.effectsActivation = effectsActivation;
		abilitiesActivated = false;
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
			return ListSet.subset(totalPlayerResources, resourceRequirements);
	}

	/**
	 * Checks if the player satisfies the dev card requirements of the leader card
	 * @param totalCardOwned ArrayList of colors and levels of dev cards owned by the player
	 * @return true if requirements are satisfied, false otherwise
	 */
	protected boolean checkCards(ArrayList<CardRequirement> totalCardOwned) {
		ArrayList<Colors> leaderCardColorsRequirement;
		ArrayList<CardRequirement> leaderCardRequirements;
		ArrayList<CardRequirement> remainingCards;
		ArrayList<Colors> remainingCardsColor;
		leaderCardRequirements=cardRequirements.stream().filter(i->i.getLevel()>0).collect(Collectors.toCollection(ArrayList::new));
		if (!ListSet.subset(totalCardOwned,leaderCardRequirements))
			return false;
		remainingCards = ListSet.removeSubSet(totalCardOwned,leaderCardRequirements);
		leaderCardColorsRequirement=cardRequirements.stream().filter(i->i.getLevel()==0).map(CardRequirement::getColor).collect(Collectors.toCollection(ArrayList::new));
		remainingCardsColor=remainingCards.stream().map(CardRequirement::getColor).collect(Collectors.toCollection(ArrayList::new));
		return ListSet.subset(remainingCardsColor, leaderCardColorsRequirement);
	}
	
	
	/**
	 * activates ability with the Strategy design pattern
	 * @param player the player who is asking to activate its ability
	 */
	protected void activateAbility(Player player) {
		for (AbilityEffectActivation effect: effectsActivation) {
			effect.activateAbility(player);
		}
		abilitiesActivated = true;
	}
	
	/**
	 * victory points earned from a leader card
	 * @return the value when the game is finished
	 */
	protected int getVictoryPoints() {
		if (abilitiesActivated) return victoryPoints;
		else return 0;
	}
	
	public ArrayList<Resources> getResourceRequirements() {
		return resourceRequirements;
	}
	
	public ArrayList<CardRequirement> getCardRequirements() {
		return cardRequirements;
	}
	
	public AbilityEffectActivation getEffectActivation(int whichAbility) {
		return effectsActivation.get(whichAbility);
	}
	
	public ArrayList<AbilityEffectActivation> getEffectsActivation() {
		return effectsActivation;
	}
}
