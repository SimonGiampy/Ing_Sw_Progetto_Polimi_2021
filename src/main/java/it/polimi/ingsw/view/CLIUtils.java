package it.polimi.ingsw.view;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.abilities.AbilityEffectActivation;
import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.model.util.Unicode;

public class CLIUtils {

	public static void initialize(){
		System.out.println("""
				
				 ,ggg, ,ggg,_,ggg,                                                                                                        \s
				dP""Y8dP""Y88P""Y8b                          I8                                                     ,dPYb,                \s
				Yb, `88'  `88'  `88                          I8                                                     IP'`Yb                \s
				 `"  88    88    88                       88888888                                                  I8  8I                \s
				     88    88    88                          I8                                                     I8  8'                \s
				     88    88    88    ,gggg,gg    ,g,       I8     ,ggg,    ,gggggg,    ,g,             ,ggggg,    I8 dP                 \s
				     88    88    88   dP"  "Y8I   ,8'8,      I8    i8" "8i   dP""\""8I   ,8'8,           dP"  "Y8ggg I8dP                  \s
				     88    88    88  i8'    ,8I  ,8'  Yb    ,I8,   I8, ,8I  ,8'    8I  ,8'  Yb         i8'    ,8I   I8P                   \s
				     88    88    Y8,,d8,   ,d8b,,8'_   8)  ,d88b,  `YbadP' ,dP     Y8,,8'_   8)       ,d8,   ,d8'  ,d8b,_                 \s
				     88    88    `Y8P"Y8888P"`Y8P' "YY8P8P 8P""Y8 888P"Y8888P      `Y8P' "YY8P8P      P"Y8888P"    PI8"888                \s
				                                                                                                    I8 `8,                \s
				 ,ggggggggggg,                                                                                      I8  `8,               \s
				dP""\"88""\"""\"Y8,                                                                                    I8   8I               \s
				Yb,  88      `8b                                                                                    I8   8I               \s
				 `"  88      ,8P                                      gg                                            I8, ,8'               \s
				     88aaaad8P"                                       ""                                             "Y8P'                \s
				     88""\""Yb,     ,ggg,    ,ggg,,ggg,     ,gggg,gg   gg     ,g,       ,g,       ,gggg,gg   ,ggg,,ggg,     ,gggg,   ,ggg, \s
				     88     "8b   i8" "8i  ,8" "8P" "8,   dP"  "Y8I   88    ,8'8,     ,8'8,     dP"  "Y8I  ,8" "8P" "8,   dP"  "Yb i8" "8i\s
				     88      `8i  I8, ,8I  I8   8I   8I  i8'    ,8I   88   ,8'  Yb   ,8'  Yb   i8'    ,8I  I8   8I   8I  i8'       I8, ,8I\s
				     88       Yb, `YbadP' ,dP   8I   Yb,,d8,   ,d8b,_,88,_,8'_   8) ,8'_   8) ,d8,   ,d8b,,dP   8I   Yb,,d8,_    _ `YbadP'\s
				     88        Y8888P"Y8888P'   8I   `Y8P"Y8888P"`Y88P""Y8P' "YY8P8PP' "YY8P8PP"Y8888P"`Y88P'   8I   `Y8P""Y8888PP888P"Y888
				                                                                                                                          \s
				                                                                                                                          \s
				                                                                                                                          \s
				                                                                                                                          \s
				                                                                                                                          \s
				                                                                                                                          \s
				""");
	}

	/**
	 * helper function that tells the user how to interact with the depot
	 */
	public static void helpDepot() {
		System.out.println("Syntax for moving resources from the deck or depot to the depot is: " +
				"[move <position> from <deck/depot> to <destination>]");
		System.out.println("Syntax for moving a resource origin the warehouse to the deck is : [move <position> to deck]");
		System.out.println("The positional number in the warehouse is between 1 and 6: origin top to bottom, and origin left to right");
	}

	/**
	 * Support method for showFaithTrack, appends to the string builder the "-" lines that form the top of the track
	 * @param string string builder containing all the representation of the track
	 * @param faithTrack the reduced track
	 */
	protected static void appendTopFrame(StringBuilder string, ReducedFaithTrack faithTrack) {
		int j = 0;
		String s = Unicode.TOP_LEFT.toString();
		for(int i = 0; i < faithTrack.getTrack().size(); i++){
			if (j < faithTrack.getReportPoints().size() && faithTrack.getTrack().get(i).isInsideVatican(j)) {
				string.append(Unicode.ANSI_YELLOW);
				j++;
			}
			if(faithTrack.getTrack().get(i).isPapalSpace()){
				string.append(Unicode.ANSI_RED);
			}
			if(i > 0 && faithTrack.getTrack().get(i-1).isPapalSpace()){
				string.append(Unicode.T_SHAPE).append(Unicode.RESET).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
						.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL);
			}else {
				string.append(s).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
						.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL);
			}
			s = Unicode.T_SHAPE.toString();
		}
		string.append(Unicode.TOP_RIGHT).append("\n").append(Unicode.RESET);
	}

	/**
	 * Support method for showFaithTrack, appends to the string builder the "-" lines that form the bottom of the track
	 * @param string string builder containing all the representation of the track
	 * @param faithTrack the reduced track
	 */
	protected static void appendBottomFrame(StringBuilder string, ReducedFaithTrack faithTrack) {
		int j = 0;
		String s = Unicode.BOTTOM_LEFT.toString();
		for(int i = 0; i < faithTrack.getTrack().size(); i++){
			if (j < faithTrack.getReportPoints().size() && faithTrack.getTrack().get(i).isInsideVatican(j)) {
				string.append(Unicode.ANSI_YELLOW);
				j++;
			}
			if(faithTrack.getTrack().get(i).isPapalSpace()){
				string.append(Unicode.ANSI_RED);
			}
			if(i > 0 && faithTrack.getTrack().get(i-1).isPapalSpace()){
				string.append(Unicode.REVERSE_T_SHAPE).append(Unicode.RESET).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
						.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL);
			}else {
				string.append(s).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
						.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL);
			}
			s = Unicode.REVERSE_T_SHAPE.toString();
		}
		string.append(Unicode.BOTTOM_RIGHT).append("\n").append(Unicode.RESET);
	}

	/**
	 * method that prints in output a leader card with its fancy formatting
	 * @param card the card to be shown on console
	 */
	public static void showLeaderCard(ReducedLeaderCard card) {
		StringBuilder string = new StringBuilder();
		if(card.isAbilitiesActivated()) string.append(Unicode.ANSI_GREEN);
		string.append(Unicode.TOP_LEFT);
		string.append(String.valueOf(Unicode.HORIZONTAL).repeat(Math.max(0, maxLength(card))));
		string.append(Unicode.TOP_RIGHT).append("\n");
		string.append(Unicode.RESET);
		string.append(Unicode.RED_BOLD).append("  LEADER ").append(Unicode.RESET).append(card.getVictoryPoints())
				.append(Resources.VICTORY_POINTS).append("\n");
		if(card.getResourceRequirements().size()!=0)
			string.append("  REQs ").append(ListSet.listMultiplicityToString(card.getResourceRequirements())).append("\n");
		if(card.getCardRequirements().size()!=0)
			string.append("  REQs ").append(ListSet.listMultiplicityToString(card.getCardRequirements())).append("\n");
		for (AbilityEffectActivation abilityEffectActivation : card.getEffectsActivation()) {
			string.append(abilityEffectActivation.toString());
		}
		if(card.isAbilitiesActivated()) string.append(Unicode.ANSI_GREEN);
		string.append(Unicode.BOTTOM_LEFT);
		string.append(String.valueOf(Unicode.HORIZONTAL).repeat(Math.max(0, maxLength(card))));
		string.append(Unicode.BOTTOM_RIGHT).append("\n").append(Unicode.RESET);
		System.out.println(string);
	}

	/**
	 * utility method used for calculating leader card's width on the console output
	 * @param card the leader card to be shown
	 * @return the max lenght required
	 */
	private static int maxLength(ReducedLeaderCard card){
		int max=12;
		int size= (int) (8+4*card.getResourceRequirements().stream().distinct().count());
		StringBuilder s= new StringBuilder();

		if(s.append("  REQs ").append(ListSet.listMultiplicityToString(card.getResourceRequirements()))
				.length()>card.getResourceRequirements().stream().distinct().count()*15)
			size=size+(s.length()-8-(int)card.getResourceRequirements().stream().distinct().count()*15);
		if(size>max)
			max=size;
		size= (int) (6+11*card.getCardRequirements().stream().distinct().count());
		if(size>max)
			max=size;
		for (AbilityEffectActivation abilityEffectActivation : card.getEffectsActivation()) {
			if (abilityEffectActivation.maxLength() > max)
				max = abilityEffectActivation.maxLength();
		}
		return max;
	}

	/**
	 * shows the development card on console with its fancy formatting
	 * @param card the dev card to be shown on console
	 */
	public static void showDevCard(DevelopmentCard card) {
		StringBuilder string = new StringBuilder();
		string.append(card.getColor().getColorCode()).append(Unicode.TOP_LEFT).append(String.valueOf(Unicode.HORIZONTAL).repeat(26))
				.append(Unicode.TOP_RIGHT).append("\n").append(Unicode.RESET).append("  LVL ");

		if(card.getLevel() == 1) {
			string.append(Unicode.DOT).append("    ");
		} else if(card.getLevel() == 2) {
			string.append(Unicode.DOT).append(" ").append(Unicode.DOT).append("  ");
		} else {
			string.append(Unicode.DOT).append(" ").append(Unicode.DOT).append("  ").append(Unicode.DOT);
		}
		string.append(Unicode.RESET+"        ").append(card.getProductionRules().getFaithOutput())
				.append(Unicode.RED_BOLD).append(Unicode.CROSS2).append(Unicode.RESET).append(" ")
				.append(card.getVictoryPoints()).append(Resources.VICTORY_POINTS).append(" ").append("\n");

		string.append("  REQs ").append(ListSet.listMultiplicityToString(card.getResourcesRequirement())).append("\n");
		string.append("  In : ").append(ListSet.listMultiplicityToString(card.getProductionRules().getInputCopy())).append("\n");
		string.append("  Out: ").append(ListSet.listMultiplicityToString(card.getProductionRules().getOutputCopy())).append("\n");
		string.append(card.getColor().getColorCode()).append(Unicode.BOTTOM_LEFT).append(String.valueOf(Unicode.HORIZONTAL).repeat(26))
				.append(Unicode.BOTTOM_RIGHT).append("\n").append(Unicode.RESET);

		System.out.println(string);
	}

	/**
	 * Clears the CLI terminal.
	 */
	protected static void clearCli() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	/**
	 * Shows a legend to help the understanding of the graphic representation of the track
	 */
	public static void helpFaithTrack(){
		System.out.println("HELP:");
		System.out.println(Unicode.RED_BOLD + "+" + Unicode.RESET + " is your marker");
		System.out.println("The number " + Unicode.UNDERLINE+ "underneath" + Unicode.RESET +
				" the tile indicate its position in the track");
		System.out.println("The number "+ Unicode.UNDERLINE + "inside" + Unicode.RESET +" the tile is the amount of " +
				"Victory Points you get if you are on it or after it at the end of the game");
		System.out.println(Unicode.ANSI_YELLOW + "Yellow" + Unicode.RESET + " frame indicates that this tile is " +
				"part of a Vatican Zone");
		System.out.println(Unicode.ANSI_RED + "Red" + Unicode.RESET + " frame indicates that this tile is a Papal Space");
		System.out.println("[" + Unicode.GREEN_BOLD+ Unicode.TICK + Unicode.RESET + "/" + Unicode.RED_BOLD +
				Unicode.CROSS_REPORT + Unicode.RESET + " Num] shows if you are currently getting " +
				"Victory Points from that Vatican Report and the amount of Victory Points");
	}
}
