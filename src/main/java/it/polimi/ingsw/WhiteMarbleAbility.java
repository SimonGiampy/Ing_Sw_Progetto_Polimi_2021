package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * This class activates the ability that transforms white marbles in new resources
 */
public class WhiteMarbleAbility implements AbilityEffectActivation {

	private ArrayList<Resources> abilityResources;
	private int whiteMarbleNumber;
	private ResourceDeck playerDeck;
	
	/**
	 * Constructor that takes the resources to gain from the white marbles
	 * @param abilityResources list of resource to convert into
	 * @param whiteMarbleNumber number of white marbles to transform at a time
	 * @param playerDeck the instance of the Resource Deck, where the white marbles are transformed
	 */
	public WhiteMarbleAbility(ArrayList<Resources> abilityResources, int whiteMarbleNumber, ResourceDeck playerDeck) {
		this.abilityResources = abilityResources;
		this.whiteMarbleNumber = whiteMarbleNumber;
		this.playerDeck = playerDeck;
	}

	@Override
	public void activateAbility() {

	}
}
