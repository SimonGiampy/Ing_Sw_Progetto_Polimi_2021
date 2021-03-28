package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * Ability that enables the extra production. It takes a number of resources in input
 */
public class ExtraProductionAbility implements AbilityEffectActivation {

	private ArrayList<Resources> inputResources; // list of input resources for the production
	private int faithPointsOutput; //number of faith points gained after the production
	private int freeChoicesResources; // number of resources to gain in output, that can be chosen by the user
	private CardManagement cardManager; // instance of card management, needed for communication
	
	/**
	 *
	 * @param inputResources list of input resources for the production
	 * @param faithPointsOutput number of faith points gained after the production
	 * @param freeChoicesResources number of resources to gain in output, that can be chosen by the user
	 * @param cardManager instance of card management, needed for communication
	 */
	public ExtraProductionAbility(ArrayList<Resources> inputResources, int faithPointsOutput, int freeChoicesResources, CardManagement cardManager) {
		this.inputResources = inputResources;
		this.faithPointsOutput = faithPointsOutput;
		this.freeChoicesResources = freeChoicesResources;
		this.cardManager = cardManager;
	}

	@Override
	public void activateAbility() {

	}
}
