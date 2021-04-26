package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.observers.ViewObservable;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class CLI extends ViewObservable implements View {
	
	Scanner scanner;
	private static final String STR_INPUT_CANCELED = "User input canceled.";
	
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
		
		try {
			askServerInfo();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asks the server address and port to the use
	 *
	 * @throws ExecutionException if the input stream thread is interrupted.
	 */
	public void askServerInfo() throws ExecutionException {
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
			} else if (ClientController.isValidIpAddress(address)) {
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
				if (ClientController.isValidPort(port)) {
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
	public void showLobbyList(ArrayList<String> lobbyList) {
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
		notifyObserver(obs -> obs.onUpdateLobbyAccess(lobbyNumber));
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
			String regex= "[1-4],\s[1-4]";
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
		PlayerActions input = availableAction.get(index);
		notifyObserver(obs -> obs.onUpdateAction(input));
	}
	
	@Override
	public void askMarketAction(ReducedMarket market) {
		String input;
		String regex_which = "(COLUMN|COL|ROW)";
		String which;
		int where;
		System.out.println("This is the Market in this moment! Choose row/column and an index!");
		market.showMarket();
		boolean check;
		do{
			System.out.println("Type row to select a row or type column to select a column");
			input=scanner.nextLine().toUpperCase();
			check=Pattern.matches(regex_which,input);
			if(!check)
				System.out.println("Input incorrect! Type again!");
		}while (!check);

		if (input.equals("COLUMN") || input.equals("COL"))
			which="col";
		else which="row";

		do{
			System.out.println("Type the index of the row/column");
			input=scanner.nextLine();
			where=Integer.parseInt(input);
			if(which.equals("col"))
				check=Pattern.matches("[1-4]",input);
			else
				check=Pattern.matches("[1-3]",input);
			if(!check)
				System.out.println("Input incorrect! Type again!");
		}while (!check);

		int finalWhere = where;
		notifyObserver(obs->obs.onUpdateMarketAction(which, finalWhere));
	}
	
	@Override
	public void askDepotMove(ReducedWarehouseDepot depot) {
		String read;
		String where;
		int from, destination = 0;

		// regex pattern for reading input for moving the resources to the warehouse
		String regexGoingToWarehouse = "move\s[1-" + depot.getIncomingResources().size() + "]\sfrom\s(deck|depot)\sto\s[1-6]";
		String regexGoingToDeck = "move\s[1-6]\sto\sdeck"; //regex pattern for reading input for moving back to the deck

		depot.showIncomingDeck();
		depot.showDepot();
		System.out.print("Write new move command (Type [help] for a tutorial, [confirm] for confirmation): ");
		boolean checkGoingToWarehouse = false, checkGoingToDeck, check;
		from = 0;
		where = "";
		do {
			read = scanner.nextLine().toLowerCase(); // user input
			if (read.equals("confirm")) {
				where = "confirm";
				check = false;
			} else if (read.equals("help")) {
				System.out.println("Syntax for moving resources from the deck or depot to the depot is: " +
						"[move <position> from <deck/depot> to <destination>]");
				System.out.println("Syntax for moving a resource from the warehouse to the deck is : [move <position> to deck]");
				System.out.println("The positional number in the warehouse is between 1 and 6: from top to bottom, and from left to right");
				check = false;
			} else {
				checkGoingToWarehouse = Pattern.matches(regexGoingToWarehouse, read);
				if (checkGoingToWarehouse) { // process request for moving resource from the deck to the warehouse
					from = Character.getNumericValue(read.charAt(5));
					if (read.charAt(14) == 'c') { //send from deck
						where = "deck";
					} else { // send from depot
						where = "depot";
					}
					if (where.equals("deck") && from > depot.getIncomingResources().size()) {
						check = false; // invalid input: position out of deck bounds (size of list)
					} else check = !where.equals("depot") || from <= 6; // invalid input: position out of depot bounds (from 1 to 6)
				} else { // sends the request
					checkGoingToDeck = Pattern.matches(regexGoingToDeck, read);
					check = checkGoingToDeck;
				}

				if (!check) { // user input does not match with the defined pattern
					System.out.println("Input request invalid, write again");
				}
			}

		} while (!check); // while the input is not valid

		if (checkGoingToWarehouse) { // process request for moving to the warehouse
			if (where.equals("deck")) {
				destination = Character.getNumericValue(read.charAt(20));
			} else {
				destination = Character.getNumericValue(read.charAt(21));
			}
		}

		String finalWhere = where;
		int finalFrom = from;
		int finalDestination = destination;

		notifyObserver(obs -> obs.onUpdateDepotMove(finalWhere, finalFrom, finalDestination));

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
		notifyObserver(obs -> obs.onUpdateBuyCardAction(color, level));
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
	}
	
	
	@Override
	public void showDisconnectionMessage(String nicknameDisconnected, String text) {
	
	}
	
	@Override
	public void showError(String error) {
	
	}
	
	@Override
	public void showFaithTrack(ReducedFaithTrack faithTrack) {
	
	}
	
	@Override
	public void showDepot(ReducedWarehouseDepot depot) {
	
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
	
	}
	
	@Override
	public void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement) {
	
	}
	
	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
	
	}
	
	@Override
	public void showStrongBox(ReducedStrongbox strongbox) {
	
	}
	
	@Override
	public void showLobby(ArrayList<String> players, int numPlayers) {
	
	}
	
	@Override
	public void showMatchInfo(ArrayList<String> players, String activePlayer) {
	
	}
	
	@Override
	public void showWinMessage(String winner) {
	
	}
	
	/**
	 * Clears the CLI terminal.
	 */
	public void clearCli() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
}
