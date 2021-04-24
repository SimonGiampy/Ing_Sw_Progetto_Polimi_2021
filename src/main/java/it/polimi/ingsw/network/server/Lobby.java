package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Lobby implements Runnable {

	private ArrayList<User> clients;
	private ArrayList<String> nicknames;
	private int numberOfPlayers;
	
	private ClientHandler host;
	private final GameController gameController;
	public static final Logger LOGGER = Logger.getLogger(Lobby.class.getName());
	
	public Lobby(ClientHandler host) {
		clients = new ArrayList<>();
		nicknames = new ArrayList<>();
		this.host = host;
		
		gameController = new GameController();
		
		
	}
	
	/**
	 * method called by the server for the initialization of the lobby (choose the number of players)
	 */
	public void setUpLobby() {
		//TODO: let the host choose the number of players
		
	}
	
	/**
	 * The request of choosing which game config to be used must be asynchronous for this thread
	 * The game can't be started until the host doesn't choose with config file to use for the match
	 */
	public void setUpGameConfig() {
		//TODO: ask for configuration file to the lobby host
		
		//TODO: instantiation of game controller with the game config
	}
	
	
	
	public void addClient(ClientHandler handler, VirtualView view) {
		
		String nick;
		boolean valid;
		do {
			view.askNickname(); // asks for a nickname (message from the server to the client)
			nick = handler.readNickname();
			valid = nicknames.contains(nick); //checks if the nickname isn't already chosen by another client
			view.showLoginResult(valid); // sends the login result to the client, otherwise
		} while (!valid);
		
		nicknames.add(nick); // memorizes the accepted nickname
		
		handler.setLobby(this);
		clients.add(new User(nick, handler, view));
		
		//broadcastMessage(Message commmunication); //TODO: updates the other connected clients that a new user entered the game lobby
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
	
	/**
	 * Runnable starts when the match starts
	 */
	@Override
	public void run() {
		
		
		
		LOGGER.info("Match started");
	}
	
	//TODO: add broadcast message function via the virtual views
	
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
	
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	
	public int getConnectedClients() {
		return clients.size();
	}
	
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
