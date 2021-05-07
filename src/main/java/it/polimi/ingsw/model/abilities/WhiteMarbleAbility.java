package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;

/**
 * This class activates the ability that transforms white marbles in new resources
 */
public class WhiteMarbleAbility implements AbilityEffectActivation {

	private final ArrayList<Resources> abilityResources;
	private final int whiteMarbleNumber;
	
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


	@Override
	public int maxLength() {
		int size=(int)(5+3*whiteMarbleNumber+4*abilityResources.stream().distinct().count());
		return Math.max(size, 23);
	}


	/**
	 * description of the ability with input white marbles, and output = list of resources
	 * @return description of the input and output
	 */
	@Override
	public String toString() {

		return "  White Marble Ability:\n  " +
				"\uD83D\uDFE3 ".repeat(Math.max(0, whiteMarbleNumber)) +
				"= " +
				ListSet.listMultiplicityToString(abilityResources) + "\n";
	}
}
