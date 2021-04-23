package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.model.util.Unicode;
import it.polimi.ingsw.observer.ViewObservable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

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
	 * Asks the server address and port to the user.
	 *
	 * @throws ExecutionException if the input stream thread is interrupted.
	 */
	public void askServerInfo() throws ExecutionException {
		HashMap<String, String> serverInfo = new HashMap<>();
		String defaultAddress = "localhost";
		String defaultPort = "25000";
		boolean validInput;
		
		System.out.println("Please specify the following settings. The default value is shown between brackets.");
		
		do {
			System.out.print("Enter the server address [" + defaultAddress + "]: ");
			
			String address = scanner.nextLine();
			
			if (address.equals("")) {
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
					out.println("Invalid port!");
					validInput = false;
				}
			}
		} while (!validInput);
		
		notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
	}
	
	@Override
	public void askNickname() {
		System.out.print("Enter your nickname: ");
		String nickname = scanner.nextLine();
		notifyObserver(obs -> obs.onUpdateNickname(nickname));
	}
	
	@Override
	public void askPlayersNumber() {
		int playerNumber;
		String question = "How many players are going to play? (You can choose between 2 or 3 players): ";
		
		try {
			playerNumber = numberInput(2, 3, null, question);
			notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
		} catch (ExecutionException e) {
			System.out.println(STR_INPUT_CANCELED);
		}
	}
	
	@Override
	public void askNumberOfPlayer() {
	
	}
	
	
	@Override
	public void askCustomGame() {
	
	}
	
	@Override
	public void askInitLeaders() {
	
	}
	
	@Override
	public void askLeaderAction(ArrayList<Resources> availableLeaders) {
	
	}
	
	@Override
	public void askAction(ArrayList<Integer> availableAction) {
	
	}
	
	@Override
	public void askMarketAction() {
	
	}
	
	@Override
	public void askDepotMove() {
	
	}
	
	@Override
	public void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable) {
	
	}
	
	@Override
	public void askProductionAction(ArrayList<Integer> productionAvailable) {
	
	}
	
	@Override
	public void showLoginResult(boolean nicknameAccepted) {
	
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
