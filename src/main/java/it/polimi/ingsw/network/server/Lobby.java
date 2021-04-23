package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Lobby implements Runnable {

	private ArrayList<User> clients;
	private int numberOfPlayers;
	private ClientHandler host;
	//private final GameController gameController;
	public static final Logger LOGGER = Logger.getLogger(Lobby.class.getName());
	
	public Lobby() {
		clients = new ArrayList<>();
		
		//TODO: fix the configuration file name and instantiation of game controller
		//gameController = new GameController("asks for config file");
	}
	
	public void addClient(String nickname, ClientHandler handler, VirtualView view) {
		if (clients.isEmpty()) {
			host = handler;
		}
		handler.setLobby(this);
		clients.add(new User(nickname, handler, view));
		//handler.sendMessage(); //updates the other connected clients that a new user entered the game lobby
	}
	
	public void removeClient(String nickname) {
		//User triple=clients.stream().filter(i-> i.getNickname().equals(nickname)).findFirst().orElse(null);
		//clients.remove(triple);
		clients.removeIf(user -> user.getNickname().equals(nickname));
		// remove virtual view association
		LOGGER.info(() -> "Removed " + nickname + " from the client list.");
	}
	
	public void setNumberOfPlayers() {
		numberOfPlayers = clients.size();
	}
	
	@Override
	public void run() {
		//TODO: ask for configuration file
		
		
		LOGGER.info("Match started");
	}
	
	/**
	 * Forwards a received message from the client to the GameController
	 * @param message the message to be forwarded.
	 */
	public void onMessageReceived(Message message) {
		//gameController.onMessageReceived(message);
	}
	
	/*
	//TODO: handle client disconnection before the match starts and after the match is started

	public synchronized void onDisconnect(ClientHandler clientHandler) {
		String clientNickname = clientHandlerHashMap.entrySet()
				.stream()
				.filter(entry -> clientHandler.equals(entry.getValue()))
				.map(HashMap.Entry::getKey)
				.findFirst()
				.orElse(null);
		
		if (clientNickname != null) {
			//check if the game is started
			removeClient(clientNickname);
			
			//if the game has started
				//broadcast disconnection message via the game controller
				// end the game in GameController
				//clientHandlerHashMap.clear();
		}
	}

	 */

	
	
	private class User {
		private String nickname;
		private ClientHandler handler;
		private VirtualView view;

		public String getNickname() {
			return nickname;
		}

		public User(String nickname, ClientHandler handler, VirtualView view) {
			this.nickname = nickname;
			this.handler = handler;
			this.view = view;
		}
	}
	
}
