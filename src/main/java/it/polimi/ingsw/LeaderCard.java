package it.polimi.ingsw;

import it.polimi.ingsw.abilities.AbilityEffectActivation;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents all the Leader cards with their requirements and use a strategy pattern for the activation of the ability
 */
public class LeaderCard {

	private int victoryPoints;
	private ArrayList<Resources> resourceRequirements; // list of resources required for the activation
	private ArrayList<CardRequirement> cardRequirements; // list of development card requirements
	private ArrayList<AbilityEffectActivation> effectsActivation; // a single leader card supports multiple abilities
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
		ArrayList<Colors> cardColors = new ArrayList<>();
		ArrayList<CardRequirement> support1= new ArrayList<>();
		ArrayList<CardRequirement> support2= new ArrayList<>();
		ArrayList<Colors> support3= new ArrayList<>();
		ArrayList<Colors> colorsRequirements;
		/*if(cardRequirements.isEmpty())
			return true;
		else if (cardRequirements.stream().noneMatch(i->i.getLevel()==0))
			return ListSet.subset(cardRequirements,totalCardOwned);
		else if (cardRequirements.stream().noneMatch(i->i.getLevel()>0)){
			cardColors=totalCardOwned.stream().map(CardRequirement::getColor).collect(Collectors.toCollection(ArrayList::new));
			colorsRequirements=cardRequirements.stream().map(CardRequirement::getColor).collect(Collectors.toCollection(ArrayList::new));
			return ListSet.subset(colorsRequirements,cardColors);
		}
		else{*/
			support1=cardRequirements.stream().filter(i->i.getLevel()>0).collect(Collectors.toCollection(ArrayList::new));
			support2=totalCardOwned;
			if (!ListSet.subset(support2, support1))
				return false;
			//support2.removeAll(support1);
			support2 = ListSet.removeSubSet(support2, support1);
			cardColors=cardRequirements.stream().filter(i->i.getLevel()==0).map(CardRequirement::getColor).collect(Collectors.toCollection(ArrayList::new));
			support3=support2.stream().map(CardRequirement::getColor).collect(Collectors.toCollection(ArrayList::new));
			return ListSet.subset(support3, cardColors);

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
