package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.model.util.Unicode;

import java.util.ArrayList;

/**
 * Ability that enables the extra production. It takes a number of resources in input
 */
public class ExtraProductionAbility implements AbilityEffectActivation {

	private final ArrayList<Resources> inputResources; // list of input resources for the production
	private final int faithPointsOutput; //number of faith points gained after the production
	private final ArrayList<Resources> outputResources;
	
	private final int cardId;
	/**
	 *
	 * @param inputResources list of input resources for the production
	 * @param faithPointsOutput number of faith points gained after the production
	 * @param outputResources list of input resources gained from the production
	 */
	public ExtraProductionAbility(int cardId, ArrayList<Resources> inputResources, int faithPointsOutput,
	                              ArrayList<Resources> outputResources) {
		this.inputResources = inputResources;
		this.faithPointsOutput = faithPointsOutput;
		this.outputResources = outputResources;
		this.cardId = cardId;
	}

	@Override
	public void activateAbility(Player player) {
		int leaderNumber = player.getExtraProdLeader(this.cardId);
		player.getPlayersCardManager().addLeaderCard(leaderNumber, inputResources, outputResources, faithPointsOutput);
	}

	@Override
	public int maxLength() {
		int inputSize= (int) (8+4*inputResources.stream().distinct().count());
		int outputSize=(int) (8+4*outputResources.stream().distinct().count());
		return Math.max(inputSize, outputSize);
	}

	/**
	 * description of the input and output parameters of the extra production ability
	 * @return a list of input and output resources, and the faith points
	 */
	@Override
	public String toString() {
		return "  In : " + ListSet.listMultiplicityToString(inputResources) + "\n" +
				"  Out: " + ListSet.listMultiplicityToString(outputResources) + "\n" +
				"       " + faithPointsOutput + Unicode.RED_BOLD + Unicode.CROSS2 + Unicode.RESET + "\n";
	}
}
