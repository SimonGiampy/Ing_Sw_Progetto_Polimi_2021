package it.polimi.ingsw.abilities;

import it.polimi.ingsw.ListSet;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Resources;

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
	public AdditionalDepotAbility(ArrayList<Resources> abilityResource) {
		this.abilityResource = abilityResource;
	}

	@Override
	public void activateAbility(Player player) {
		player.getPlayersWarehouseDepot().enableAdditionalDepot(abilityResource);
	}

	public ArrayList<Resources> getAbilityResource() {
		return abilityResource;
	}
	
	/**
	 * description of ability properties and contents
	 * @return the contents of the arraylist with the resources
	 */
	@Override
	public String toString() {
		return "Additional Depot: resources = " + abilityResource.toString();
	}

	@Override
	public void appendPower(StringBuilder string){
		string.append("  Additional Depot:\n");
		string.append("  "+ListSet.showListMultiplicityOnConsole(abilityResource)+"\n");
	}

	@Override
	public int maxLength() {
		int size= (int) (3+4*abilityResource.stream().distinct().count());
		if(size>19)
			return size;
		else return 19;
	}


}
