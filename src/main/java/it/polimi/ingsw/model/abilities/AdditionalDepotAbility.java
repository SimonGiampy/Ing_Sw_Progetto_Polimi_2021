package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;

/**
 * class implementation of strategy interface, handles the leader cards that provides additional deposits for
 * specific resources
 */
public class AdditionalDepotAbility implements AbilityEffectActivation {

	private final ArrayList<Resources> abilityResource;

	/**
	 * Constructor that set the attributes
	 * @param abilityResource is the array of the resources that can be store in the additional depot
	 */
	public AdditionalDepotAbility(ArrayList<Resources> abilityResource) {
		this.abilityResource = abilityResource;
	}

	@Override
	public void activateAbility(Player player) {
		player.getPlayersWarehouseDepot().enableAdditionalDepot(abilityResource);
	}
	
	/**
	 * description of ability properties and contents
	 * @return the contents of the arraylist with the resources
	 */
	@Override
	public String toString() {
		return "  Additional Depot:\n" +
				"  " + ListSet.listMultiplicityToString(abilityResource) + "\n";
	}


	@Override
	public int maxLength() {
		int size= (int) (3+4*abilityResource.stream().distinct().count());
		return Math.max(size, 19);
	}

}
