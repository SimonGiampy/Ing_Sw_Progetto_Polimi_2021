package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * class implementation of strategy interface, handles the leader cards that provides additional deposits for
 * specific resources
 */
public class AdditionalDepotAbility implements AbilityEffectActivation{

	private final ArrayList<Resources> abilityResource;

	/**
	 * Constructor that set the attributes
	 * @param abilityResource is the array of the resources that can be store in the additional depot
	 */
	protected AdditionalDepotAbility(ArrayList<Resources> abilityResource) {
		this.abilityResource = abilityResource;
	}

	@Override
	public void activateAbility(Player player) {
		player.getPlayersWarehouseDepot().enableAdditionalDepot(abilityResource);
	}
}
