package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * This class activates the ability that transforms white marbles in new resources
 */
public class WhiteMarbleAbility implements AbilityEffectActivation {

	private ArrayList<Resources> abilityResources;
	private int whiteMarbleNumber;
	
	/**
	 * Constructor that takes the resources to gain from the white marbles
	 * @param abilityResources list of resource to convert into
	 * @param whiteMarbleNumber number of white marbles to transform at a time
	 */
	public WhiteMarbleAbility(ArrayList<Resources> abilityResources, int whiteMarbleNumber) {
		this.abilityResources = abilityResources;
		this.whiteMarbleNumber = whiteMarbleNumber;
	}

	/**
	 * Sets the parameters in deck to activate the white marble ability
	 * @param player the player who is activating its ability
	 */
	@Override
	public void activateAbility(Player player) {
		player.getPlayersResourceDeck().setFromWhiteMarble(abilityResources, whiteMarbleNumber);
	}
}
