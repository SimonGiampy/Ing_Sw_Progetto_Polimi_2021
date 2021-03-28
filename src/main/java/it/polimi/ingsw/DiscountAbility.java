package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * Class implementation of strategy interface, handles the leader cards that set a discount when buying
 * a development card
 */
public class DiscountAbility implements AbilityEffectActivation{

	private ArrayList<Resources> singleDiscounts;
	private CardManagement playerCardManagement;

	/**
	 * Constructor that sets the attributes
	 * @param singleDiscounts array of resources that identifies what resource is discounted and how much it is
	 * @param playerCardManagement CardManagement of the player who owns the leader card
	 */
	public DiscountAbility(ArrayList<Resources> singleDiscounts, CardManagement playerCardManagement) {
		this.singleDiscounts = singleDiscounts;
		this.playerCardManagement = playerCardManagement;
	}

	@Override
	public void activateAbility() {

	}
}
