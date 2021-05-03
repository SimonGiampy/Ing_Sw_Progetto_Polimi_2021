package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientSideController;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.abilities.AbilityEffectActivation;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.observers.ViewObservable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CLI extends ViewObservable implements View {
	
	private final Scanner scanner;

	public CLI() {
		scanner = new Scanner(System.in);
	}

	/**
	 * it initializes CLI
	 */
	public void initialize() {
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
		
		askServerInfo();
	}
	
	/**
	 * Asks the server address and port to the use
	 */
	public void askServerInfo() {
		HashMap<String, String> serverInfo = new HashMap<>();
		String defaultAddress = "localhost";
		String defaultPort = "25000";
		boolean validInput;
		
		do {
			System.out.print("Enter the server address [" + defaultAddress + "]: ");
			
			String address = scanner.nextLine();
			
			if (address.equals("") || address.equals("localhost")) {
				serverInfo.put("address", defaultAddress);
				validInput = true;
			} else if (ClientSideController.isValidIpAddress(address)) {
				serverInfo.put("address", address);
				validInput = true;
			} else {
				System.out.println("Invalid address!");
				clearCli();
				validInput = false;
			}
		} while (!validInput);
		
		do {
			System.out.print("Enter the server port [" + defaultPort + "]: ");
			String port = scanner.nextLine();
			
			if (port.equals("")) {
				serverInfo.put("port", defaultPort);
				validInput = true;
			} else {
				if (ClientSideController.isValidPort(port)) {
					serverInfo.put("port", port);
					validInput = true;
				} else {
					System.out.println("Invalid port!");
					validInput = false;
				}
			}
		} while (!validInput);
		
		notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
	}


	@Override
	public void showLobbyList(ArrayList<String> lobbyList, int idVersion) {
		int lobbyNumber;
		boolean check;
		String regex = "[0-" + lobbyList.size() + "]";
		String input;
		do {
			if (lobbyList.isEmpty())
				System.out.print("There is no existing lobby, type [0] to create a new one and become game host: ");
			else {
				System.out.println("These are the lobbies present in the server: ");
				for (String lobby : lobbyList)
					System.out.println(lobby);
				System.out.print("Choose which lobby to enter or type [0] to create a new one and become game host: ");
			}
			input = scanner.nextLine();
			check = Pattern.matches(regex, input);
		}while(!check);

		lobbyNumber = Integer.parseInt(input);
		notifyObserver(obs -> obs.onUpdateLobbyAccess(lobbyNumber, idVersion));
	}
	
	@Override
	public void showLobbyConfirmation(boolean lobbyAccessed) {
		if(lobbyAccessed)
			System.out.println("You joined the lobby!");
		else
			System.out.println("Cannot connect to the selected lobby");
	}
	
	@Override
	public void askNickname() {
		String nickname;
		System.out.print("Enter your nickname: ");
		do {
			nickname = scanner.nextLine();
			if (nickname.equals("")) System.out.println("Wrong input, type again");
		}while(nickname.equals(""));
		String finalNickname = nickname;
		notifyObserver(obs -> obs.onUpdateNickname(finalNickname));
	}
	
	@Override
	public void askNumberOfPlayer() {
		int playerNumber;
		System.out.println("How many players are going to play? (You can choose between 1 and 4 players): ");
		String input;
		boolean check;
		do {
			input = scanner.nextLine();
			check = Pattern.matches("[1-4]", input);
			if (!check) System.out.print("Input incorrect, write again: ");
		} while (!check);

		playerNumber = Integer.parseInt(input);
		notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
	}
	
	@Override
	public void showNicknameConfirmation(boolean nicknameAccepted) {
		if(nicknameAccepted) {
			System.out.println("Login confirmed! Waiting for the game to start");
		} else {
			System.out.println("Login failed! Your nickname is already taken");
		}
	}
	
	
	@Override
	public void askCustomGame() {
		System.out.println("Choose game configuration!, Type [standard] for standard game, or the full path for a custom game!");
		String input;
		boolean check = true;
		do {
			input = scanner.nextLine();
			if (!input.equals("standard") && !input.equals("")) {
				if (!input.endsWith(".xml")) {
					System.out.println("Invalid file extension: XML file expected");
					check = false;
				} else {
					check = true;
				}
			}
		} while (!check);

		// assumes that the selected file has a correct schema and is a valid configuration file
		String finalInput = input;
		notifyObserver(obs->obs.onUpdateGameConfiguration(finalInput));
	}
	
	@Override
	public void askInitResources(int number) {
		ArrayList<Resources> resourcesList = new ArrayList<>();
		ArrayList<Integer> resourcesNumber = new ArrayList<>();
		String input;
		String regex = "(COIN|SERVANT|STONE|SHIELD)";
		boolean check;
		if(number == 1) {
			System.out.println("You can choose " + number + " free resource, which one do you want?");
			System.out.println("Type [resource] to select. (Example: [coin])");
			do {
				input = scanner.nextLine().toUpperCase();
				check = Pattern.matches(regex, input);
				if (!check) System.out.println("Input incorrect! Type again!");
			} while(!check);

			resourcesList.add(Resources.valueOf(input));
			resourcesNumber.add(1);
		}
		else {
			System.out.println("You can choose " + number + " free resources");
			System.out.println("Type [RESOURCE] to select. For different type of resources " +
					"separate them with a space (Example: [stone servant])");
			String[] selection;
			do {
				input = scanner.nextLine().toUpperCase();
				selection = input.split("\s");
				check = Arrays.stream(selection).allMatch(i -> Pattern.matches(regex, i));
				if(!check) System.out.println("Input incorrect! Type again!");
			}while(!check || selection.length > 2);

			if(selection[0].equals(selection[1]))
				resourcesNumber.add(2);
			else {
				resourcesNumber.add(1);
				resourcesNumber.add(1);
			}
			resourcesList.add(Resources.valueOf(selection[0]));
			resourcesList.add(Resources.valueOf(selection[1]));
		}

		notifyObserver(obs -> obs.onUpdateResourceChoice(resourcesList, resourcesNumber,0));
	}
	
	@Override
	public void askInitLeaders(ArrayList<ReducedLeaderCard> leaderCards) {
		System.out.println("These are your leader cards, select 2 of them! Type [index,index] of the two you want to keep");
		for (int i = 0; i < leaderCards.size(); i++) {
			System.out.println("Card's number: " + (i + 1));
			showLeaderCard(leaderCards.get(i));
		}
			String regex= "[1-4],[1-4]";
			boolean checkRegex;
			boolean checkArray=false;
			String[] array;
			do{
				String input = scanner.nextLine();
				checkRegex=Pattern.matches(regex,input);
				array = input.split(",");
				if (array.length==2)
					checkArray= !array[0].equals(array[1]);

				if (!checkRegex || !checkArray)
					System.out.println("Input incorrect! Type again!");
			}while (!checkRegex || !checkArray);

		ArrayList<Integer> leaderSelection= new ArrayList<>();
		leaderSelection.add(Integer.parseInt(array[0]));
		leaderSelection.add(Integer.parseInt(array[1]));

		notifyObserver(obs-> obs.onUpdateInitLeaders(leaderSelection));
	}
	
	@Override
	public void askLeaderAction(ArrayList<ReducedLeaderCard> availableLeaders) {
		int selectedLeader = 0, action = 0; //action: 0 = nothing, 1 = play leader, 2 discard leader
		String input;
		
		boolean check;
		showLeaderCards(availableLeaders);
		
		System.out.println("Type <action> <number> use the leader card or [0] to do nothing and end the turn.");
		System.out.println("The action can be 'play' or 'discard', where number is 1 or 2");
		
		String regex = "((play|discard)\s[1-" + availableLeaders.size() + "])|0";
		do {
			input = scanner.nextLine().toLowerCase();
			check = Pattern.matches(regex, input);
			if(!check) {
				System.out.println("Input incorrect or selected action not available");
			} else {
				if (input.equals("0")) {
					selectedLeader = 0;
					action = 0;
				} else {
					if (input.charAt(0) == 'p') { // plays the leader card
						action = 1;
						selectedLeader = Character.getNumericValue(input.charAt(5));
						if (!availableLeaders.get(selectedLeader-1).isPlayable()) {
							System.out.println("Selected leader card is not playable! Choose something else to do.");
							check = false;
						}
					} else { // discards the leader card
						action = 2;
						selectedLeader = Character.getNumericValue(input.charAt(8));
					}
				}
			}
		} while(!check);
		
		
		int finalSelectedLeader = selectedLeader;
		int finalAction = action;
		notifyObserver(obs-> obs.onUpdateLeaderAction(finalSelectedLeader, finalAction));
	}
	
	@Override
	public void askAction(ArrayList<PlayerActions> availableAction) {
		int x = 1;
		for (PlayerActions s: availableAction) {
			System.out.println(x + ": " + s);
			x++;
		}
		x--;
		String playerInput;
		String playerActionRegex = "[1-" + x + "]";
		boolean check;
		System.out.println("What do you want to do?");
		do {
			playerInput = scanner.nextLine();
			check = Pattern.matches(playerActionRegex, playerInput);
			if(!check) System.out.println("Selected action is not correct! Type again");
		} while (!check);
		int index = Integer.parseInt(playerInput);
		PlayerActions input = availableAction.get(index-1);
		notifyObserver(obs -> obs.onUpdateAction(input));
	}
	
	@Override
	public void askMarketAction(ReducedMarket market) {
		String input;
		String regex = "((COL\s[1-4])|(ROW\s[1-3]))";
		System.out.println("This is the Market in this moment!\n" +
				"Syntax for using the market: [<row/col> <num>] where num indicates the row or column where to shift the marbles");
		showMarket(market);
		boolean check;
		do {
			input = scanner.nextLine().toUpperCase();
			check = Pattern.matches(regex, input);
			if(!check)
				System.out.println("Input incorrect! Type again!");
		} while (!check);

		String which = input.substring(0, 3);
		int where = Character.getNumericValue(input.charAt(4));
		notifyObserver(obs->obs.onUpdateMarketAction(which, where));
 
	}

	@Override
	public void askWhiteMarbleChoice(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2,
									 int whiteMarblesInput1, int whiteMarblesInput2, int howMany) {
		String marble = Marbles.WHITE + "\uD83D\uDFE3" + Unicode.RESET;
		String regex = "[1-4],[1-4]";
		System.out.println("You have two White Marbles leaders activated: ");
		System.out.print("1. " + marble.repeat(whiteMarblesInput1) + "-->" + fromWhiteMarble1);
		System.out.print("2. " + marble.repeat(whiteMarblesInput2) + "-->" + fromWhiteMarble2);
		System.out.println("You have obtained " + howMany + "white marbles from the market, choose how many times to activate" +
				"each leader (You can't discard white marbles if it's possible to convert them)");
		System.out.println("Type [quantity1,quantity2] (ex. [1,1])");
		String input;
		boolean check;
		do{
			input = scanner.nextLine();
			check = Pattern.matches(regex, input);
			if(!check) System.out.println("Input incorrect! Type again");
		}while(!check);

		int quantity1 = input.charAt(0),  quantity2 = input.charAt(2);

		notifyObserver(obs->obs.onUpdateWhiteMarbleChoice(quantity1 ,quantity2));
	}

	@Override
	public void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		String read;
		boolean confirming = false;

		// regex pattern for reading input for moving the resources to the warehouse
		String regexGoingToWarehouse;
		if (depot.getIncomingResources().size() == 0) {
			regexGoingToWarehouse = "move\s[1-6]\sfrom\sdepot\sto\s[1-6]";
		} else {
			regexGoingToWarehouse = "move\s(([1-" + depot.getIncomingResources().size() + "]\sfrom\sdeck)|([1-6]\sfrom\sdepot))\sto[1-6]";
		}
		String regexGoingToDeck = "move\s[1-6]\sto\sdeck"; //regex pattern for reading input for moving back to the deck
		
		if (initialMove) { // confirmation cannot be available
			System.out.println("Write commands to position the resources in the warehouse depot." +
					" Type <help> for understanding the commands syntax");
		} else if (confirmationAvailable) { // the player can confirm its actions
			System.out.println("The current configuration is valid: you can confirm it by typing <confirm>.");
		}
		if (!inputValid) { // previous input was not valid
			System.out.println("The positioning you requested is not valid: write a different move.");
		}
		
		showDepot(depot); // shows the current configuration of the warehouse
		
		boolean checkGoingToWarehouse = false, checkGoingToDeck = false, check;
		int origin = 0, destination = 0;
		String fromWhere = "", toWhere = "";
		
		do {
			read = scanner.nextLine().toLowerCase(); // user input
			if (read.equals("confirm") && confirmationAvailable) {
				check = true;
				confirming = true;
			} else if (read.equals("help")) {
				System.out.println("Syntax for moving resources from the deck or depot to the depot is: " +
						"[move <position> from <deck/depot> to <destination>]");
				System.out.println("Syntax for moving a resource origin the warehouse to the deck is : [move <position> to deck]");
				System.out.println("The positional number in the warehouse is between 1 and 6: origin top to bottom, and origin left to right");
				check = false;
			} else {
				checkGoingToWarehouse = Pattern.matches(regexGoingToWarehouse, read);
				checkGoingToDeck = Pattern.matches(regexGoingToDeck, read);
				check = checkGoingToDeck || checkGoingToWarehouse;
				if (!check) { // user input does not match with the defined pattern
					System.out.println("Input request invalid, write again");
				}
			}

		} while (!check); // while the input is not valid

		if (checkGoingToWarehouse) { // sends the request for moving resource from the deck or depot to the warehouse
			toWhere = "depot";
			if (read.charAt(14) == 'c') { //send origin deck
				fromWhere = "deck";
				destination = Character.getNumericValue(read.charAt(20));
			} else { // send origin depot
				fromWhere = "depot";
				destination = Character.getNumericValue(read.charAt(21));
			}
			origin = Character.getNumericValue(read.charAt(5));
		} else if (checkGoingToDeck) { // sends the request for moving resources from the warehouse to the deck
			origin = Character.getNumericValue(read.charAt(5));
			fromWhere = "depot";
			toWhere = "deck";
		}
		
		String finalFromWhere = fromWhere;
		String finalToWhere = toWhere;
		int finalOrigin = origin;
		int finalDestination = destination;
		boolean finalConfirming = confirming;
		// notifies the server with the choices of the player
		notifyObserver(obs -> obs.onUpdateDepotMove(finalFromWhere, finalToWhere, finalOrigin, finalDestination, finalConfirming));

	}
	
	
	@Override
	public void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable, boolean wrongSlot) {
		String input;
		
		if (wrongSlot) {
			System.out.println("You can't place that development card in the slot you chose!" +
					" Retype the card number and choose a different slot.");
		}
		
		String regex = "[1" + cardsAvailable.size() + "]";
		System.out.println("Available Development Cards: ");
		for(int i = 0; i < cardsAvailable.size(); i++){
			System.out.println("Card's number: " + (i + 1));
			showDevCard(cardsAvailable.get(i));
		}
		System.out.println("Type the number of the development card you want to buy");
		boolean check;
		do {
			input = scanner.nextLine();
			check = Pattern.matches(regex, input);
			if(!check) System.out.println("Invalid input! Type again");
		} while(!check);

		int num = Integer.parseInt(input) - 1;
		Colors color = cardsAvailable.get(num).getColor();
		int level = cardsAvailable.get(num).getLevel();
		
		System.out.println("Type the slot where you want to put the selected card");
		String regex2 = "[1-3]";
		do {
			input = scanner.nextLine();
			check = Pattern.matches(regex2, input);
			if(!check) System.out.println("Invalid input! Type again");
		} while(!check);
		
		int slot = Integer.parseInt(input);
		notifyObserver(obs -> obs.onUpdateBuyCardAction(color, level, slot));
	}
	
	@Override
	public void askProductionAction(ArrayList<Productions> productionAvailable) {
		System.out.println("These are your available production! Remember that not necessarily all productions " +
				"can be activated at the same time!");
		
		for (int i = 0; i < productionAvailable.size(); i++) {
			System.out.println((i+1) + ": " + productionAvailable.get(i).toString());
		}
		String regex = "[1-" + productionAvailable.size() + "]";
		
		System.out.println("Write the list of productions you want to use in this turn, as a list of integers separated by spaces." +
						" For example type: '1, 3, 4'");
		boolean check;
		String input;
		String[] x;
		do {
			input = scanner.nextLine();
			x = input.split(",");
			check = true;
			for (String s : x) {
				if (!Pattern.matches(regex, s)) {
					check = false;
				}
			}
			if (!check) {
				System.out.println("Input is not valid, type again.");
			}
		} while (!check);
		
		ArrayList<Productions> selection = new ArrayList<>();
		for (String s: x) {
			selection.add(productionAvailable.get(Integer.parseInt(s) - 1));
		}
		notifyObserver(obs -> obs.onUpdateProductionAction(selection));

	}

	private  void freeChoice(int number, int flag){
		ArrayList<Resources> resourcesList = new ArrayList<>();
		ArrayList<Integer> resourcesNumber = new ArrayList<>();
		String regex = "[1-" + number + "]X(COIN|SERVANT|STONE|SHIELD)";
		if(flag==1)
			System.out.println("You have " + number + " free choice resources in input");
		else
		System.out.println("You have " + number + " free choice resources in output");
		System.out.println("Type [NUMBERxRESOURCE,NUMBERxRESOURCE,...] to select them (ex. [2xshield])");
		String input;
		String [] splitCommands;
		String [] singleCommand;
		boolean check = false;
		do {
			input = scanner.nextLine().toUpperCase();
			if(Pattern.matches(regex, input)){
				singleCommand = input.split("X");
				resourcesNumber.add(Integer.parseInt(singleCommand[0]));
				resourcesList.add(Resources.valueOf(singleCommand[1]));
				check = true;
			} else {
				splitCommands = input.split(",");
				for (String s : splitCommands) {
					if (Pattern.matches(regex, s)) {
						singleCommand = s.split("X");
						resourcesNumber.add(Integer.parseInt(singleCommand[0]));
						resourcesList.add(Resources.valueOf(singleCommand[1]));
					} else break;
					check = true;
				}
			}
			if (resourcesNumber.stream().mapToInt(i -> i).sum() != number ||
					resourcesList.stream().distinct().count() != resourcesNumber.size()) check = false;
			if (!check) System.out.println("Input incorrect! Type again!");
		}while(!check);
		notifyObserver(obs -> obs.onUpdateResourceChoice(resourcesList, resourcesNumber,flag));
	}

	@Override
	public void askFreeInput(int number) {
		freeChoice(number,1);
	}

	@Override
	public void askFreeOutput(int number) {
		freeChoice(number,2);
	}
	
	
	@Override
	public void showGenericMessage(String genericMessage) {
		System.out.println("Message from the server: " + genericMessage);
	}
	
	
	@Override
	public void disconnection(String text, boolean termination) {
		System.out.println(text);
		if (termination) {
			System.out.println("Exiting from game.");
		}
	}
	
	@Override
	public void showError(String error) {
		System.out.println("Error: " + error);
	}

	/**
	 * Show all the track with the current position, the activated reports, the victory points for every tile
	 * and the number that identifies the tiles
	 * @param faithTrack to be shown on console
	 */
	@Override
	public void showFaithTrack(ReducedFaithTrack faithTrack) {

		StringBuilder string = new StringBuilder();
		StringBuilder markerColor = new StringBuilder();
		
		boolean lorenzo = faithTrack.isSinglePlayer();
		if(!lorenzo) markerColor.append(Unicode.RED_BOLD);
		else markerColor.append(Unicode.BLACK_BOLD);
		
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
			appendTopFrame(string, faithTrack);

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
				if(faithTrack.getTrack().get(i).tilePoints() != currentVictoryPoints){
					if(i == faithTrack.getCurrentPosition()) {
						string.append(Unicode.BLACK_BOLD).append(faithTrack.getTrack().get(i).tilePoints())
								.append(Unicode.WHITE_BACKGROUND_BRIGHT);
					}else{
						string.append(Unicode.RESET).append(faithTrack.getTrack().get(i).tilePoints());
					}
					if(faithTrack.getTrack().get(i).tilePoints() < 10){
						string.append("  ");
					}
					else{
						string.append(" ");
					}
					string.append(currentColor);
					currentVictoryPoints = faithTrack.getTrack().get(i).tilePoints();
				}else{
					string.append("   ");
				}
				string.append(Unicode.RESET);
			}
			string.append(Unicode.ANSI_RED).append(Unicode.VERTICAL).append("\n").append(Unicode.RESET);

			//Last row of "-"
			appendBottomFrame(string, faithTrack);

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
	private void appendTopFrame(StringBuilder string, ReducedFaithTrack faithTrack) {
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
	private void appendBottomFrame(StringBuilder string, ReducedFaithTrack faithTrack) {
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
	
	@Override
	public void showDepot(ReducedWarehouseDepot depot) {
		ArrayList<Resources> incomingResources = depot.getIncomingResources();
		Resources[] pyr = depot.getDepot();
		
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
				for (int r = 0; r <= depot.getExtraDepotResources().get(i).size(); r++) {
					if (depot.getExtraDepotContents().get(i).get(r)) {
						System.out.println(depot.getExtraDepotResources().get(i).get(r).toString() + "  ");
					}
				}
				System.out.print("\n");
			}
		}
	}

	@Override
	public void showLeaderCards(ArrayList<ReducedLeaderCard> availableLeaders) {
		System.out.println("Your Leader Cards:");
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
				showLeaderCard(availableLeaders.get(i));
		}
	}
	
	/**
	 * method that prints in output a leader card with its fancy formatting
	 * @param card the card to be shown on console
	 */
	public void showLeaderCard(ReducedLeaderCard card) {
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
			abilityEffectActivation.appendPower(string);
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
	private int maxLength(ReducedLeaderCard card){
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
	

	@Override
	public void showMarket(ReducedMarket market) {
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
	
	@Override
	public void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement) {
		for (int i = 0; i < 3; i++) {
			if(cardProductionsManagement.getCards().get(i).size()>0)
				showDevCard(cardProductionsManagement.getCards().get(i).peek());
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
	
	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
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
					showDevCard(deck.getCardStackStructure()[i][j].get(0));
				}
			}
			System.out.println("\n");
		}
	}
	
	/**
	 * shows the development card on console with its fancy formatting
	 * @param card the dev card to be shown on console
	 */
	public void showDevCard(DevelopmentCard card) {
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
	
	@Override
	public void showStrongBox(ReducedStrongbox strongbox) {
		System.out.println(ListSet.listMultiplicityToString(strongbox.getContent()));
	}
	
	@Override
	public void showMatchInfo(ArrayList<String> players) {
		System.out.println("The match is starting. The players in the game are: ");
		for (int i = 0; i < players.size() - 1; i++) {
			System.out.print(players.get(i) + ", ");
		}
		System.out.print(players.get(players.size() - 1) + ".\n");
	}

	@Override
	public void showToken(TokenType token, Colors color){
		if(token==TokenType.DISCARD_TOKEN)
			System.out.println("Discard Token activated: 2 "+color+" cards are discarded from the deck!");
		else if(token==TokenType.BLACK_CROSS_TOKEN)
			System.out.println("Black Cross Token activated: Lorenzo marker has moved 2 position forward!");
		else
			System.out.println("Black Cross Token with shuffle activated: Lorenzo marker has moved 1 position forward! " +
					"The stack will be shuffled!");
	}

	
	@Override
	public void showWinMessage(String winner,int points) {
		System.out.println("Match ended! The winner is: " + winner);
	}
	
	/**
	 * Clears the CLI terminal.
	 */
	private void clearCli() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	@Override
	public void connectionError(){
		System.out.println("Error! Connection failed!");
		askServerInfo();
	}
	
}
