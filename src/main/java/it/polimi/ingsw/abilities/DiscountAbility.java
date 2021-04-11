package it.polimi.ingsw.abilities;

import it.polimi.ingsw.ListSet;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Resources;

import java.util.ArrayList;

/**
 * Class implementation of strategy interface, handles the leader cards that set a discount when buying
 * a development card
 */
public class DiscountAbility implements AbilityEffectActivation {

	private final ArrayList<Resources> singleDiscounts;

	/**
	 * Constructor that sets the attributes
	 * @param singleDiscounts array of resources that identifies what resource is discounted and how much it is
	 */
	public DiscountAbility(ArrayList<Resources> singleDiscounts) {
		this.singleDiscounts = singleDiscounts;
	}

	/**
	 * Set the parameters of the discounts in the Player instance
	 * @param player the player who wants to activate the discount
	 */
	@Override
	public void activateAbility(Player player) {
		player.setDiscount(singleDiscounts);
	}

	@Override
	public void appendPower(StringBuilder string) {
		string.append("  Discount Ability:\n");
		string.append("  "+ListSet.showListMultiplicityOnConsole(singleDiscounts)+"\n");
	}

	@Override
	public int maxLength() {
		int size= (int) (3+4*singleDiscounts.stream().distinct().count());
		if (size>19)
			return size;
		else return 19;
	}

	public ArrayList<Resources> getSingleDiscounts() {
		return singleDiscounts;
	}
	
	/**
	 * description of the discounts applied
	 * @return a list of resources representing single discounts
	 */
	@Override
	public String toString() {
		return "Discount Ability: discounts = " + singleDiscounts.toString();
	}
}
