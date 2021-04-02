package it.polimi.ingsw.abilities;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Resources;
import it.polimi.ingsw.abilities.AbilityEffectActivation;

import java.util.ArrayList;

/**
 * Ability that enables the extra production. It takes a number of resources in input
 */
public class ExtraProductionAbility implements AbilityEffectActivation {

	private ArrayList<Resources> inputResources; // list of input resources for the production
	private int faithPointsOutput; //number of faith points gained after the production
	private ArrayList<Resources> outputResources;
	private int freeChoicesResources; // number of resources to gain in output, that can be chosen by the user
	
	/**
	 *
	 * @param inputResources list of input resources for the production
	 * @param faithPointsOutput number of faith points gained after the production
	 * @param freeChoicesResources number of resources to gain in output, that can be chosen by the user
	 * @param outputResources list of input resources gained from the production
	 */
	public ExtraProductionAbility(ArrayList<Resources> inputResources, int faithPointsOutput, int freeChoicesResources,
	                              ArrayList<Resources> outputResources) {
		this.inputResources = inputResources;
		this.faithPointsOutput = faithPointsOutput;
		this.freeChoicesResources = freeChoicesResources;
		this.outputResources = outputResources;
	}

	@Override
	public void activateAbility(Player player) {
		player.getPlayersCardManager().
				addLeaderCard(inputResources,outputResources,faithPointsOutput);
	}
}
