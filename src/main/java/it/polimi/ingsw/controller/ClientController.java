package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ClientController implements ViewObserver, Observer {

	private Client client;
	private final View view;
	private String nickname;
	
	private final ExecutorService taskQueue;

	public ClientController(View view) {
		this.view = view;
		taskQueue = Executors.newSingleThreadExecutor();
	}
	
	
	/**
	 * Messages from the server to the client (Reception Rx)
	 * @param message sent to the client
	 */
	@Override
	public void update(Message message) {
		if (message != null) {
			switch(message.getMessageType()) {
				case LOBBY_LIST -> view.showLobbyList(((LobbyList) message).getLobbies());
				case LOGIN_CONFIRMATION -> view.showLoginConfirmation(((LoginConfirmation) message).isConfirmed());
				case PLAYER_NUMBER_REQUEST -> view.askNumberOfPlayer();
				case NICKNAME_REQUEST -> view.askNickname();
				case NICKNAME_CONFIRMATION -> view.showNicknameConfirmation(((NicknameConfirmation) message).isConfirmed());
				case GAME_CONFIG_REQUEST -> view.askCustomGame();
				case LOBBY_SHOW -> view.showLobby(((LobbyShow) message).getPlayers(), ((LobbyShow) message).getNumberOfPlayers());
				case RESOURCE_CHOICE -> {
					ResourceChoice choice = (ResourceChoice) message;
					switch (choice.getAction()) {
						case 0 -> view.askInitResources(choice.getNumber());
						case 1 -> view.askFreeInput(choice.getNumber());
						case 2 -> view.askFreeOutput(choice.getNumber());
						default -> view.showError("Wrong info from server");
					}
				}
				case LEADER_SHOW -> {
					LeaderShow show = (LeaderShow) message;
					if(show.isInGame())
						view.askLeaderAction(show.getLeaderCards());
					else
						view.askInitLeaders(show.getLeaderCards());
				}
				case MARKET_SHOW -> {
					if(((MarketShow) message).getAction() == 0)
						view.showMarket(((MarketShow) message).getMarket());
					else
						view.askMarketAction(((MarketShow) message).getMarket());
				}
				case DEPOT_SHOW -> {
					if(((DepotShow) message).getAction() == 0)
						view.showDepot(((DepotShow) message).getDepot());
					else
						view.askDepotMove(((DepotShow) message).getDepot());
				}
				case STRONGBOX_SHOW -> view.showStrongBox(((StrongboxShow) message).getStrongbox());
				case FAITH_TRACK_SHOW -> view.showFaithTrack(((FaithTrackShow) message).getFaithTrack());
				case CARDS_DECK_SHOW -> view.showCardsDeck(((CardsDeckShow) message).getCardsDeck());
				case PLAYER_CARDS_AND_PRODUCTION_SHOW ->
						view.showPlayerCardsAndProduction(((PlayerCardsAndProductionShow) message).getCardProductionManagement());
				case ACTION_REQUEST -> view.askAction(((ActionRequest) message).getAvailableAction());
				case CARDS_SHOW -> view.askBuyCardAction(((CardsShow) message).getCards());
				case PRODUCTION_SHOW -> view.askProductionAction(((ProductionShow) message).getAvailableProduction());
				case MATCH_INFO_SHOW -> view.showMatchInfo(((MatchInfoShow) message).getPlayers(),
						((MatchInfoShow) message).getActivePlayer());
				case WIN_MESSAGE -> view.showWinMessage(((WinMessage) message).getWinner());
				case DISCONNECTION_MESSAGE -> {
					DisconnectionMessage mess = (DisconnectionMessage) message;
					view.showDisconnectionMessage(mess.getNicknameDisconnected(), mess.getMessage());
				}
				case GENERIC_MESSAGE -> view.showGenericMessage(((GenericMessage) message).getMessage());
			}
		}
	}
	
	@Override
	public void onUpdateServerInfo(HashMap<String, String> serverInfo) {
		client = new Client(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
		client.attach(this);
		client.readMessage(); // Starts an asynchronous reading from the server.
		taskQueue.execute(view::askNickname);
	}

	@Override
	public void onUpdateLobbyAccess(int lobbyNumber) {
		client.sendMessage(new LobbyAccess(lobbyNumber));
	}

	@Override
	public void onUpdateNickname(String nickname) {
		this.nickname = nickname;
		client.sendMessage(new NicknameReply(nickname));
	}

	@Override
	public void onUpdatePlayersNumber(int playerNumber) {
		client.sendMessage(new PlayerNumberReply(nickname, playerNumber));
	}

	@Override
	public void onUpdateInitLeaders(ArrayList<Integer> selectedLeaders) {
		client.sendMessage(new LeaderSelection(nickname, selectedLeaders));
	}

	@Override
	public void onUpdateLeaderAction(int selectedLeader, int action) {
		client.sendMessage(new LeaderAction(nickname, selectedLeader, action));
	}

	@Override
	public void onUpdateAction(int selectedAction) {
		client.sendMessage(new ActionReply(nickname, selectedAction));
	}

	@Override
	public void onUpdateMarketAction(String which, int where) {
		client.sendMessage(new InteractionWithMarket(nickname, which, where));
	}

	@Override
	public void onUpdateDepotMove(String where, int from, int destination) {
		client.sendMessage(new DepotInteraction(nickname, where, from, destination));
	}

	@Override
	public void onUpdateBuyCardAction(Colors color, int level) {
		client.sendMessage(new BuyCard(nickname, level, color));
	}

	@Override
	public void onUpdateProductionAction(ArrayList<Integer> selectedProduction) {
		client.sendMessage(new ProductionSelection(nickname, selectedProduction));
	}

	@Override
	public void onUpdateResourceChoice(ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber) {
		client.sendMessage(new ResourcesList(nickname, resourcesList, resourcesNumber));
	}

	@Override
	public void onDisconnection() {
		client.disconnect();
	}
	
	/**
	 * Validates the given IPv4 address by using a regex.
	 * @param ip the string of the ip address to be validated
	 * @return {@code true} if the ip is valid, {@code false} otherwise.
	 */
	public static boolean isValidIpAddress(String ip) {
		String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		return Pattern.matches(regex, ip);
	}
	
	/**
	 * Checks if the given port string is in the range of allowed ports.
	 * @param portStr the ports to be checked.
	 * @return {@code true} if the port is valid, {@code false} otherwise.
	 */
	public static boolean isValidPort(String portStr) {
		try {
			int port = Integer.parseInt(portStr);
			return port >= 1 && port <= 65535;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
