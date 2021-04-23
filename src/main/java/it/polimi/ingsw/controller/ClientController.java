package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.server.Client;
import it.polimi.ingsw.network.messages.LoginRequest;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.PlayerNumberReply;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientController implements ViewObserver, Observer {

	private Client client;
	private View view;
	private String nickname;
	
	private final ExecutorService taskQueue;

	public ClientController(View view) {
		this.view = view;
		taskQueue = Executors.newSingleThreadExecutor();
	}
	
	
	/**
	 * Messages from the server to the client
	 * @param message sent to the client (Reception Rx)
	 */
	@Override
	public void update(Message message) {
		if (message != null) {
			if (message.getMessageType() == MessageType.NICKNAME_REQUEST) {
				view.askNickname();
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
	public void onUpdateNickname(String nickname) {
		this.nickname = nickname;
		client.sendMessage(new LoginRequest(nickname));
	}

	@Override
	public void onUpdatePlayersNumber(int playerNumber) {
		client.sendMessage(new PlayerNumberReply(nickname, playerNumber));
	}

	@Override
	public void onUpdateInitLeaders(ArrayList<Integer> selectedLeaders) {

	}

	@Override
	public void onUpdateLeaderAction(ArrayList<Integer> selectedLeaders) {

	}

	@Override
	public void onUpdateAction(int selectedAction) {

	}

	@Override
	public void onUpdateMarketAction(String which, int where) {

	}

	@Override
	public void onUpdateDepotMove() {

	}

	@Override
	public void onUpdateBuyCardAction(Colors color, int level) {

	}

	@Override
	public void onUpdateProductionAction(ArrayList<Integer> selectedProduction) {

	}

	@Override
	public void onDisconnection() {

	}
	
	/**
	 * Validates the given IPv4 address by using a regex.
	 *
	 * @param ip the string of the ip address to be validated
	 * @return {@code true} if the ip is valid, {@code false} otherwise.
	 */
	public static boolean isValidIpAddress(String ip) {
		String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		return ip.matches(regex);
	}
	
	/**
	 * Checks if the given port string is in the range of allowed ports.
	 *
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
