package it.polimi.ingsw.view;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.abilities.AbilityEffectActivation;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;

import java.util.ArrayList;

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

	public static void showFaithTrack(String nickname , ReducedFaithTrack faithTrack){

		StringBuilder string = new StringBuilder();
		StringBuilder markerColor = new StringBuilder();
		boolean lorenzo = faithTrack.isSinglePlayer();
		if(!lorenzo) markerColor.append(Unicode.RED_BOLD);
		else markerColor.append(Unicode.BLACK_BOLD);

		System.out.println(nickname + "'s Faith Track");
		//Row for vatican reports
		int count = 0;
		for(int i = 0; i < faithTrack.getReportPoints().size(); i++) {
			while (!faithTrack.getTrack().get(count).isPapalSpace()) {
				string.append("      ");
				count++;
			}
			/*if(i > lastReportClaimed){
				string.append(Unicode.BLUE_BOLD).append("   ?  ");

			 */
			if(faithTrack.getVaticanReports().get(i)){
				string.append(Unicode.GREEN_BOLD).append("  ").append(Unicode.TICK).append(" ").append(Unicode.RESET)
						.append(faithTrack.getReportPoints().get(i));
			}else{
				string.append(Unicode.RED_BOLD).append("  ").append(Unicode.CROSS_REPORT).append(" ").append(Unicode.RESET)
						.append(faithTrack.getReportPoints().get(i));
			}
			//✖ ✕ ✔ ✓
			count++;
		}
		string.append("\n").append(Unicode.RESET);

		//First row of "-"
		CLIUtils.appendTopFrame(string, faithTrack);

		//Second row of "|", spaces and marker
		int k = 0;
		String currentColor = Unicode.RESET;
		for(int i = 0; i < faithTrack.getTrack().size(); i++){
			if (k < faithTrack.getReportPoints().size() && faithTrack.getTrack().get(i).isInsideVatican(k)) {
				currentColor = Unicode.ANSI_YELLOW.toString();
				k++;
			}
			if(faithTrack.getTrack().get(i).isPapalSpace()){
				currentColor = Unicode.ANSI_RED.toString();
			}
			if(i > 1 && faithTrack.getTrack().get(i-2).isPapalSpace()){
				currentColor = Unicode.RESET;
			}
			if(i == faithTrack.getCurrentPosition()){
				string.append(currentColor).append(Unicode.VERTICAL).append(Unicode.WHITE_BACKGROUND_BRIGHT).append("  ");
				string.append(markerColor).append(Unicode.CROSS_MARKER)
						.append("  ").append(currentColor);
			}
			else{
				string.append(currentColor).append(Unicode.VERTICAL).append("  ");
				string.append("   ");
			}
		}
		string.append(currentColor).append(Unicode.VERTICAL).append("\n").append(Unicode.RESET);

		//Third row of "|", spaces and victory points (printed only in the tile where they change)
		int currentVictoryPoints = -1;
		int l = 0;
		currentColor = Unicode.RESET;
		for(int i = 0; i < faithTrack.getTrack().size(); i++){
			if (l < faithTrack.getReportPoints().size() && faithTrack.getTrack().get(i).isInsideVatican(l)) {
				currentColor = Unicode.ANSI_YELLOW.toString();
				l++;
			}
			if(faithTrack.getTrack().get(i).isPapalSpace()){
				currentColor = Unicode.ANSI_RED.toString();
			}
			if(i > 1 && faithTrack.getTrack().get(i-2).isPapalSpace()){
				currentColor = Unicode.RESET;
			}
			string.append(currentColor).append(Unicode.VERTICAL);
			if(i == faithTrack.getCurrentPosition()) {
				string.append(Unicode.WHITE_BACKGROUND_BRIGHT);
			}
			string.append("  ");
			if(faithTrack.getTrack().get(i).getVictoryPoints() != currentVictoryPoints){
				if(i == faithTrack.getCurrentPosition()) {
					string.append(Unicode.BLACK_BOLD).append(faithTrack.getTrack().get(i).getVictoryPoints())
							.append(Unicode.WHITE_BACKGROUND_BRIGHT);
				}else{
					string.append(Unicode.RESET).append(faithTrack.getTrack().get(i).getVictoryPoints());
				}
				if(faithTrack.getTrack().get(i).getVictoryPoints() < 10){
					string.append("  ");
				}
				else{
					string.append(" ");
				}
				string.append(currentColor);
				currentVictoryPoints = faithTrack.getTrack().get(i).getVictoryPoints();
			}else{
				string.append("   ");
			}
			string.append(Unicode.RESET);
		}
		string.append(Unicode.ANSI_RED).append(Unicode.VERTICAL).append("\n").append(Unicode.RESET);

		//Last row of "-"
		CLIUtils.appendBottomFrame(string, faithTrack);

		//Number of the tiles
		string.append(Unicode.RESET);
		for(int i = 0; i < faithTrack.getTrack().size(); i++){
			string.append("   ").append(i);
			if(i < 10){
				string.append("  ");
			}
			else{
				string.append(" ");
			}
		}
		System.out.println(string);
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

	public static void showDepot(String nickname, ReducedWarehouseDepot depot){
		ArrayList<Resources> incomingResources = depot.getIncomingResources();
		Resources[] pyr = depot.getDepot();
		if(nickname.equals(""))
			System.out.print("Your Warehouse Depot");
		else
			System.out.print(nickname + "'s Warehouse Depot");
		// shows the incoming resource from the deck if there are any
		if (incomingResources.size() > 0) System.out.print("Resource deck contains: \t");
		for (int i = 1; i <= incomingResources.size(); i++) {
			System.out.print(i + ": " + incomingResources.get(i - 1).toString() + "\t");
		}
		System.out.print("\n");

		// shows the pyramid with the resources in it
		String string = "            " + Unicode.TOP_LEFT + Unicode.HORIZONTAL + Unicode.HORIZONTAL +
				Unicode.HORIZONTAL + Unicode.HORIZONTAL + Unicode.TOP_RIGHT + "\n" +
				"            " + Unicode.VERTICAL + " " + pyr[0].toString() +
				" " + Unicode.VERTICAL + "\n" +
				"        " + Unicode.TOP_LEFT + Unicode.HORIZONTAL + Unicode.HORIZONTAL +
				Unicode.HORIZONTAL + Unicode.BOTTOM_RIGHT + "    " +
				Unicode.BOTTOM_LEFT + Unicode.HORIZONTAL + Unicode.HORIZONTAL +
				Unicode.HORIZONTAL + Unicode.TOP_RIGHT + "\n" +
				"        " + Unicode.VERTICAL + " " + pyr[1].toString() + "      " +
				pyr[2].toString() + " " + Unicode.VERTICAL + "\n" +
				"    " + Unicode.TOP_LEFT + Unicode.HORIZONTAL + Unicode.HORIZONTAL +
				Unicode.HORIZONTAL + Unicode.BOTTOM_RIGHT + "            " +
				Unicode.BOTTOM_LEFT + Unicode.HORIZONTAL + Unicode.HORIZONTAL +
				Unicode.HORIZONTAL + Unicode.TOP_RIGHT + "\n" +
				"    " + Unicode.VERTICAL + " " + pyr[3].toString() +
				"      " + pyr[4].toString() + "  " + "    " + pyr[5].toString() +
				" " + Unicode.VERTICAL + "\n" +
				"    " + Unicode.BOTTOM_LEFT +
				String.valueOf(Unicode.HORIZONTAL).repeat(20) + Unicode.BOTTOM_RIGHT + "\n";
		System.out.println(string);

		// shows additional leaders if activated
		for (int i = 0; i <= 1; i++) {
			if (depot.isLeaderActivated(i)) {
				System.out.print("Leader #" + (i+1) + "'s additional depot = ");
				for (int r = 0; r < depot.getExtraDepotResources().get(i).size(); r++) {
					if (depot.getExtraDepotContents().get(i).get(r)) {
						System.out.println(depot.getExtraDepotResources().get(i).get(r).toString() + "  ");
					}
				}
				System.out.print("\n");
			}
		}
	}

	public static void showLeaderCards(String nickname, ArrayList<ReducedLeaderCard> availableLeaders){
		System.out.println(nickname + "'s Leader cards:");
		for (int i = 0; i < availableLeaders.size(); i++) {
			System.out.println("Card's number: " + (i + 1));
			if (availableLeaders.get(i).isPlayable()) {
				System.out.print("PLAYABLE\n");
			} else if(availableLeaders.get(i).isDiscarded()){
				System.out.print("DISCARDED\n");
			} else{
				System.out.print("\n");
			}
			if(!availableLeaders.get(i).isDiscarded())
				CLIUtils.showLeaderCard(availableLeaders.get(i));
		}
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

	public static void showMarket(ReducedMarket market){
		System.out.println(Unicode.RESET + "Extra ball = " + market.getExtraBall().colorCode + "\uD83D\uDFE3");
		for (int i = 0; i < 3; i++) { // rows
			for (int j = 0; j < 4; j++) { // columns
				System.out.print(market.getMarket()[i][j].colorCode + "\uD83D\uDFE3\t");
			}
			System.out.println(Unicode.RESET+"←");
		}
		for (int j = 0; j < 4; j++) {
			System.out.print(" ↑\t");
		}
		System.out.print("\n");
	}

	public static void showPlayerCardsAndProduction(String nickname, ReducedCardProductionManagement cardProductionsManagement) {
		System.out.println(nickname+ "'s card and production manager");
		for (int i = 0; i < 3; i++) {
			if(cardProductionsManagement.getCards().get(i).size()>0)
				CLIUtils.showDevCard(cardProductionsManagement.getCards().get(i).peek());
			else {
				String string = Unicode.TOP_LEFT +
						String.valueOf(Unicode.HORIZONTAL).repeat(26) +
						Unicode.TOP_RIGHT + "\n\n" +
						"  EMPTY\n  SLOT\n\n" +
						Unicode.BOTTOM_LEFT +
						String.valueOf(Unicode.HORIZONTAL).repeat(26) +
						Unicode.BOTTOM_RIGHT + "\n";
				System.out.println(string);
			}
		}
	}

	public static void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (deck.getCardStackStructure()[i][j].isEmpty()) { // shows empty card slot
					String string = Unicode.TOP_LEFT +
							String.valueOf(Unicode.HORIZONTAL).repeat(26) +
							Unicode.TOP_RIGHT + "\n\n" +
							"  EMPTY\n  SLOT\n\n" +
							Unicode.BOTTOM_LEFT +
							String.valueOf(Unicode.HORIZONTAL).repeat(26) +
							Unicode.BOTTOM_RIGHT + "\n";
					System.out.println(string);
				} else {
					CLIUtils.showDevCard(deck.getCardStackStructure()[i][j].get(0));
				}
			}
			System.out.println("\n");
		}
	}

	public static void showToken(TokenType token, Colors color){
		if(token==TokenType.DISCARD_TOKEN)
			System.out.println("Discard Token activated: 2 "+color+" cards are discarded from the deck!");
		else if(token==TokenType.BLACK_CROSS_TOKEN)
			System.out.println("Black Cross Token activated: Lorenzo marker has moved 2 position forward!");
		else
			System.out.println("Black Cross Token with shuffle activated: Lorenzo marker has moved 1 position forward! " +
					"The stack will be shuffled!");
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
