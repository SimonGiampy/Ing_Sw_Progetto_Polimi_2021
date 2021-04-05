package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GameController {
	
	private static Scanner scanner;

	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		
		GameController test = new GameController();
		test.startGame();
	}
	public void startGame() {
		
		GameMechanicsMultiPlayer mechanics = new GameMechanicsMultiPlayer(this, 2);
		
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		
		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devcards = parser.readDevCards();
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
		ProductionRules baseProdu = parser.parseBaseProductionFromXML();
		mechanics.instantiateGame(devcards, leaderCards, baseProdu, tiles, report);
		
		for (Player p: mechanics.getPlayers()) {
			p.chooseTwoLeaders(1, 4); // chooses 1 and 4 leader
		}
		
		
		System.out.println("what do you want to do?\nOptions: 1 (Market), 2 (Dev Card), 3 (Production)");
		int input = Integer.parseInt(scanner.nextLine());
		String which;
		int where;
		if (input == 1) {
			mechanics.getMarket().showMarket();
			System.out.println("where do you want to shift the marbles (row / col)?");
			which = scanner.nextLine();
			System.out.println("where do you want to shift the marbles (1-3) / (1-4)?");
			where = Integer.parseInt(scanner.nextLine()) - 1;
			
			try {
				Player player0 = mechanics.getPlayers()[0]; //first test with just one player
				
				player0.interactWithMarket(which, where);
				ArrayList<Boolean> check = new ArrayList<>();
				for (int i = 0; i < mechanics.getPlayers().length; i++ ) {
					check.add(mechanics.getPlayers()[i].getPlayerFaithTrack().checkVaticanReport(mechanics.lastReportClaimed));
				}
				if(check.contains(true)) {
					mechanics.lastReportClaimed++;
				}
				
			} catch (InvalidUserRequestException e) {
				e.printStackTrace();
			}
			
			//for debugging purposes
			for(Player p: mechanics.getPlayers()){
				System.out.println(p.getPlayerFaithTrack().getCurrentPosition()+" "+
						p.getPlayerFaithTrack().countFaithTrackVictoryPoints() +" "+p.getPlayerFaithTrack()
						.getVaticanReports().toString());
			}

			mechanics.getMarket().showMarket();
			System.out.println(mechanics.getPlayers()[0].getPlayersResourceDeck().getResourceList().toString());
			
			
			//-----------------------------------------------------------------------------
			
			String in = "no";
			WarehouseDepot depot = mechanics.getPlayer(0).getPlayersWarehouseDepot();
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
			
			depot.discardResourcesAfterUserConfirmation();
		} else {
			// input is != 1
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
	
	public String requestInitialResource(int howMany) {
		String userResponse = "";
		if (howMany == 1) {
			System.out.println("you get to choose one resource: which one do you want?");
			userResponse = scanner.nextLine();
		} else if (howMany == 2) {
			System.out.println("you get to choose two resources: which ones do you want (resA,resB)?");
			userResponse = scanner.nextLine();
		}
		return userResponse;
	}
	
}
