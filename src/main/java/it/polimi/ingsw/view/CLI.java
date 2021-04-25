package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.observers.ViewObservable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
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
		do {
			if (lobbyList.isEmpty())
				System.out.print("There is no existing lobby, type [0] to create a new one and become game host: ");
			else {
				System.out.println("These are the lobbies present in the server: ");
				for (String lobby : lobbyList)
					System.out.println(lobby);
				System.out.print("Choose which lobby to enter or type [0] to create a new one and become game host: ");
			}
			String input = scanner.nextLine();
			lobbyNumber = Integer.parseInt(input);
			check = Pattern.matches(regex, input);
		}while(!check);

		int finalLobbyNumber = lobbyNumber;
		notifyObserver(obs -> obs.onUpdateLobbyAccess(finalLobbyNumber));
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
		boolean check;
		do {
			String input = scanner.nextLine();
			playerNumber = Integer.parseInt(input);
			check = Pattern.matches("[1-4]", input);
			if (!check) System.out.print("Input incorrect, write again: ");
		} while (!check);
		
		int finalPlayerNumber = playerNumber;
		notifyObserver(obs -> obs.onUpdatePlayersNumber(finalPlayerNumber));
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
		//TODO: check if the input file actually exists in the selected directory and is an xml file.
		//  We assume that if the file is an xml file, is a valid schema for the configuration
	}
	
	@Override
	public void askInitResources(int number) {
		ArrayList<Resources> resourcesList = new ArrayList<>();
		ArrayList<Integer> resourcesNumber = new ArrayList<>();
		String userResponse;
		if(number == 1)
			System.out.println("You can choose " + number + " free resource");
		else
			System.out.println("You can choose " + number + " free resources");
		//TODO: complete
		/*
		if (howMany == 1) { //one free choice
			userResponse = scanner.nextLine().toUpperCase();
			out[0] = Resources.valueOf(userResponse);

		} else if (howMany == 2) { // 2 free choices
			out = new Resources[2];
			System.out.println("Player "+ (playerIndex+1) + " gets to choose two resources: which ones do you want [resA,resB]?");
			userResponse = scanner.nextLine().toUpperCase();
			String[] ress = userResponse.split(",");
			out[0] = Resources.valueOf(ress[0]);
			out[1] = Resources.valueOf(ress[1]);
		}

		 */
		notifyObserver(obs -> obs.onUpdateResourceChoice(resourcesList, resourcesNumber));
	}
	
	@Override
	public void askInitLeaders(ArrayList<ReducedLeaderCard> leaderCards) {
	
	}
	
	@Override
	public void askLeaderAction(ArrayList<ReducedLeaderCard> availableLeaders) {
	
	}
	
	@Override
	public void askAction(ArrayList<Integer> availableAction) {
	
	}
	
	@Override
	public void askMarketAction(ReducedMarket market) {
	
	}
	
	@Override
	public void askDepotMove(ReducedWarehouseDepot depot) {
	
	}
	
	@Override
	public void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable) {
	
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
