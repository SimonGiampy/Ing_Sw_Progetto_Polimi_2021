package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * class implementation of strategy interface, handles the leader cards that provides additional deposits for
 * specific resources
 */
public class AdditionalDepotAbility implements AbilityEffectActivation{

	private final ArrayList<Resources> abilityResource;
	private final WarehouseDepot playerDepot;

	/**
	 * Constructor that set the attributes
	 * @param abilityResource is the array of the resources that can be store in the additional depot
	 * @param playerDepot the depot of the player that owns the corresponding leader card
	 */
	protected AdditionalDepotAbility(ArrayList<Resources> abilityResource, WarehouseDepot playerDepot) {
		this.abilityResource = abilityResource;
		this.playerDepot = playerDepot;
	}

	@Override
	public void activateAbility() {

	}
}
