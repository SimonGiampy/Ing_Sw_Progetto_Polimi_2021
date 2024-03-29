package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientSideController;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CLI extends ViewObservable implements View {
	
	private final Scanner scanner;
	private String playerNickname;

	public CLI() {
		scanner = new Scanner(System.in);
	}

	/**
	 * it initializes CLI
	 */
	public void initialize() {
		CLIUtils.initialize();
		askServerInfo();
	}
	
	/**
	 * Asks the server address and port to the use
	 */
	public void askServerInfo() {
		HashMap<String, String> serverInfo = new HashMap<>();
		boolean validInput;

		System.out.print("Enter the server address: ");
		String address = scanner.nextLine();
		serverInfo.put("address", address);

		do {
			System.out.print("Enter the server port: ");
			String port = scanner.nextLine();
			if (ClientSideController.isValidPort(port)) {
				serverInfo.put("port", port);
				validInput = true;
			} else {
				System.out.println("Invalid port!");
				validInput = false;
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
		playerNickname = nickname;
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
	public void askInitResources(int number) {
		ArrayList<Resources> resourcesList = new ArrayList<>();
		ArrayList<Integer> resourcesNumber = new ArrayList<>();
		String input;
		String regex = "(COIN|SERVANT|STONE|SHIELD)";
		boolean check;
		if(number == 1) {
			System.out.println("You can choose " + number + " free resource, which one do you want?");
			System.out.println("Type [resource] to select. ([coin], [servant], [shield], [stone])");
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
	public void askInitLeaders(String nickname, ArrayList<ReducedLeaderCard> leaderCards) {
		System.out.println("These are your leader cards, select 2 of them! Type [index,index] of the two you want to keep");
		for (int i = 0; i < leaderCards.size(); i++) {
			System.out.println("Card's number: " + (i + 1));
			CLIUtils.showLeaderCard(leaderCards.get(i));
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
	public void askLeaderAction(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
		int selectedLeader = 0, action = 0; //action: 0 = nothing, 1 = play leader, 2 discard leader
		String input;
		
		boolean check;
		showLeaderCards(nickname, availableLeaders);
		
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
						if (!availableLeaders.get(selectedLeader-1).isPlayable() ||
								availableLeaders.get(selectedLeader-1).isAbilitiesActivated()) {
							System.out.println("Selected leader card is not playable! Choose something else to do.");
							check = false;
						}
					} else { // discards the leader card
						action = 2;
						selectedLeader = Character.getNumericValue(input.charAt(8));
						if (availableLeaders.get(selectedLeader-1).isAbilitiesActivated() ||
								availableLeaders.get(selectedLeader-1).isDiscarded()) {
							System.out.println("Selected leader card is not discardable! Choose something else to do.");
							check = false;
						}
					}
				}
			}
		} while(!check);
		
		
		int finalSelectedLeader = selectedLeader;
		int finalAction = action;
		notifyObserver(obs-> obs.onUpdateLeaderAction(finalSelectedLeader, finalAction));
	}

	@Override
	public void askAction(String nickname,ArrayList<PlayerActions> availableAction) {
		int x = 1;
		for (PlayerActions s: availableAction) {
			System.out.println(x + ": " + s);
			x++;
		}
		x--;
		String playerInput;
		String playerActionRegex = "[1-" + x + "]";
		boolean check;
		System.out.println("What do you want to do? (Type <help> for the instructions about how to read the faith track)");
		do {
			playerInput = scanner.nextLine();
			if(playerInput.equals("help"))
				CLIUtils.helpFaithTrack();
			check = Pattern.matches(playerActionRegex, playerInput);
			if(!check) System.out.println("Selected action is not correct! Type again");
		} while (!check);
		int index = Integer.parseInt(playerInput);
		PlayerActions input = availableAction.get(index-1);
		notifyObserver(obs -> obs.onUpdateAction(input));
	}
	
	@Override
	public void askMarketAction(String nickname, ReducedMarket market) {
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
		String regex = "[0-4],[0-4]";
		System.out.println("You have two White Marbles leaders activated: ");
		System.out.print("1. " + marble.repeat(whiteMarblesInput1) + "-->" + fromWhiteMarble1);
		System.out.print("2. " + marble.repeat(whiteMarblesInput2) + "-->" + fromWhiteMarble2);
		System.out.println("You have obtained " + howMany + " white marbles from the market, choose how many times to activate " +
				"each leader. (You can't discard white marbles if it's possible to convert them)");
		System.out.println("Type <quantity1>,<quantity2>. (for example: [1,1])");
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
			regexGoingToWarehouse = "move\s(([1-" + depot.getIncomingResources().size() + "]\sfrom\sdeck)|([1-6]\sfrom\sdepot))\sto\s[1-6]";
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
		
		showDepot(playerNickname, depot); // shows the current configuration of the warehouse
		
		boolean checkGoingToWarehouse = false, checkGoingToDeck = false, check;
		int origin = 0, destination = 0;
		String fromWhere = "", toWhere = "";

		do {
			read = scanner.nextLine().toLowerCase(); // user input
			if (read.equals("confirm") && confirmationAvailable) {
				check = true;
				confirming = true;
			} else if (read.equals("help")) {
				CLIUtils.helpDepot();
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
	public void askBuyCardAction(String nickname, ArrayList<DevelopmentCard> cardsAvailable, boolean wrongSlot) {
		String input;
		
		if (wrongSlot) {
			System.out.println("You can't place that development card in the slot you chose!" +
					" Retype the card number and choose a different slot.");
		}

		String regex;
		if (cardsAvailable.size() >= 10) {
			regex = "([1-9]|[1][0-" + cardsAvailable.size() % 10 + "])";
		}  else {
			regex = "[1-" + cardsAvailable.size() + "]";
		}
		System.out.println("Available Development Cards: ");
		for(int i = 0; i < cardsAvailable.size(); i++){
			System.out.println("Card's number: " + (i + 1));
			CLIUtils.showDevCard(cardsAvailable.get(i));
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
	public void askProductionAction(String nickname,ArrayList<Productions> productionAvailable) {
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
			x = input.split(", ");
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

	private void freeChoice(int number, int flag){
		ArrayList<Resources> resourcesList = new ArrayList<>();
		ArrayList<Integer> resourcesNumber = new ArrayList<>();
		String regex = "[1-" + number + "]X(COIN|SERVANT|STONE|SHIELD)";
		if(flag==1)
			System.out.println("You have " + number + " free choice resources in input");
		else
			System.out.println("You have " + number + " free choice resources in output");
		System.out.println("Type <NUMBER>x<RESOURCE>, [<NUMBER>x<RESOURCE>,...] to select them (for example: [2xshield])");
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
				splitCommands = input.split(", ");
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
		notifyObserver(obs -> obs.onUpdateResourceChoice(resourcesList, resourcesNumber, flag));
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
	public void showFaithTrack(String nickname, ReducedFaithTrack faithTrack) {
		if(nickname.equals(playerNickname)) {
			if (faithTrack.isLorenzosTrack()) {
				System.out.println("Lorenzo's Faith Track");
			} else {
				System.out.println("Your Faith Track");
			}
		} else {
			System.out.println(nickname + "'s Faith Track");
		}
		CLIUtils.showFaithTrack(faithTrack);
	}
	
	@Override
	public void showDepot(String nickname, ReducedWarehouseDepot depot) {
		if(nickname.equals(playerNickname))
			System.out.println("Your Warehouse Depot");
		else
			System.out.println(nickname + "'s Warehouse Depot");
		CLIUtils.showDepot(depot);
	}

	@Override
	public void showLeaderCards(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
		if(nickname.equals(playerNickname)) {
			System.out.println("Your Leader Cards");
			CLIUtils.showMyLeaderCards(availableLeaders);
		}
		else {
			System.out.println(nickname + "'s Leader Cards");
			CLIUtils.showOtherLeaderCards(availableLeaders);
		}

	}

	@Override
	public void showMarket(ReducedMarket market) {
		CLIUtils.showMarket(market);
	}
	
	@Override
	public void showPlayerCardsAndProduction(String nickname, ReducedCardProductionManagement cardProductionsManagement) {
		if(nickname.equals(playerNickname))
			System.out.println("Your Cards and Production Management");
		else
			System.out.println(nickname + "'s Cards and Production Management");
		CLIUtils.showPlayerCardsAndProduction(cardProductionsManagement);
	}
	
	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
		CLIUtils.showCardsDeck(deck);
	}
	
	@Override
	public void showStrongBox(String nickname, ReducedStrongbox strongbox) {
		if(nickname.equals(playerNickname))
			System.out.println("Your Strongbox");
		else
			System.out.println(nickname + "'s Strongbox");
		if(strongbox.getContent().isEmpty())
			System.out.println("EMPTY");
		else
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
		CLIUtils.showToken(token, color);
	}

	
	@Override
	public void showWinMessage(String winner, int points) {
		System.out.println("Match ended! The winner is " + winner + " with " + points + " points!");
	}


	@Override
	public void connectionError(){
		System.out.println("Error! Connection failed!");
		askServerInfo();
	}

	public String getPlayerNickname() {
		return playerNickname;
	}
}
