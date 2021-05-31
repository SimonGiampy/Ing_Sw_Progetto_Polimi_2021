package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.messages.game.client2server.*;
import it.polimi.ingsw.network.messages.game.server2client.*;
import it.polimi.ingsw.network.messages.generic.DisconnectionMessage;
import it.polimi.ingsw.network.messages.generic.ErrorMessage;
import it.polimi.ingsw.network.messages.generic.GenericMessage;
import it.polimi.ingsw.network.messages.login.*;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSideController implements ViewObserver, Observer {

	private Client client;
	private final View view;
	private String nickname;
	
	private final ExecutorService userTask;

	public ClientSideController(View view) {
		this.view = view;
		userTask = Executors.newSingleThreadExecutor();
	}
	
	
	/**
	 * Messages from the server to the client (Reception Rx)
	 * @param message sent to the client
	 */
	@Override
	public void update(Message message) {
		if (message != null) {
			switch(message.getMessageType()) {
				case LOBBY_LIST -> userTask.execute(() -> view.showLobbyList(((LobbyList) message).getLobbies(),
						((LobbyList) message).getIdVersion()));
				case LOGIN_CONFIRMATION -> userTask.execute(() -> view.showLobbyConfirmation(((LobbyConfirmation) message).isConfirmed()));
				case PLAYER_NUMBER_REQUEST -> userTask.execute(view::askNumberOfPlayer);
				case NICKNAME_REQUEST -> userTask.execute(view::askNickname);
				case NICKNAME_CONFIRMATION -> userTask.execute(() ->
						view.showNicknameConfirmation(((NicknameConfirmation) message).isConfirmed()));
				//case GAME_CONFIG_REQUEST -> userTask.execute(view::askCustomGame);
				case MATCH_INFO -> userTask.execute(() -> view.showMatchInfo(((MatchInfo) message).getPlayers()));
				case RESOURCE_CHOICE -> userTask.execute(() -> {
					ResourceChoice choice = (ResourceChoice) message;
					switch (choice.getAction()) {
						case 0 -> view.askInitResources(choice.getNumber());
						case 1 -> view.askFreeInput(choice.getNumber());
						case 2 -> view.askFreeOutput(choice.getNumber());
						default -> view.showError("Wrong info from server");
					}
				});
				case LEADER_SHOW -> userTask.execute(() -> {
					LeaderInteractions show = (LeaderInteractions) message;
					if(show.getAction() == 0)
						view.askInitLeaders(message.getNickname(), show.getLeaderCards());
					else if(show.getAction() == 1)
						view.showLeaderCards(message.getNickname(), show.getLeaderCards());
					else
						view.askLeaderAction(message.getNickname(), show.getLeaderCards());

				});
				case MARKET_SHOW -> userTask.execute(() -> {
					MarketShow marketShow = (MarketShow) message;
					if (!marketShow.isAskAction())
						view.showMarket(marketShow.getMarket());
					else
						view.askMarketAction(message.getNickname(),marketShow.getMarket());
				});
				case WHITE_MARBLE_REQUEST -> userTask.execute(() -> {
					WhiteMarbleRequest m = (WhiteMarbleRequest) message;
					view.askWhiteMarbleChoice(m.getFromWhiteMarble1(), m.getFromWhiteMarble2(),
							m.getWhiteMarblesInput1(), m.getWhiteMarblesInput2(), m.getHowMany());
				});
				case DEPOT_SHOW -> userTask.execute(() -> view.showDepot(message.getNickname(),((DepotShow) message).getDepot()));
				case DEPOT_CONFIRMATION -> userTask.execute(() -> {
					DepotReply c = (DepotReply) message;
					view.replyDepot(c.getDepot(), c.isInitialMove(), c.isConfirmationAvailable(), c.isMoveValid());
				});
				case STRONGBOX_SHOW -> userTask.execute(() ->
						view.showStrongBox(message.getNickname(), ((StrongboxShow) message).getStrongbox()));
				case FAITH_TRACK_SHOW -> userTask.execute(() ->
						view.showFaithTrack((message).getNickname(), ((FaithTrackShow) message).getFaithTrack()));
				case CARDS_DECK_SHOW -> userTask.execute(() ->
						view.showCardsDeck(((CardsDeckShow) message).getCardsDeck()));
				case PLAYER_CARDS_AND_PRODUCTION_SHOW -> userTask.execute(() ->
						view.showPlayerCardsAndProduction(message.getNickname(),
								((PlayerCardsAndProductionShow) message).getCardProductionManagement()));
				case ACTION_REQUEST -> userTask.execute(() ->
						view.askAction(message.getNickname(),((ActionRequest) message).getAvailableAction()));
				case CARDS_SHOW -> userTask.execute(() ->
						view.askBuyCardAction(message.getNickname(),((BuyableDevCards) message).getCards(), ((BuyableDevCards) message).isWrongSlot()));
				case PRODUCTION_SHOW -> userTask.execute(() ->
						view.askProductionAction(message.getNickname(),((ProductionsAvailable) message).getAvailableProduction()));
				case WIN_MESSAGE -> userTask.execute(() ->
						view.showWinMessage(((WinMessage) message).getWinner(),((WinMessage) message).getPoints()));
				case DISCONNECTION_MESSAGE -> { // the only one which must not use taskQueue to show the message
					DisconnectionMessage mess = (DisconnectionMessage) message;
					view.disconnection(mess.getText(), mess.isTermination());
					if (mess.isTermination()) {
						client.disconnect();
					}
				}
				case GENERIC_MESSAGE -> userTask.execute(() -> view.showGenericMessage(((GenericMessage) message).getMessage()));
				case ERROR_MESSAGE -> userTask.execute(() -> view.showError(((ErrorMessage) message).getErrorMessage()));

				case TOKEN_SHOW -> userTask.execute(() -> {
					TokenShow t = (TokenShow) message;
					view.showToken(t.getTokenType(), t.getColor());
				});
			}
		}
	}
	
	@Override
	public void onUpdateServerInfo(HashMap<String, String> serverInfo) {
		try {
			client = new Client(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
		} catch (IOException e) {
			Client.LOGGER.error("Error in connecting to the server: " + e.getMessage());
			view.connectionError();
			return;
		}
		client.attach(this);
		client.readMessage(); // Starts an asynchronous reading from the server.
		client.enablePinger(true);
	}

	@Override
	public void onUpdateLobbyAccess(int lobbyNumber, int idVersion) {
		client.sendMessage(new LobbyAccess(lobbyNumber, idVersion));
	}

	@Override
	public void onUpdateNickname(String nickname) {
		this.nickname = nickname;
		client.sendMessage(new NicknameReply(nickname));
	}

	@Override
	public void onUpdatePlayersNumber(int playerNumber) {
		client.sendMessage(new PlayerNumberReply(playerNumber));
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
	public void onUpdateAction(PlayerActions selectedAction) {
		client.sendMessage(new ActionReply(nickname, selectedAction));
	}

	@Override
	public void onUpdateMarketAction(String which, int where) {
		client.sendMessage(new MarketInteraction(nickname, which, where));
	}

	@Override
	public void onUpdateWhiteMarbleChoice(int quantity1, int quantity2) {
		client.sendMessage(new WhiteMarbleReply(nickname, quantity1, quantity2));
	}

	@Override
	public void onUpdateDepotMove(String fromWhere, String toWhere, int origin, int destination, boolean confirmation) {
		client.sendMessage(new DepotInteraction(nickname, fromWhere, toWhere, origin, destination, confirmation));
	}

	@Override
	public void onUpdateBuyCardAction(Colors color, int level, int slot) {
		client.sendMessage(new BuyCard(nickname, level, color,slot));
	}

	@Override
	public void onUpdateProductionAction(ArrayList<Productions> selectedProduction) {
		client.sendMessage(new ProductionSelection( nickname, selectedProduction));
	}

	@Override
	public void onUpdateResourceChoice(ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber, int flag) {
		client.sendMessage(new ResourcesList(nickname, resourcesList, resourcesNumber,flag));
	}
	
	/**
	 * Validates the given IPv4 address by using a regex.
	 * @param ip the string of the ip address to be validated
	 * @return {@code true} if the ip is valid, {@code false} otherwise.
	 */
	public static boolean isValidIpAddress(String ip) {
		return true;
		/*
		String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		return Pattern.matches(regex, ip);

		 */
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
