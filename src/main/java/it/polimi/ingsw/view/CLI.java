package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientSideController;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.observers.ViewObservable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CLI extends ViewObservable implements View {
	
	Scanner scanner;
	
	public CLI() {
		scanner = new Scanner(System.in);
	}
	
	public void initialize() {
		System.out.println("\n" +
				" ,ggg, ,ggg,_,ggg,                                                                                                         \n" +
				"dP\"\"Y8dP\"\"Y88P\"\"Y8b                          I8                                                     ,dPYb,                 \n" +
				"Yb, `88'  `88'  `88                          I8                                                     IP'`Yb                 \n" +
				" `\"  88    88    88                       88888888                                                  I8  8I                 \n" +
				"     88    88    88                          I8                                                     I8  8'                 \n" +
				"     88    88    88    ,gggg,gg    ,g,       I8     ,ggg,    ,gggggg,    ,g,             ,ggggg,    I8 dP                  \n" +
				"     88    88    88   dP\"  \"Y8I   ,8'8,      I8    i8\" \"8i   dP\"\"\"\"8I   ,8'8,           dP\"  \"Y8ggg I8dP                   \n" +
				"     88    88    88  i8'    ,8I  ,8'  Yb    ,I8,   I8, ,8I  ,8'    8I  ,8'  Yb         i8'    ,8I   I8P                    \n" +
				"     88    88    Y8,,d8,   ,d8b,,8'_   8)  ,d88b,  `YbadP' ,dP     Y8,,8'_   8)       ,d8,   ,d8'  ,d8b,_                  \n" +
				"     88    88    `Y8P\"Y8888P\"`Y8P' \"YY8P8P 8P\"\"Y8 888P\"Y8888P      `Y8P' \"YY8P8P      P\"Y8888P\"    PI8\"888                 \n" +
				"                                                                                                    I8 `8,                 \n" +
				" ,ggggggggggg,                                                                                      I8  `8,                \n" +
				"dP\"\"\"88\"\"\"\"\"\"Y8,                                                                                    I8   8I                \n" +
				"Yb,  88      `8b                                                                                    I8   8I                \n" +
				" `\"  88      ,8P                                      gg                                            I8, ,8'                \n" +
				"     88aaaad8P\"                                       \"\"                                             \"Y8P'                 \n" +
				"     88\"\"\"\"Yb,     ,ggg,    ,ggg,,ggg,     ,gggg,gg   gg     ,g,       ,g,       ,gggg,gg   ,ggg,,ggg,     ,gggg,   ,ggg,  \n" +
				"     88     \"8b   i8\" \"8i  ,8\" \"8P\" \"8,   dP\"  \"Y8I   88    ,8'8,     ,8'8,     dP\"  \"Y8I  ,8\" \"8P\" \"8,   dP\"  \"Yb i8\" \"8i \n" +
				"     88      `8i  I8, ,8I  I8   8I   8I  i8'    ,8I   88   ,8'  Yb   ,8'  Yb   i8'    ,8I  I8   8I   8I  i8'       I8, ,8I \n" +
				"     88       Yb, `YbadP' ,dP   8I   Yb,,d8,   ,d8b,_,88,_,8'_   8) ,8'_   8) ,d8,   ,d8b,,dP   8I   Yb,,d8,_    _ `YbadP' \n" +
				"     88        Y8888P\"Y8888P'   8I   `Y8P\"Y8888P\"`Y88P\"\"Y8P' \"YY8P8PP' \"YY8P8PP\"Y8888P\"`Y88P'   8I   `Y8P\"\"Y8888PP888P\"Y888\n" +
				"                                                                                                                           \n" +
				"                                                                                                                           \n" +
				"                                                                                                                           \n" +
				"                                                                                                                           \n" +
				"                                                                                                                           \n" +
				"                                                                                                                           \n");
		
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
	public void showLoginConfirmation(boolean lobbyAccessed) {
		if(lobbyAccessed)
			System.out.println("You joined the lobby!");
		else
			System.out.println("Cannot connect to the selected lobby");
	}
	
	@Override
	public void askNickname() {
		System.out.print("Enter your nickname: ");
		String nickname = scanner.nextLine();
		notifyObserver(obs -> obs.onUpdateNickname(nickname));
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
		if(nicknameAccepted)
			System.out.println("Login confirmed! Waiting for the game to start");
		else
			System.out.println("Login failed! Your nickname is already taken");
	}
	
	
	@Override
	public void askCustomGame() {

		System.out.println("Choose game configuration!, Type [standard] for standard game, [custom] for custom game!");
		String input = scanner.nextLine();
		notifyObserver(obs->obs.onUpdateGameConfiguration(input));
		//TODO: check if the input file actually exists in the selected directory and is an xml file.
		//  We assume that if the file is an xml file, is a valid schema for the configuration
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
			input = scanner.nextLine().toUpperCase();
			String[] selection = input.split(" ");
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

		notifyObserver(obs -> obs.onUpdateResourceChoice(resourcesList, resourcesNumber));
	}
	
	@Override
	public void askInitLeaders(ArrayList<ReducedLeaderCard> leaderCards) {
		System.out.println("These are your leader cards, select 2 of them! Type the index of the selected cards!");
		for (int i = 0; i < leaderCards.size(); i++) {
			System.out.println("Card's number: " + (i + 1));
			leaderCards.get(i).showLeader();
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
		int selectedLeader, action;
		String input;
		String[] splitInput;
		String regex = "[1-" + availableLeaders.size() + "]" + "\s" + "[1-2]" ;
		boolean check;
		System.out.println("Your Leader Cards:");
		for (int i = 0; i < availableLeaders.size(); i++) {
			System.out.println("Card's number: " + (i + 1));
			if(availableLeaders.get(i).isPlayable())
				System.out.println("PLAYABLE");
			availableLeaders.get(i).showLeader();
		}
		System.out.println("Type [INDEX ACTION] to play or discard the leader card or [0] to do nothing and end the turn");
		System.out.println("ACTION = 1 -> play, ACTION = 2 -> discard");
		do {
			input = scanner.nextLine();
			if (input.equals("0")) {
				notifyObserver(obs-> obs.onUpdateLeaderAction(0, 0));
				return;
			} else {
				check = Pattern.matches(regex, input);
				splitInput = input.split("\s");
				if(Integer.parseInt(splitInput[1]) == 1 && !(availableLeaders.get(Integer.parseInt(splitInput[0])).isPlayable()))
					check = false;
			}
			if(!check) System.out.println("Input incorrect or selected action not available");
		}while(!check);

		selectedLeader = Integer.parseInt(splitInput[0]);
		action = Integer.parseInt(splitInput[1]);

		notifyObserver(obs-> obs.onUpdateLeaderAction(selectedLeader, action));
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
		String playerActionRegex = "[1-" + x + "]"; //stupid warning: DO NOT TOUCH
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

		if(market.isWhiteMarble1() || market.isWhiteMarble2()){
			if(market.isWhiteMarble1() && market.isWhiteMarble2()){
				System.out.println("You have 2 Marbles Leader Activated!");
				System.out.println("How many times do you want to use the first leader? ");
				int quantity1 = scanner.nextInt();
				System.out.println("How many times do you want to use the second leader? ");
				int quantity2 = scanner.nextInt();
				notifyObserver(obs->obs.onUpdateMarketAction(which, where,quantity1,quantity2));

			}
			else if(market.isWhiteMarble1()){
				System.out.println("How many times do you want to use the first leader? ");
				int quantity1 = scanner.nextInt();
				notifyObserver(obs->obs.onUpdateMarketAction(which, where,quantity1,0));
			}
			else {
				System.out.println("How many times do you want to use the second leader? ");
				int quantity2 = scanner.nextInt();
				notifyObserver(obs->obs.onUpdateMarketAction(which, where,0,quantity2));
			}
		}
		else
			notifyObserver(obs->obs.onUpdateMarketAction(which, where,0,0));
	}
	
	@Override
	public void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		String read;
		boolean confirming = false;

		// regex pattern for reading input for moving the resources to the warehouse
		String regexGoingToWarehouse = "((move\s[1-" + depot.getIncomingResources().size() + "]\sfrom\sdeck\sto\s[1-6])|" +
				"(move\s[1-6]\sfrom\sdepot\sto\s[1-6]))";
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
	public void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable) {
		String input;
		String regex = "[1" + cardsAvailable.size() + "]";
		System.out.println("Available Development Cards: ");
		for(int i = 0; i < cardsAvailable.size(); i++){
			System.out.println("Card's number: " + (i + 1));
			cardsAvailable.get(i).showCard();
		}
		System.out.println("Type the number of the one you want to buy");
		boolean check;
		do {
			input = scanner.nextLine();
			check = Pattern.matches(regex, input);
			if(!check) System.out.println("Invalid input! Type again");
		}while(!check);

		Colors color = cardsAvailable.get(Integer.parseInt(input) - 1).getColor();
		int level = cardsAvailable.get(Integer.parseInt(input) - 1).getLevel();
		notifyObserver(obs -> obs.onUpdateBuyCardAction(color, level, 1)); //TODO: ask for slot
	}
	
	@Override
	public void askProductionAction(ArrayList<Integer> productionAvailable) {

	}
	
	@Override
	public void askFreeInput(int number) {
	
	}
	
	@Override
	public void askFreeOutput(int number) {
	
	}
	
	
	@Override
	public void showGenericMessage(String genericMessage) {
		System.out.println("Message from the server: " + genericMessage);
	}
	
	
	@Override
	public void showDisconnectionMessage(String nicknameDisconnected, String text) {
		System.out.println(nicknameDisconnected + text);
	}
	
	@Override
	public void showError(String error) {
		System.out.println("Error: " + error);
	}

	/**
	 * Show all the track with the current position, the activated reports, the victory points for every tile
	 * and the number that identifies the tiles
	 */
	@Override
	public void showFaithTrack(ReducedFaithTrack faithTrack) {

		StringBuilder string = new StringBuilder();
		StringBuilder markerColor = new StringBuilder();
		//TODO: get this parameter from the controller based on the shown track is Lorenzo's or the player's one
		int marker = 1;
		if(marker == 1)
			markerColor.append(Unicode.RED_BOLD);
		else
			markerColor.append(Unicode.BLACK_BOLD);
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
	 * Support method for showFaithTrack, appends to the string builder the "-" lines that form the top
	 * of the track
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
	 * Support method for showFaithTrack, appends to the string builder the "-" lines that form the bottom
	 * of the track
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
		//System.out.print("deck contains: \t"); TODO: move this
		for (int i = 1; i <= incomingResources.size(); i++) {
			System.out.print(i + ": " + incomingResources.get(i - 1).toString() + "\t");
		}
		System.out.print("\n");
		
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
		
		//TODO: add show methods for showing additional depots
	}

	@Override
	public void showLeaderCards(ArrayList<ReducedLeaderCard> availableLeaders) {
		System.out.println("Your Leader Cards:");
		for (int i = 0; i < availableLeaders.size(); i++) {
			System.out.println("Card's number: " + (i + 1));
			availableLeaders.get(i).showLeader();
		}
	}

	@Override
	public void showMarket(ReducedMarket market) {
		System.out.println("\033[0m" + "extra ball = " + market.getExtraBall() + "\uD83D\uDFE3");
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
				cardProductionsManagement.getCards().get(i).peek().showCard();
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
				if(deck.getCardStackStructure()[i][j].isEmpty())
					System.out.println("EMPTY");
				deck.getCardStackStructure()[i][j].get(0).showCard();
			}
			System.out.println("\n");
		}
	}
	
	@Override
	public void showStrongBox(ReducedStrongbox strongbox) {
		ListSet.showListMultiplicityOnConsole(strongbox.getContent());
	}
	
	@Override
	public void showMatchInfo(ArrayList<String> players) {
		System.out.println("The match is starting. The players in the game are:");
		for (String s: players) {
			System.out.print(s + ", ");
		}
		System.out.print("\n");
	}

	
	@Override
	public void showWinMessage(String winner) {
		System.out.println("Match ended! The winner is: " + winner);
	}
	
	/**
	 * Clears the CLI terminal.
	 */
	public void clearCli() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	@Override
	public void connectionError(){
		System.out.println("Error! Connection failed!");
		askServerInfo();
	}
	
}
