package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GameController {
	
	private static Scanner scanner;
	private static GameMechanicsMultiPlayer mechanics;

	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		
		GameController test = new GameController();
		test.startGame();
	}
	public void startGame() {
		
		mechanics = new GameMechanicsMultiPlayer(this, 3);
		
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		
		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		mechanics.instantiateGame(devCards, leaderCards, baseProduction, tiles, report);
		
		for (Player p: mechanics.getPlayers()) {
			p.chooseTwoLeaders(1, 4); // chooses 1 and 4 leader
		}
		int indexCurrentPlayer = mechanics.getStartingPlayer();

		for(int round = 0; round < 6; round++) {
			scanner.nextLine();
			Player currentPlayer = mechanics.getPlayer(indexCurrentPlayer); //first test with just one player
			System.out.println("Player " + (indexCurrentPlayer + 1) +" is playing");
			System.out.println("What do you want to do?\nOptions: 1 (Market), 2 (Dev Card), 3 (Production)");
			int input = Integer.parseInt(scanner.nextLine());


			if (input == 1) {
				processMarketInteraction(currentPlayer);

			} else if (input == 2) {
				processBuyDevCard(currentPlayer);
			}
			else if (input == 3) {
				processProduction(currentPlayer);
			}
			indexCurrentPlayer = (indexCurrentPlayer+1) % mechanics.getNumberOfPlayers();
		}
		
	}
	
	/** TODO: move this function somewhere else in the controller part of the project
	 * input management for moving things around in the warehouse
	 * @return a boolean that indicates if all the resources have been moved and if the warehouse configuration is correct
	 *         returns false if more moves are required
	 */
	public boolean processNewMove(WarehouseDepot depot) throws InvalidUserRequestException {
		Scanner scanner = new Scanner(System.in);
		String read;
		
		String regexGoingToWarehouse = "move\s[1-9]\sfrom\s(deck|depot)\sto\s[1-6]"; // regex pattern for reading input for moving the
		// resources to the warehouse
		String regexGoingToDeck = "move\s[1-6]\sto\sdeck"; //regex pattern for reading input for moving back to the deck
		
		depot.showIncomingDeck();
		depot.showDepot();
		System.out.println("write new move command");
		boolean checkGoingToWarehouse, checkGoingToDeck, ok ;
		int from = 0;
		String place = "";
		do {
			read = scanner.nextLine(); // user input
			
			checkGoingToWarehouse = Pattern.matches(regexGoingToWarehouse, read);
			if (checkGoingToWarehouse) { // process request for moving resource from the deck to the warehouse
				from = Character.getNumericValue(read.charAt(5));
				if (read.charAt(14) == 'c') { //send from deck
					place = "deck";
				} else { // send from depot
					place = "depot";
				}
				if (place.equals("deck") && from > depot.getResourceDeckSize()) {
					ok = false; // invalid input: position out of deck bounds (size of list)
				} else ok = !place.equals("depot") || from <= 6; // invalid input: position out of depot bounds (from 1 to 6)
			} else { // sends the request
				checkGoingToDeck = Pattern.matches(regexGoingToDeck, read);
				ok = checkGoingToDeck;
			}
			
			if (!ok) { // user input does not match with the defined pattern
				System.out.println("input request invalid, write again");
			}
		} while (!ok); // while the input is not valid
		
		if (checkGoingToWarehouse) { // process request for moving to the warehouse
			if (place.equals("deck")) {
				return depot.moveResources(place, from, Character.getNumericValue(read.charAt(20)));
			} else {
				return depot.moveResources(place, from, Character.getNumericValue(read.charAt(21)));
			}
		} else { // process request for moving from the warehouse to the deck
			return depot.moveResourcesBackToDeck(Character.getNumericValue(read.charAt(5)));
		}
		
	}
	
	/**
	 * helper function that shows the user how to interact with the CLI
	 * //TODO: move this function in the controller classes for the CLI
	 */
	protected void helpMe() {
		System.out.println("Syntax for moving resources from the deck or depot to the depot is: 'move <position> from <deck/depot> to " +
				"<destination>'");
		System.out.println("Syntax for moving a resource from the warehouse to the deck is : 'move <position> to deck'");
		System.out.println("The positional number in the warehouse is between 1 and 6: from top to bottom, and from left to right");
	}
	
	public Resources[] requestInitialResource(int playerIndex, int howMany) {
		String userResponse = "";
		Resources[] out = new Resources[0];
		if (howMany == 1) {
			out = new Resources[1];
			System.out.println("Player " + (playerIndex+1) + " gets to choose one resource: which one do you want?");
			userResponse = scanner.nextLine().toUpperCase();
			out[0] = Resources.valueOf(userResponse);
		} else if (howMany == 2) {
			out = new Resources[2];
			System.out.println("Player "+ (playerIndex+1) + " gets to choose two resources: which ones do you want?");
			userResponse = scanner.nextLine().toUpperCase();
			out[0] = Resources.valueOf(userResponse);
			userResponse = scanner.nextLine().toUpperCase();
			out[1] = Resources.valueOf(userResponse);
		}
		return out;
	}

	public ArrayList<Integer> playerInputToArraylist(String playerInput){
		ArrayList<Integer> playerProductionInput = new ArrayList<>();
		String[] array = playerInput.split(",");  //Split the previous String for separate by commas
		for(String s:array){  //Iterate over the previous array for put each element on the ArrayList like Integers
			playerProductionInput.add(Integer.parseInt(s));
		}
		return playerProductionInput;
	}
	public int[] playerInputToArray(String playerInput){
		int[] selectedResourcesInput= new int[4];
		String[] array = playerInput.split(",");  //Split the previous String for separate by commas
		for (int i = 0; i < array.length; i++) {
			selectedResourcesInput[i]=Integer.parseInt(array[i]);

		}
		return selectedResourcesInput;
	}

	public void processMarketInteraction(Player currentPlayer){
		String which; // row/column for market (input)
		int where; //selected row/selected column (input)
		mechanics.getMarket().showMarket();
		System.out.println("where do you want to shift the marbles (row / col)?");
		which = scanner.nextLine();
		System.out.println("where do you want to shift the marbles (1-3) / (1-4)?");
		where = Integer.parseInt(scanner.nextLine());

		try {

			currentPlayer.interactWithMarket(which, where);
			ArrayList<Boolean> check = new ArrayList<>();
			for (int i = 0; i < mechanics.getPlayers().length; i++) {
				check.add(mechanics.getPlayers()[i].getPlayerFaithTrack().checkVaticanReport(mechanics.lastReportClaimed));
			}
			if (check.contains(true)) {
				mechanics.lastReportClaimed++;
			}

		} catch (InvalidUserRequestException e) {
			e.printStackTrace();
		}

		//for debugging purposes
			/*
			for(Player p: mechanics.getPlayers()){
				System.out.println(p.getPlayerFaithTrack().getCurrentPosition()+" "+
						p.getPlayerFaithTrack().countFaithTrackVictoryPoints() +" "+p.getPlayerFaithTrack()
						.getVaticanReports().toString());
			}
			 */

		mechanics.getMarket().showMarket();
		System.out.println(currentPlayer.getPlayersResourceDeck().getResourceList().toString());


		//-----------------------------------------------------------------------------

		String in = "no";
		WarehouseDepot depot = currentPlayer.getPlayersWarehouseDepot();
		do {
			try {
				if (processNewMove(depot)) {
					depot.showIncomingDeck();
					depot.showDepot();
					System.out.println("Do you want to confirm (yes / no)? Resources in the deck will be automatically discarded");
					in = scanner.next();
				} else {
					System.out.println("write new move");
				}
			} catch (InvalidUserRequestException e) {
				e.printStackTrace();
			}
		} while (!in.equals("yes") && !in.equals("y"));

		int faithPoints = depot.discardResourcesAfterUserConfirmation();

		if (faithPoints > 0) {
			for (Player p : mechanics.getPlayers()) {
				if (!p.equals(currentPlayer)) {
					p.getPlayerFaithTrack().moveMarker(faithPoints);
				}
			}
		}

	}
	public void processBuyDevCard(Player currentPlayer){
		// dev card buy

		Resources[] resources = new Resources[]{Resources.STONE, Resources.COIN, Resources.COIN,
				Resources.SHIELD, Resources.SHIELD, Resources.SHIELD};
		currentPlayer.getPlayersWarehouseDepot().setDepotForDebugging(resources);

		ArrayList<Resources> resourcesArrayList = new ArrayList<>();
		resourcesArrayList.add(Resources.SERVANT);
		resourcesArrayList.add(Resources.SERVANT);
		resourcesArrayList.add(Resources.SERVANT);
		resourcesArrayList.add(Resources.STONE);
		resourcesArrayList.add(Resources.STONE);
		currentPlayer.getMyStrongbox().storeResources(resourcesArrayList);

		System.out.println("check what this player can do: " + currentPlayer.checkWhatThisPlayerCanDo() + "\n");

		if (currentPlayer.isBuyMoveAvailable()) {
			mechanics.getGameDevCardsDeck().showDevelopmentCardsDeck();
		}

		currentPlayer.getPlayersWarehouseDepot().showDepot();
		System.out.println("strongbox contains: " + currentPlayer.getMyStrongbox().getContent().toString());

		System.out.println("which card do you want to buy?");
		System.out.print("input level: ");
		int level = Integer.parseInt(scanner.nextLine());
		System.out.print("input color: ");
		String color = scanner.nextLine();
		System.out.print("input card slot: ");
		int slot = Integer.parseInt(scanner.nextLine());

		try {
			currentPlayer.buyNewDevCard(level, Colors.valueOf(color.toUpperCase()), slot);
			currentPlayer.getPlayersCardManager().showCards();
		} catch (InvalidInputException e) {
			System.out.println("idiota");
		}
	}
	public void processProduction(Player currentPlayer){

		ArrayList<Integer> playerProductionInput = new ArrayList<Integer>();  //This is the ArrayList where you want to put the String
		String listOfInt; // list of production (input)
		int[] selectedResourcesInput = new int[]{0, 0, 0, 0};
		int[] selectedResourcesOutput = new int[]{0, 0, 0, 0};
		System.out.println("Player Strongbox:" + mechanics.getPlayers()[0].getMyStrongbox().getContent());
		mechanics.getPlayers()[0].getPlayersWarehouseDepot().showDepot();
		System.out.println("which production do you want to activate?");
		listOfInt = scanner.nextLine();
		playerProductionInput = playerInputToArraylist(listOfInt);
		if (mechanics.getPlayers()[0].getPlayersCardManager().numberOfInputEmptySelectedProduction(playerProductionInput) > 0) {
			System.out.println("which resources do you want to put in input?");
			listOfInt = scanner.nextLine();
			selectedResourcesInput = playerInputToArray(listOfInt);
		}
		if (mechanics.getPlayers()[0].getPlayersCardManager().numberOfOutputEmptySelectedProduction(playerProductionInput) > 0) {
			System.out.println("which resources do you want to put in output?");
			listOfInt = scanner.nextLine();
			selectedResourcesOutput = playerInputToArray(listOfInt);
		}
		try {
			mechanics.getPlayers()[0].activateProduction(playerProductionInput, selectedResourcesInput, selectedResourcesOutput);
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		System.out.println(mechanics.getPlayers()[0].getMyStrongbox().getContent());

	}
}
