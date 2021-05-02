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
	
	/**
	 *
	 * @param inputResources list of input resources for the production
	 * @param faithPointsOutput number of faith points gained after the production
	 * @param outputResources list of input resources gained from the production
	 */
	public ExtraProductionAbility(ArrayList<Resources> inputResources, int faithPointsOutput,
	                              ArrayList<Resources> outputResources) {
		this.inputResources = inputResources;
		this.faithPointsOutput = faithPointsOutput;
		this.outputResources = outputResources;
	}

	@Override
	public void activateAbility(Player player) {
		player.getPlayersCardManager().
				addLeaderCard(inputResources,outputResources,faithPointsOutput);
	}

	@Override
	public void appendPower(StringBuilder string) {
		string.append("  In : ").append(ListSet.listMultiplicityToString(inputResources)).append("\n");
		string.append("  Out: ").append(ListSet.listMultiplicityToString(outputResources)).append("\n");
		string.append("       "+faithPointsOutput+ Unicode.RED_BOLD+Unicode.CROSS2+Unicode.RESET+"\n");
	}

	@Override
	public int maxLength() {
		int inputSize= (int) (8+4*inputResources.stream().distinct().count());
		int outputSize=(int) (8+4*outputResources.stream().distinct().count());
		return Math.max(inputSize, outputSize);
	}

	public ArrayList<Resources> getInputResources() {
		return inputResources;
	}

	public int getFaithPointsOutput() {
		return faithPointsOutput;
	}

	public ArrayList<Resources> getOutputResources() {
		return outputResources;
	}
	
	/**
	 * description of the input and output parameters of the extra production ability
	 * @return a list of input and output resources, and the faith points
	 */
	@Override
	public String toString() {
		return "Extra Production Ability : {" +	"inputResources = " + inputResources.toString() +
				", faith Points in output = " + faithPointsOutput +
				", outputResources = " + outputResources.toString() + '}';
	}
}
